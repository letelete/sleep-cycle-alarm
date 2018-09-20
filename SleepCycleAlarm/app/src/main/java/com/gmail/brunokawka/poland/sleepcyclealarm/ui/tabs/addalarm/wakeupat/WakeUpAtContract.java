package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import org.joda.time.DateTime;

public interface WakeUpAtContract {

    interface WakeUpAtView {

        void updateCurrentDate();

        void setUpRecycler();

        void showSetTimeDialog();

        void showList();

        void hideList();

        void showCardInfo();

        void hideCardInfo();

        void showEmptyListHint();

        void hideEmptyListHint();

        void tryToUpdateCardInfoContent();

        void generateListAndShowLayoutElements(DateTime executionDate);

        void updateLastExecutionDate(DateTime newDate);

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

        void showOrHideElementsDependingOnGivenAmountOfItems(int amount);

        void bindView(WakeUpAtContract.WakeUpAtView view);

        void unbindView();

        void setUpUIElement(DateTime lastExecutionDate);

        void hideWakeUpAtElements();

        void showWakeUpAtElements();

        void showTimeDialog();

        void dismissTimeDialog();

        void passDialogValueToListGenerator(WakeUpAtContract.WakeUpAtView.DialogContract dialogContract);

        void tryToGenerateAListWithGivenValues(DateTime currentDate, DateTime executionDate);

        void showTheClosestAlarmToDefinedHour(DateTime definedHour);

        void updateCardInfoContent();
    }
}
