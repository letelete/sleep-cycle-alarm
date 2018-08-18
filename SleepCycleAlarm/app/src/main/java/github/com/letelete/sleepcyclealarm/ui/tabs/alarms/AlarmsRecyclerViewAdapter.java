package github.com.letelete.sleepcyclealarm.ui.tabs.alarms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import github.com.letelete.sleepcyclealarm.R;
import github.com.letelete.sleepcyclealarm.model.alarms.Alarm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

class AlarmsRecyclerViewAdapter extends RealmRecyclerViewAdapter<Alarm, AlarmsRecyclerViewAdapter.MyViewHolder> {

    private boolean isDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<>();

    AlarmsRecyclerViewAdapter(OrderedRealmCollection<Alarm> data) {
        super(data, true);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_access_alarm, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Alarm obj = getItem(position);
        holder.data = obj;
        final int itemId = obj.getId();

        holder.title.setText("Title");
        holder.summary.setText("Summary");
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView summary;
        public Alarm data;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.titleInListTextView);
            summary = view.findViewById(R.id.sleepTimeTextView); }

    }
}
