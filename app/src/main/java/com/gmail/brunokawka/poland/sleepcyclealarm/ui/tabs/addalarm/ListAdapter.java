package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.AmountOfItemsChangedEvent;

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
            final int position = this.getAdapterPosition();
            showAlertDialogForAddAlarmAction(view.getContext(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //TODO: ADD ALARM
                    Log.d(getClass().getName(), String.valueOf(position));
                }
            });

        }

        private void showAlertDialogForAddAlarmAction(@NonNull Context context, DialogInterface.OnClickListener onClickListener) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.add_alarm_dialog_title)
                    .setMessage(context.getString(R.string.add_alarm_dialog_message, item.getTitle()))
                    .setPositiveButton(context.getString(R.string.yes), onClickListener)
                    .setNegativeButton(context.getString(R.string.no), null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
