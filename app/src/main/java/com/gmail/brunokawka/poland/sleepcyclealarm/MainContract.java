package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

public interface MainContract {

    interface MainView {
        void setAppTheme(int themeId);
        void replaceFragment(Fragment fragment);
        void showWakeUpAtActionButton();
        void hideWakeUpAtActionButton();
        void openMenuActivityWithItemVariables(int itemId, String itemTitle);
        void showToastWithDoubleBackMessage();
        void countDownInMillisecondsAndEmitSignalBackAtTheEnd(int seconds);
        void moveAppToBack();
    }

    interface MainPresenter {
        void handleSetTheme(String changeThemeKey, SharedPreferences preferences);
        void handleBottomNavigationTabClick(int menuItemId);
        void handleMenuItemClick(MenuItem item);
        void handleBackPress();
        void onCountedDown();
    }
}
