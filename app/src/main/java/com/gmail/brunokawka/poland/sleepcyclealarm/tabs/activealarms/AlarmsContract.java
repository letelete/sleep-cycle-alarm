package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.activealarms;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;

public interface AlarmsContract {
    interface AlarmsView {

        void showEditAlarmDialog(Alarm alarm);

        void setupAdapter();

        interface DialogContract {
            void setRingtone(String ringtone);
            String getRingtone();

            void bind(Alarm alarm);
        }
    }

    interface AlarmsPresenter {
        void bindView(AlarmsContract.AlarmsView viewContract);

        void unbindView();

        void handleRealmChange();

        void showEditDialog(Alarm alarm);

        void deleteAlarmById(final String id);

    }
}
