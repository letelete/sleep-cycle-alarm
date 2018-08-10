package github.com.letelete.sleepcyclealarm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import github.com.letelete.sleepcyclealarm.ui.menu.MenuActivity;
import github.com.letelete.sleepcyclealarm.ui.tabs.AlarmsFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.SleepNowFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.WakeUpAtFragment;
import github.com.letelete.sleepcyclealarm.utils.ThemeHelper;

public class MainActivity extends AppCompatActivity
    implements BottomNavigationBar.OnTabSelectedListener,
        Toolbar.OnMenuItemClickListener {

    private final static String TAG = "MainActivityLog";

    private String menuItemIdKey;
    private String menuItemTitleKey;

    private BottomNavigationBar bottomNavigationBar;
    private final FragmentManager fragmentManager = getFragmentManager();
    private Fragment currentFragment = null;

    private SharedPreferences sharedPreferences;
    private ThemeHelper themeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.themeHelper = new ThemeHelper(sharedPreferences);

        setAppTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuItemIdKey = getString(R.string.key_menu_item_id);
        menuItemTitleKey = getString(R.string.key_menu_item_title);

        setupToolbar();
        setupBottomNavigationBar();
    }

    private void setAppTheme() {
        AppCompatDelegate.setDefaultNightMode(themeHelper.getCurrentTheme(getString(R.string.key_change_theme)));
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
        Log.i(TAG, "tab selected, index: " + String.valueOf(position));
        switch(position) {
            case 0:
                setSleepNowFragment();
                break;
            case 1:
                setWakeUpAtFragment();
                break;
            case 2:
                setAlarmsFragment();
                break;
            default:
                Log.wtf(TAG, "Default tab selected. Position value: " + String.valueOf(position));
                break;
        }
        if(this.currentFragment != null) {
            FragmentTransaction ft = this.fragmentManager.beginTransaction();
            ft.replace(R.id.main_activity_container, this.currentFragment);
            ft.commit();
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void setSleepNowFragment() {
        this.currentFragment = new SleepNowFragment();
    }

    private void setWakeUpAtFragment() {
        this.currentFragment = new WakeUpAtFragment();
    }

    private void setAlarmsFragment() {
        this.currentFragment = new AlarmsFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.three_dot_menu, menu);
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

