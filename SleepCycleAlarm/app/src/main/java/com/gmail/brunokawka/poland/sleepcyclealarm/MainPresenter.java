package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;

import com.gmail.brunokawka.poland.sleepcyclealarm.alarms.AlarmsFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.SleepNowFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.WakeUpAtFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ThemeHelper;

public class MainPresenter implements MainContract.MainPresenter {
    private final static String TAG = "MainPresenterLog";

    private MainContract.MainView view;

    MainPresenter(MainContract.MainView view) {
        this.view = view;
    }

    @Override
    public void handleSetTheme(String changeThemeKey, SharedPreferences sharedPreferences) {
        int themeId = ThemeHelper.getCurrentTheme(changeThemeKey, sharedPreferences);
        view.setAppTheme(themeId);
    }

    @Override
    public void handleBottomNavigationTabClick(int position, int previousPosition) {
        Log.i(TAG, "tab selected, index: " + String.valueOf(position));

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new SleepNowFragment();
                break;
            case 1:
                fragment = new WakeUpAtFragment();
                break;
            case 2:
                fragment = new AlarmsFragment();
                break;
            default:
                fragment = new SleepNowFragment();
                Log.e(TAG, "Default tab selected. Position value: " + String.valueOf(position));
                break;
        }

        int[] animationPair = getAnimationPair(position, previousPosition);
        view.navigateToSpecificFragmentWithAnimation(fragment, animationPair);
    }

    private int[] getAnimationPair(int position, int previousPosition) {
        int [] swipeLeftPair = {R.animator.swipe_left_enter, R.animator.swipe_left_exit};
        int [] swipeRightPair = {R.animator.swipe_right_enter, R.animator.swipe_right_exit};
        return position >= previousPosition
                ? swipeLeftPair
                : swipeRightPair;
    }

    @Override
    public void handleMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        String itemTitle = item.getTitle().toString();
        view.openMenuActivityWithItemVariables(itemId, itemTitle);
    }

}
