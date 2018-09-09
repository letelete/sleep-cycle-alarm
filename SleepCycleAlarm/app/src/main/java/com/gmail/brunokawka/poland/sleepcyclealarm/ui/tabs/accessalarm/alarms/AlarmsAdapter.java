package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Alarm;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class AlarmsAdapter extends RealmRecyclerViewAdapter<Alarm, AlarmsAdapter.AlarmViewHolder> {
    private static final String TAG = "AlarmsAdapterLog";

    public AlarmsAdapter(RealmResults<Alarm> alarms) {
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
        private static final String TAG = "AlarmViewHolderLog";

        @BindView(R.id.accessAlarm)
        ConstraintLayout itemLayout;

        @BindView(R.id.accessAlarmTitle)
        TextView textTitle;

        @BindView(R.id.accessAlarmSummary)
        TextView textSummary;

        final Context context;
        final AlarmsPresenter alarmsPresenter;


        public AlarmViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.alarmsPresenter = AlarmsPresenter.getService(context);
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
