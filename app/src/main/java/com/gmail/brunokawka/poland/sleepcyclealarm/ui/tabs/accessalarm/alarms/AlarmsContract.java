package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

public interface AlarmsContract {
    interface AlarmsView {

        void showAddAlarmDialog();

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
            String getRingtone(); // TODO: not sure how ringtone will be passed like so setted up String for entry testing

            void bind(Alarm alarm);
        }
    }

    interface AlarmsPresenter {
        void bindView(AlarmsContract.AlarmsView viewContract);

        void unbindView();

        void setUpUIDependingOnDatabaseItemAmount();

        void handleRealmChange();

        void showAddDialog();

        void dismissAddDialog();

        void showEditDialog(Alarm alarm);

        void saveAlarm(AlarmsContract.AlarmsView.DialogContract dialogContract, final Item item);

        void deleteAlarmByIdWithDialog(final String id);

        void deleteAlarmById(final String id);

        void editAlarm(final AlarmsContract.AlarmsView.DialogContract dialogContract, final String id);

    }
}
