package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.util.Log;

import org.joda.time.DateTime;

public class WakeUpAtPresenter {

    public static final String TAG = "WakeUpAtPresenterLog";

    public interface ViewContract {

        void showSetTimeDialog();

        void showList();

        void hideList();

        void showCardInfo();

        void hideCardInfo();

        void showEmptyListHint();

        void hideEmptyListHint();

        void updateCardInfoContent();

        void generateList(DateTime executionDate);

        interface DialogContract {
            DateTime getDateTime();
        }
    }

    private ViewContract viewContract;
    private boolean isDialogShowing;

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

    public void showTimeDialog() {
        if (hasView() && !isDialogShowing) {
            isDialogShowing = true;
            viewContract.showSetTimeDialog();
        }
    }

    public void dismissTimeDialog() {
        isDialogShowing = false;
    }

    public void generateList(ViewContract.DialogContract dialogContract) {
        viewContract.generateList(dialogContract.getDateTime());
    }

    public void showWakeUpAtElements() {
        viewContract.showList();
        viewContract.hideEmptyListHint();
        viewContract.showCardInfo();
        viewContract.updateCardInfoContent();
    }

    public void hideWakeUpAtElements() {
        viewContract.hideList();
        viewContract.showEmptyListHint();
        viewContract.hideCardInfo();
    }

    public void showTheClosestAlarmToDefinedHour(DateTime definedHour) {
        Log.d(TAG, "Showing the closest alarm to defined hour which is: " + definedHour.toString());
        // TODO: issue #6 - github.com/letelete/Sleep-Cycle-Alarm/issues/6
    }
}
