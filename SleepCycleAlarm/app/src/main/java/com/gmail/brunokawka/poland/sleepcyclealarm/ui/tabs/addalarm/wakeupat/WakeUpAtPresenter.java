package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.util.Log;
import android.widget.Toast;

import org.joda.time.DateTime;

public class WakeUpAtPresenter {

    public static final String TAG = "WakeUpAtPresenterLog";

    public interface ViewContract {

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

    private ViewContract viewContract;
    private boolean isDialogShowing;

    public void handleFloatingActionButtonClicked() {
        viewContract.updateCurrentDate();
        showTimeDialog();
    }

    public void showOrHideElementsDependingOnGivenAmountOfItems(int amount) {
        if (amount <= 0) {
            hideWakeUpAtElements();
        } else {
            showWakeUpAtElements();
        }
    }

    private boolean hasView() {
        return viewContract != null;
    }

    public void bindView(ViewContract viewContract) {
        this.viewContract = viewContract;
        if (isDialogShowing) {
            showTimeDialog();
        }
    }

    public void unbindView() {
        this.viewContract = null;
    }

    public void onActivityCreatedSetUp() {
        viewContract.updateCurrentDate();
        viewContract.setLastExecutionDateFromPreferences();
        viewContract.setUpRecycler();
    }

    public void hideWakeUpAtElements() {
        viewContract.hideList();
        viewContract.showEmptyListHint();
        viewContract.hideCardInfo();
    }

    public void showWakeUpAtElements() {
        viewContract.showList();
        viewContract.hideEmptyListHint();
        viewContract.showCardInfo();
        viewContract.tryToUpdateCardInfoContent();
    }

    public void setUpAdapterAndItsContent() {
        viewContract.setUpAdapterAndCheckForContentUpdate();
    }

    public void showTimeDialog() {
        if (hasView() && !isDialogShowing) {
            isDialogShowing = true;
            viewContract.showSetTimeDialog();
        }
    }

    public void dismissTimeDialog() {
        isDialogShowing = false;
    }

    public void passDialogValueToListGenerator(ViewContract.DialogContract dialogContract) {
        viewContract.generateListAndShowLayoutElements(dialogContract.getDateTime());
    }

    public void tryToGenerateAListWithGivenValues(DateTime currentDate, DateTime executionDate) {
        if (executionDate != null) {
            if (WakeUpAtItemsBuilder.isPossibleToCreateNextItem(currentDate, executionDate)) {
                viewContract.updateLastExecutionDate(executionDate);
                showWakeUpAtElements();
                generateList(executionDate);
            } else {
                showTheClosestAlarmToDefinedHour(executionDate);
            }
        } else {
            Log.e(TAG, "executionDate is null");
        }
    }

    private void generateList(DateTime executionDate) {
        viewContract.updateLastExecutionDate(executionDate);
        viewContract.setUpAdapterAndCheckForContentUpdate();
    }

    public void showTheClosestAlarmToDefinedHour(DateTime definedHour) {
        viewContract.showToast(definedHour);
    }

    public void updateCardInfoContent() {
        viewContract.updateCardInfoTitle();
        viewContract.updateCardInfoSummary();
    }
}
