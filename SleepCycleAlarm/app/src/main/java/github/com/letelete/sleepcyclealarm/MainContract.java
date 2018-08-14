package github.com.letelete.sleepcyclealarm;

import android.app.Fragment;

public interface MainContract {

    interface MvpView {
        void navigateToSpecificTab(Fragment tabFragment);
    }

    interface Presenter {
        void handleBottomNavigationTabClick(int position);
    }
}
