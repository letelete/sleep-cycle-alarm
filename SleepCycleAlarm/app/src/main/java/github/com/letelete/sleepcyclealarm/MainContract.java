package github.com.letelete.sleepcyclealarm;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.view.MenuItem;

public interface MainContract {

    interface MvpView {
        void setAppTheme(int themeId);
        void navigateToSpecificFragmentWithAnimation(Fragment fragment, int[] enterExitAnimationPair);
        void openMenuActivityWithItemVariables(int itemId, String itemTitle);
    }

    interface Presenter {
        void handleSetTheme(String changeThemeKey, SharedPreferences preferences);
        void handleBottomNavigationTabClick(int position, int previousPosition);
        void handleMenuItemClick(MenuItem item);
    }
}
