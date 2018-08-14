package github.com.letelete.sleepcyclealarm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.Arrays;

import github.com.letelete.sleepcyclealarm.ui.menu.MenuActivity;
import github.com.letelete.sleepcyclealarm.utils.ThemeHelper;

public class MainActivity extends AppCompatActivity
    implements
        MainContract.MvpView,
        BottomNavigationBar.OnTabSelectedListener,
        Toolbar.OnMenuItemClickListener {

    private final static String TAG = "MainActivityLog";

    private String menuItemIdKey;
    private String menuItemTitleKey;

    private BottomNavigationBar bottomNavigationBar;
    private int previousTabPosition;

    private final FragmentManager fragmentManager = getFragmentManager();

    private SharedPreferences sharedPreferences;
    private ThemeHelper themeHelper;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.themeHelper = new ThemeHelper(sharedPreferences);
        this.mainPresenter = new MainPresenter(this);

        setAppTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuItemIdKey = getString(R.string.key_menu_item_id);
        menuItemTitleKey = getString(R.string.key_menu_item_title);
        previousTabPosition = 0;

        setupToolbar();
        setupBottomNavigationBar();
    }

    public void setAppTheme() {
        int themeId = themeHelper.getCurrentTheme(getString(R.string.key_change_theme));
        getDelegate().setLocalNightMode(themeId);
    }

    private void setupToolbar() {
        Toolbar appToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);
        appToolbar.setOnMenuItemClickListener(this);
    }

    private void setupBottomNavigationBar() {
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home, getString(R.string.sleep_now_tab)))
                .addItem(new BottomNavigationItem(R.drawable.ic_watch, getString(R.string.wake_up_at_tab)))
                .addItem(new BottomNavigationItem(R.drawable.ic_access_alarm, getString(R.string.alarms_tab)))
                .setBarBackgroundColor(R.color.color_primary)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.selectTab(0);
    }

    @Override
    public void onTabSelected(int position) {
        mainPresenter.handleBottomNavigationTabClick(position);
    }

    @Override
    public void navigateToSpecificTab(Fragment tabFragment) {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        ft.replace(R.id.main_activity_container, tabFragment)
                .commit();
    }

    @Override
    public void onTabUnselected(int position) { }

    @Override
    public void onTabReselected(int position) { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        String itemTittle = item.getTitle().toString();

        openMenuActivityWithArguments(id, itemTittle);
        return true;
    }

    private void openMenuActivityWithArguments(int itemId, String itemTitle) {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra(menuItemIdKey, itemId);
        intent.putExtra(menuItemTitleKey, itemTitle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Activity destroyed");
    }

}

