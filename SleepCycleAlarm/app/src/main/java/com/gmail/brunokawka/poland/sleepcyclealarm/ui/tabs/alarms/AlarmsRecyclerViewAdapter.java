package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.alarms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.model.alarms.Alarm;

class AlarmsRecyclerViewAdapter extends RealmRecyclerViewAdapter<Alarm, AlarmsRecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "AlarmsRecyclerViewAdapterLog";

    AlarmsRecyclerViewAdapter(OrderedRealmCollection<Alarm> data) {
        super(data, true);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_access_alarm, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Alarm obj = getItem(position);
        holder.data = obj;

        holder.title.setText("Title");
        holder.summary.setText("Summary");
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView summary;
        public Alarm data;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.titleInListTextView);
            summary = view.findViewById(R.id.sleepTimeTextView);
        }
    }
}
