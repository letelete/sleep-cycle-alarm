package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.activealarms.AlarmsPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ActiveAlarmsAdapter extends RealmRecyclerViewAdapter<Alarm, ActiveAlarmsAdapter.AlarmViewHolder> {

    public ActiveAlarmsAdapter(RealmResults<Alarm> alarms) {
        super(alarms, true);
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlarmViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_access_alarm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder alarmViewHolder, final int position) {
        final Alarm alarm = getItem(position);
        if(alarm != null) {
            alarmViewHolder.bind(alarm);
        }
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.accessAlarm) protected ConstraintLayout itemLayout;
        @BindView(R.id.accessAlarmTitle) protected TextView textTitle;
        @BindView(R.id.accessAlarmSummary) protected TextView textSummary;

        private final AlarmsPresenter alarmsPresenter;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            this.alarmsPresenter = AlarmsPresenter.getService();
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Alarm alarm) {
            final String id = alarm.getId();
            textTitle.setText(alarm.getTitle());
            textSummary.setText(alarm.getSummary());

            itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    alarmsPresenter.deleteAlarmById(id);
                    return false;
                }
            });

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmsPresenter.showEditDialog(alarm);
                }
            });
        }
    }
}
