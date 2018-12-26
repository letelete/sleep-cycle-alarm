package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm.wakeupat;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.WakeUpAtBuildingStrategy;

import org.joda.time.DateTime;

public class WakeUpAtPresenter implements WakeUpAtContract.WakeUpAtPresenter {

    private WakeUpAtContract.WakeUpAtView view;
    private boolean isDialogShowing;
    private ItemsBuilder itemsBuilder;

    @Override
    public void handleFloatingActionButtonClicked() {
        view.updateCurrentDate();
        showTimeDialog();
    }

    @Override
    public void showOrHideElementsDependingOnAmountOfListItems(int amount, 
                                                               DateTime lastExecutionDate) {
        if (view != null) {
            if (amount <= 0) {
                hideWakeUpAtElements();
            } else {
                showWakeUpAtElements(lastExecutionDate);
            }
        }
    }

    private boolean hasView() {
        return view != null;
    }

    @Override
    public void bindView(WakeUpAtContract.WakeUpAtView view) {
        this.view = view;

        this.itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());

        if (isDialogShowing) {
            showTimeDialog();
        }
    }

    public void unbindView() {
        this.view = null;
    }

    @Override
    public void setUpEnvironment() {
        view.updateCurrentDate();
        view.setLastExecutionDateFromPreferences();
        view.setUpRecycler();
    }

    @Override
    public void setUpUIElements(DateTime lastExecutionDate) {
        if (lastExecutionDate == null) {
            hideWakeUpAtElements();
        } else {
            view.setUpAdapterAndCheckForContentUpdate();
        }
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
    public void tryToGenerateAListWithGivenValues(DateTime newChosenExecutionDate, 
                                                  DateTime currentDate,
                                                  DateTime lastExecutionDate) {
        if (itemsBuilder.isPossibleToCreateNextItem(currentDate, newChosenExecutionDate)) {
            updateLastExecutionDate(lastExecutionDate, newChosenExecutionDate);
            showWakeUpAtElements(newChosenExecutionDate);
            view.setUpAdapterAndCheckForContentUpdate();
        } else {
            showTheClosestAlarmToDefinedHour(newChosenExecutionDate);
        }
    }

    private void updateLastExecutionDate(DateTime lastExecutionDate, DateTime newExecutionDate) {
        if (!newExecutionDate.equals(lastExecutionDate)) {
            view.setLastExecutionDate(newExecutionDate);
            view.saveExecutionDateToPreferencesAsString();
        }
    }

    @Override
    public void hideWakeUpAtElements() {
        view.hideList();
        view.showEmptyListHint();
        view.hideInfoCard();
    }

    @Override
    public void showWakeUpAtElements(DateTime lastExecutionDate) {
        view.showList();
        view.hideEmptyListHint();
        view.showInfoCard();

        if (lastExecutionDate != null) {
            updateCardInfoContent();
        } else {
            Log.d(getClass().getName(),
                    "lastExecutionDate is null, couldn't update card info content");
        }
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
