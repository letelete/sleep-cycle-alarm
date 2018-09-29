package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms.AlarmsFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.sleepnow.SleepNowFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ThemeCoordinator;


public class MainPresenter implements MainContract.MainPresenter {
    private final static String TAG = "MainPresenterLog";

    private MainContract.MainView view;
    private boolean isAfterFirstPress;

    public MainPresenter(MainContract.MainView view) {
        this.view = view;
    }

    @Override
    public void handleSetTheme(String changeThemeKey, SharedPreferences sharedPreferences) {
        int themeId = ThemeCoordinator.getCurrentTheme(changeThemeKey, sharedPreferences);
        view.setAppTheme(themeId);
    }

    @Override
    public void handleBottomNavigationTabClick(MenuItem menuItem) {
        Fragment fragment = null;
        int menuId = menuItem.getItemId();

        switch (menuId) {
            case R.id.action_wakeupat:
                fragment = new WakeUpAtFragment();
                view.showWakeUpAtActionButton();
                break;
            case R.id.action_alarms:
                fragment = new AlarmsFragment();
                view.hideWakeUpAtActionButton();
                break;
            case R.id.action_sleepnow:
            default:
                fragment = new SleepNowFragment();
                view.hideWakeUpAtActionButton();
                break;
        }

        view.replaceFragment(fragment);
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
