package github.com.letelete.sleepcyclealarm;

import android.app.Fragment;
import android.util.Log;

import github.com.letelete.sleepcyclealarm.ui.tabs.AlarmsFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.SleepNowFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.WakeUpAtFragment;

public class MainPresenter implements MainContract.Presenter {
    private final static String TAG = "MainPresenterLog";

    private MainContract.MvpView view;

    MainPresenter(MainContract.MvpView view) {
        this.view = view;
    }

    @Override
    public void handleBottomNavigationTabClick(int position) {
        Log.i(TAG, "tab selected, index: " + String.valueOf(position));
        switch (position) {
            case 0:
                view.navigateToSpecificTab(new SleepNowFragment());
                break;
            case 1:
                view.navigateToSpecificTab(new WakeUpAtFragment());
                break;
            case 2:
                view.navigateToSpecificTab(new AlarmsFragment());
                break;
            default:
                view.navigateToSpecificTab(new SleepNowFragment());
                Log.e(TAG, "Default tab selected. Position value: " + String.valueOf(position));
                break;
        }
    }
}
