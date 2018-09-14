package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import org.joda.time.DateTime;

public class WakeUpAtPresenter {

    public static final String TAG = "WakeUpAtPresenterLog";

    public interface ViewContract {

        void showSetTimeDialog();

        void showList();

        void hideList();

        void showEmptyListHint();

        void hideEmptyListHint();

        void showListHelper();

        void hideListHelper();

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
        if (hasView()) {
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
}
