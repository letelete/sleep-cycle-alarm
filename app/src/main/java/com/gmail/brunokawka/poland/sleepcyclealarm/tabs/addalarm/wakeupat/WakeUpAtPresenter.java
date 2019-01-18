package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm.wakeupat;

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
            view.setupAdapter();
        }
    }

    private void updateLastExecutionDate(DateTime lastExecutionDate, DateTime newExecutionDate) {
        if (!newExecutionDate.equals(lastExecutionDate)) {
            view.setLastExecutionDate(newExecutionDate);
            view.saveExecutionDateToPreferencesAsString();
            view.updateDescription();
        }
    }
}
