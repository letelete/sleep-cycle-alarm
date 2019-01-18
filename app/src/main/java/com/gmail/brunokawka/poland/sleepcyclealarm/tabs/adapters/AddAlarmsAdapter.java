package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.AddAlarmEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAlarmsAdapter extends RecyclerView.Adapter<AddAlarmsAdapter.ListAdapterHolder> {

    private List<Item> listItems;

    public AddAlarmsAdapter(List<Item> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup,
                                                final int index) {
        return new ListAdapterHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_alarm, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder listAdapterHolder, int position) {
        final Item item = listItems.get(position);
        if (item != null) {
            listAdapterHolder.bind(item);
        }
    }

    public class ListAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cl_item_alarm_root)
        ConstraintLayout clRoot;

        @BindView(R.id.iv_item_alarm_icon)
        ImageView ivIcon;

        @BindView(R.id.tv_item_alarm_title)
        TextView tvTitle;

        @BindView(R.id.tv_item_alarm_subtitle)
        TextView tvSubtitle;

        ListAdapterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final Item item) {
            ivIcon.setImageResource(R.drawable.ic_list_alarm_add_full_shape);
            tvTitle.setText(item.getTitle());
            tvSubtitle.setText(item.getSummary());

            clRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new AddAlarmEvent(item));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
