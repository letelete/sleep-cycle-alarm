package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter {

    private final static String TAG = "ListAdapterLog";

    private ArrayList<Item> listItems;
    private RecyclerView recycler;

    public class ListAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.addAlarmTitle)
        TextView textTitle;

        @BindView(R.id.addAlarmSummary)
        TextView textSummary;

        public ListAdapterHolder(View item) {
            super(item);
            ButterKnife.bind(this, item);
        }
    }

    public ListAdapter(ArrayList<Item> listItems, RecyclerView recycler) {
        this.listItems = listItems;
        this.recycler = recycler;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_add_alarm, viewGroup, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add alarm to realm database (EventBus might do the job)
                Log.d(TAG, "User clicked on item in list with id: " + String.valueOf(view.getId()));
            }
        });

        return new ListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Item item = listItems.get(i);
        ((ListAdapterHolder) viewHolder).textTitle.setText(item.getTitle());
        ((ListAdapterHolder) viewHolder).textSummary.setText(item.getSummary());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
