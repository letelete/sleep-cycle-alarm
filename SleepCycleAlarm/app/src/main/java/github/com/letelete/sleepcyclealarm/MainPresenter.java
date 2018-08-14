package github.com.letelete.sleepcyclealarm;

import android.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import github.com.letelete.sleepcyclealarm.ui.tabs.AlarmsFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.SleepNowFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.WakeUpAtFragment;

public class MainPresenter implements MainContract.Presenter {
    private final static String TAG = "MainPresenterLog";

    private MainContract.MvpView view;
    private Fragment fragment = null;

    MainPresenter(MainContract.MvpView view) {
        this.view = view;
    }

    @Override
    public void handleBottomNavigationTabClick(int position, int previousPosition) {
        Log.i(TAG, "tab selected, index: " + String.valueOf(position));

        switch (position) {
            case 0:
                this.fragment = new SleepNowFragment();
                break;
            case 1:
                this.fragment = new WakeUpAtFragment();
                break;
            case 2:
                this.fragment = new AlarmsFragment();
                break;
            default:
                this.fragment = new SleepNowFragment();
                Log.e(TAG, "Default tab selected. Position value: " + String.valueOf(position));
                break;
        }

        if(fragment != null) {
            int[] animationPair = getAnimationPair(position, previousPosition);
            view.navigateToSpecificFragmentWithAnimation(fragment, animationPair);
        }
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
