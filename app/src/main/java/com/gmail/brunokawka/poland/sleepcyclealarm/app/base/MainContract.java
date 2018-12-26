package com.gmail.brunokawka.poland.sleepcyclealarm.app.base;

import android.os.Bundle;

public interface MainContract {

    interface MainView {
        void setUpBottomNavigationBar();
        void openLatestFragment();
        void openDefaultFragment();
        void setUpToolbar();
        void openSleepNowFragment();
        void openWakeUpAtFragment();
        void openAlarmsFragment();
        void showWakeUpAtActionButton();
        void hideWakeUpAtActionButton();
        void openSettingsActivity();
        void showToastWithDoubleBackMessage();
        void countDownInMilliseconds(int seconds);
        void moveAppToBack();
    }

    interface MainPresenter {
        void setUpUi(Bundle savedInstanceState);
        void handleBottomNavigationTabClick(int menuItemId);
        void handleBackPress();
        void handleMenuItemClick(int menuItemId);
        void onCountedDown();
    }
}
