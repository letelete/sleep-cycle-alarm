package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms.AlarmsFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.sleepnow.SleepNowFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ThemeCoordinator;


public class MainPresenter implements MainContract.MainPresenter {

    private MainContract.MainView view;
    private boolean isAfterFirstPress;

    public MainPresenter(MainContract.MainView view) {
        this.view = view;
    }

    @Override
    public void setUpUi(Bundle savedInstanceState) {
        view.setUpBottomNavigationBar();
        view.openLatestFragmentOrDefault(savedInstanceState);
        view.setUpToolbar();
    }

    @Override
    public void handleSetTheme(String changeThemeKey, SharedPreferences sharedPreferences) {
        int themeId = ThemeCoordinator.getCurrentTheme(sharedPreferences.getString(changeThemeKey, "1"));
        view.setAppTheme(themeId);
    }

    @Override
    public void handleBottomNavigationTabClick(int menuItemId) {
        Fragment fragment;

        switch (menuItemId) {
            case R.id.action_wakeupat:
                fragment = new WakeUpAtFragment();
                break;
            case R.id.action_alarms:
                fragment = new AlarmsFragment();
                break;
            case R.id.action_sleepnow:
            default:
                fragment = new SleepNowFragment();
                break;
        }
        handleWakeUpAtButtonVisibility(menuItemId);
        view.replaceFragment(fragment);
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
    public void handleMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        String itemTitle = item.getTitle().toString();
        view.openMenuActivityWithItemVariables(itemId, itemTitle);
    }

    @Override
    public void handleBackPress() {
        int milliseconds = 2000;

        if (isAfterFirstPress) {
            view.moveAppToBack();
        } else {
            view.showToastWithDoubleBackMessage();
            isAfterFirstPress = true;
            view.countDownInMillisecondsAndEmitSignalBackAtTheEnd(milliseconds);
        }
    }

    @Override
    public void onCountedDown() {
        isAfterFirstPress = false;
    }
}
