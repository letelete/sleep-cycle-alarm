package github.com.letelete.sleepcyclealarm;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;

import github.com.letelete.sleepcyclealarm.ui.tabs.AlarmsFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.SleepNowFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.WakeUpAtFragment;
import github.com.letelete.sleepcyclealarm.utils.ThemeHelper;

public class MainPresenter implements MainContract.Presenter {
    private final static String TAG = "MainPresenterLog";

    private MainContract.MvpView view;

    MainPresenter(MainContract.MvpView view) {
        this.view = view;
    }

    @Override
    public void handleSetTheme(String changeThemeKey, SharedPreferences preferences) {
        ThemeHelper themeHelper = new ThemeHelper(preferences);
        int themeId = themeHelper.getCurrentTheme(changeThemeKey);
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
