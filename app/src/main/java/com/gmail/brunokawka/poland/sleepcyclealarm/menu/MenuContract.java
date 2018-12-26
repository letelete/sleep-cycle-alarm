package com.gmail.brunokawka.poland.sleepcyclealarm.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public interface MenuContract {

    interface MenuView {
        void setUpToolbar();
        void setAppTheme(int themeId);
        void setActivityTitle(String title);
        void openPreferenceFragment();
        void openStandardFragment();
        void showErrorAndFinish(int resourceMsgReference);
        void closeActivity();
        void setToolbarBackIcon();
        void setToolbarCloseIcon();
        boolean isPreferenceKeyEqualsToOneOfCategory(String preferenceKey);
    }

    interface MenuPresenter {
        void setUpActivity(String idKey, String titleKey, Intent intent);
        void initializeValueByKeysAndPassedIntent(String idKey, String titleKey, Intent intent);
        void handleSetTheme(String changeThemeKey, SharedPreferences preferences);
        void handleSetActivityTitle();
        void performActionDependingOnMenuItemIdKey(Bundle savedInstanceState);
        void handleCloseActivityButton();
        void handlePreferenceScreenChange(String key);
    }
}
