package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.AmountOfItemsChangedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.ItemInListClickedEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterHolder> {

    private List<Item> listItems;

    private Item getItem(int position) {
        return listItems != null ? listItems.get(position) : null;
    }

    @Override
    public int getItemCount() {
        EventBus.getDefault().post(new AmountOfItemsChangedEvent(listItems.size()));
        return listItems.size();
    }

    public ListAdapter(List<Item> listItems) {
        this.listItems = listItems;
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
        protected TextView textTitle;

        @BindView(R.id.addAlarmSummary)
        protected TextView textSummary;

        ListAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Item item = listItems.get(position);
            EventBus.getDefault().postSticky(new ItemInListClickedEvent(item));
            Log.d(getClass().getName(), String.valueOf(position));
        }
    }
}
