package github.com.letelete.sleepcyclealarm.ui.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import github.com.letelete.sleepcyclealarm.R;
import github.com.letelete.sleepcyclealarm.model.preferences.SettingsFragment;
import github.com.letelete.sleepcyclealarm.utils.ThemeHelper;

public class MenuPresenter implements MenuContract.MenuPresenter {
    private static final String TAG = "MenuPresenterLog";

    private MenuContract.MenuView view;
    private Intent passedIntent;

    private static final int WRONG_KEY_ERROR_CODE = -1;
    private int menuItemIdValue;
    private String menuItemTitleValue;

    public MenuPresenter(MenuContract.MenuView menuView) {
        this.view = menuView;
    }

    @Override
    public void initializeValueByKeysAndPassedIntent(String idKey, String titleKey, Intent intent) {
        this.passedIntent = intent;
        this.menuItemIdValue = passedIntent.getIntExtra(idKey, R.id.menu_settings);
        this.menuItemTitleValue = TextUtils.isEmpty(titleKey)
                ? "key_settings_tag"
                : passedIntent.getStringExtra(titleKey);
    }

    @Override
    public void handleSetTheme(String changeThemeKey, SharedPreferences preferences) {
        ThemeHelper themeHelper = new ThemeHelper(preferences);
        int themeId = themeHelper.getCurrentTheme(changeThemeKey);
        view.setAppTheme(themeId);
    }

    @Override
    public void handleSetActivityTitle() {
        if(!TextUtils.isEmpty(menuItemTitleValue)) {
            view.setActivityTitle(menuItemTitleValue);
        } else {
            view.setActivityTitle("Activity");
            Log.e(TAG,"ActivityTitle is empty or null");
        }
    }

    @Override
    public void performActionDependingOnMenuItemIdKey(Bundle savedInstanceState) {
        switch (menuItemIdValue) {
            case WRONG_KEY_ERROR_CODE:
                Log.e(TAG, "Default value assigned to the key");
                view.showErrorAndFinish(R.string.error_menu_activity_key_use_default_value);

            case R.id.menu_settings:
                if(savedInstanceState != null)
                    view.findPreferenceFragment();
                else
                    view.openNewPreferenceFragment();
                break;

            default:
                Log.e(TAG, "Default case in switch terminated. Key value: " + menuItemIdValue);
                view.showErrorAndFinish(R.string.error_menu_activity_default_case_in_switch);
        }
    }

    @Override
    public void handleCloseActivityButton() {
        Log.i(TAG, "User close an activity");
        view.closeActivity();
    }
}
