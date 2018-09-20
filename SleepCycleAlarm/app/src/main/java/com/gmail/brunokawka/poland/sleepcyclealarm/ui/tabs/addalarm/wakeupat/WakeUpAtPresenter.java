package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.util.Log;

import org.joda.time.DateTime;

public class WakeUpAtPresenter implements WakeUpAtContract.WakeUpAtPresenter {

    public static final String TAG = "WakeUpAtPresenterLog";


    private WakeUpAtContract.WakeUpAtView view;
    private boolean isDialogShowing;

    @Override
    public void handleFloatingActionButtonClicked() {
        view.updateCurrentDate();
        showTimeDialog();
    }

    @Override
    public void showOrHideElementsDependingOnGivenAmountOfItems(int amount) {
        if (amount <= 0) {
            hideWakeUpAtElements();
        } else {
            showWakeUpAtElements();
        }
    }

    private boolean hasView() {
        return view != null;
    }

    @Override
    public void bindView(WakeUpAtContract.WakeUpAtView view) {
        this.view = view;
        if (isDialogShowing) {
            showTimeDialog();
        }
    }

    public void unbindView() {
        this.view = null;
    }

    @Override
    public void setUpUIElement(DateTime lastExecutionDate) {
        view.updateCurrentDate();
        view.setLastExecutionDateFromPreferences();
        view.setUpRecycler();

        if (lastExecutionDate == null) {
            hideWakeUpAtElements();
        } else {
            view.setUpAdapterAndCheckForContentUpdate();
        }
    }

    @Override
    public void hideWakeUpAtElements() {
        view.hideList();
        view.showEmptyListHint();
        view.hideCardInfo();
    }

    @Override
    public void showWakeUpAtElements() {
        view.showList();
        view.hideEmptyListHint();
        view.showCardInfo();
        view.tryToUpdateCardInfoContent();
    }

    @Override
    public void showTimeDialog() {
        if (hasView() && !isDialogShowing) {
            isDialogShowing = true;
            view.showSetTimeDialog();
        }
    }

    @Override
    public void dismissTimeDialog() {
        isDialogShowing = false;
    }

    @Override
    public void passDialogValueToListGenerator(WakeUpAtContract.WakeUpAtView.DialogContract dialogContract) {
        view.generateListAndShowLayoutElements(dialogContract.getDateTime());
    }

    @Override
    public void tryToGenerateAListWithGivenValues(DateTime currentDate, DateTime executionDate) {
        if (executionDate != null) {
            if (WakeUpAtItemsBuilder.isPossibleToCreateNextItem(currentDate, executionDate)) {
                view.updateLastExecutionDate(executionDate);
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
        view.updateLastExecutionDate(executionDate);
        view.setUpAdapterAndCheckForContentUpdate();
    }

    @Override
    public void showTheClosestAlarmToDefinedHour(DateTime definedHour) {
        view.showToast(definedHour);
    }

    @Override
    public void updateCardInfoContent() {
        view.updateCardInfoTitle();
        view.updateCardInfoSummary();
    }


}
