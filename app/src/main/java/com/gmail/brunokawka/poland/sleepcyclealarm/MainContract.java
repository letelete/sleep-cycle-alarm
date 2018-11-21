package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

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
