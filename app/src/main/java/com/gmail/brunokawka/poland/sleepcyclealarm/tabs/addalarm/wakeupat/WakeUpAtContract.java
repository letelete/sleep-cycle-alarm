package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm.wakeupat;

import org.joda.time.DateTime;

public interface WakeUpAtContract {

    interface WakeUpAtView {

        void updateCurrentDate();

        void setUpRecycler();

        void showSetTimeDialog();

        void showList();

        void hideList();

        void showInfoCard();

        void hideInfoCard();

        void showEmptyListHint();

        void hideEmptyListHint();

        void setLastExecutionDate(DateTime newDate);

        void setLastExecutionDateFromPreferences();

        void setUpAdapterAndCheckForContentUpdate();

        void saveExecutionDateToPreferencesAsString();

        void updateCardInfoTitle();

        void updateCardInfoSummary();

        void showToast(DateTime definedHour);

        interface DialogContract {
            DateTime getDateTime();
        }
    }

    interface WakeUpAtPresenter {
        void handleFloatingActionButtonClicked();

        void showOrHideElementsDependingOnAmountOfListItems(int amount, DateTime lastExecutionDate);

        void bindView(WakeUpAtContract.WakeUpAtView view);

        void unbindView();

        void setUpEnvironment();

        void setUpUIElements(DateTime lastExecutionDate);

        void showTimeDialog();

        void dismissTimeDialog();

        void tryToGenerateAListWithGivenValues(DateTime newChosenExecutionDate, DateTime currentDate, DateTime lastExecutionDate);

        void hideWakeUpAtElements();

        void showWakeUpAtElements(DateTime lastExecutionDate);

        void showTheClosestAlarmToDefinedHour(DateTime definedHour);

        void updateCardInfoContent();
    }
}
