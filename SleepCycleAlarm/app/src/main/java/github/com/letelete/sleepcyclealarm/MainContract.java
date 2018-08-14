package github.com.letelete.sleepcyclealarm;

public interface MainContract {

    interface MvpView {
        void navigateToSleepNowTab();
        void navigateToWakeUpAtTab();
        void navigateToAlarmsTab();
    }

    interface Presenter {
        void handleBottomNavigationTabClick(int position);
    }
}
