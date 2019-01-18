package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm.wakeupat;

import org.joda.time.DateTime;

public interface WakeUpAtContract {

    interface WakeUpAtView {

        void updateCurrentDate();

        void showSetTimeDialog();

        void setLastExecutionDate(DateTime newDate);

        void setLastExecutionDateFromPreferences();

        void setupAdapter();

        void saveExecutionDateToPreferencesAsString();

        void updateDescription();

        interface DialogContract {
            DateTime getDateTime();
        }
    }

    interface WakeUpAtPresenter {
        void handleFloatingActionButtonClicked();

        void bindView(WakeUpAtContract.WakeUpAtView view);

        void unbindView();

        void setUpEnvironment();

        void showTimeDialog();

        void dismissTimeDialog();

        void tryToGenerateAListWithGivenValues(DateTime newChosenExecutionDate,
                                               DateTime currentDate, DateTime lastExecutionDate);

    }
}
