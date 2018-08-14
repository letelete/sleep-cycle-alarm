package github.com.letelete.sleepcyclealarm;

import android.util.Log;

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
                view.navigateToSleepNowTab();
                break;
            case 1:
                view.navigateToWakeUpAtTab();
                break;
            case 2:
                view.navigateToAlarmsTab();
                break;
            default:
                view.navigateToSleepNowTab();
                Log.e(TAG, "Default tab selected. Position value: " + String.valueOf(position));
                break;
        }
    }
}
