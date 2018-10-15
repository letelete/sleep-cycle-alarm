package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

public interface AlarmsContract {
    interface AlarmsView {

        void showEditAlarmDialog(Alarm alarm);

        void setUpRecycler();

        void setUpAdapter();

        void showList();

        void hideList();

        void showInfoCard();

        void hideInfoCard();

        void showEmptyListHint();

        void hideEmptyListHint();

        interface DialogContract {
            String getRingtone();

            void bind(Alarm alarm);
        }
    }

    interface AlarmsPresenter {
        void bindView(AlarmsContract.AlarmsView viewContract);

        void unbindView();

        void setUpUIDependingOnDatabaseItemAmount();

        void handleRealmChange();

        void showEditDialog(Alarm alarm);

        void deleteAlarmById(final String id);

        void updateEditedAlarm(final AlarmsContract.AlarmsView.DialogContract dialogContract, final Alarm alarm);

    }
}
