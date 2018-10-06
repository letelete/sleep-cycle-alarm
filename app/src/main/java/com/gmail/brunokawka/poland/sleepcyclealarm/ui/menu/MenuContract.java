package com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public interface MenuContract {

    interface MenuView {
        void setAppTheme(int themeId);
        void setActivityTitle(String title);
        void openPreferenceFragment();
        void openStandardFragment();
        void showErrorAndFinish(int resourceMsgReference);
        void closeActivity();
    }

    interface MenuPresenter {
        void initializeValueByKeysAndPassedIntent(String idKey, String titleKey, Intent intent);
        void handleSetTheme(String changeThemeKey, SharedPreferences preferences);
        void handleSetActivityTitle();
        void performActionDependingOnMenuItemIdKey(Bundle savedInstanceState);
        void handleCloseActivityButton();
    }
}
