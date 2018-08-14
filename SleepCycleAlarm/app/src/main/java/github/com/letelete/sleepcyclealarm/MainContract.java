package github.com.letelete.sleepcyclealarm;

import android.app.Fragment;
import android.view.MenuItem;

public interface MainContract {

    interface MvpView {
        void navigateToSpecificFragmentWithAnimation(Fragment fragment, int[] enterExitAnimationPair);
        void openMenuActivityWithItemVariables(int itemId, String itemTitle);
    }

    interface Presenter {
        void handleBottomNavigationTabClick(int position, int previousPosition);
        void handleMenuItemClick(MenuItem item);
    }
}
