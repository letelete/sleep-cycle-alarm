package com.gmail.brunokawka.poland.sleepcyclealarm.app.base;

import android.os.Bundle;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;


public class MainPresenter implements MainContract.MainPresenter {

    private MainContract.MainView view;
    private boolean isButtonAfterFirstPress;

    public MainPresenter(MainContract.MainView view) {
        this.view = view;
    }

    @Override
    public void setUpUi(Bundle savedInstanceState) {
        view.setUpBottomNavigationBar();
        view.setUpToolbar();
        handleFragmentToDisplay(savedInstanceState);
    }

    private void handleFragmentToDisplay(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            view.openDefaultFragment();
        } else {
            view.openLatestFragment();
        }
    }

    @Override
    public void handleMenuItemClick(int menuItemId) {
        switch (menuItemId) {
            case R.id.menu_settings:
                view.openSettingsActivity();
                break;
            default:
                break;
        }
    }

    @Override
    public void handleBottomNavigationTabClick(int menuItemId) {
        switch (menuItemId) {
            case R.id.action_wakeupat:
                view.openWakeUpAtFragment();
                break;
            case R.id.action_alarms:
                view.openAlarmsFragment();
                break;
            case R.id.action_sleepnow:
            default:
                view.openSleepNowFragment();
                break;
        }

        handleWakeUpAtButtonVisibility(menuItemId);
    }

    private void handleWakeUpAtButtonVisibility(int menuItemId) {
        if (isWakeUpAtTabOpen(menuItemId)) {
            view.showWakeUpAtActionButton();
        } else {
            view.hideWakeUpAtActionButton();
        }
    }

    private boolean isWakeUpAtTabOpen(int menuItemId) {
        return menuItemId == R.id.action_wakeupat;
    }

    @Override
    public void handleBackPress() {
        int milliseconds = 2000;

        if (isButtonAfterFirstPress) {
            view.moveAppToBack();
        } else {
            view.showToastWithDoubleBackMessage();
            isButtonAfterFirstPress = true;
            view.countDownInMilliseconds(milliseconds);
        }
    }

    @Override
    public void onCountedDown() {
        isButtonAfterFirstPress = false;
    }
}
