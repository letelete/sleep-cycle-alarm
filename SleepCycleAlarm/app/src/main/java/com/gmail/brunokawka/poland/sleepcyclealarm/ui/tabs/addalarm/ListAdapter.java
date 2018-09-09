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
import com.gmail.brunokawka.poland.sleepcyclealarm.events.SetAlarmEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterHolder> {

    private final static String TAG = "ListAdapterLog";

    private ArrayList<Item> listItems;
    private RecyclerView recycler;

    public Item getItem(int position) {
        return listItems != null ? listItems.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public ListAdapter(ArrayList<Item> listItems, RecyclerView recycler) {
        this.listItems = listItems;
        this.recycler = recycler;
    }

    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int index) {
        return new ListAdapterHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_add_alarm, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder listAdapterHolder, int position) {
        Item item = listItems.get(position);
        listAdapterHolder.item = getItem(position);

        listAdapterHolder.textTitle.setText(item.getTitle());
        listAdapterHolder.textSummary.setText(item.getSummary());
    }

    public class ListAdapterHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        private Item item;

        @BindView(R.id.addAlarmTitle)
        TextView textTitle;

        @BindView(R.id.addAlarmSummary)
        TextView textSummary;

        public ListAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Item item = listItems.get(position);
            EventBus.getDefault().postSticky(new SetAlarmEvent(item));
            Log.d(TAG, String.valueOf(position));
        }
    }
}
