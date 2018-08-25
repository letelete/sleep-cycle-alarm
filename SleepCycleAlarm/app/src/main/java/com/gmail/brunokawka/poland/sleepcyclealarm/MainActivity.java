package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import com.gmail.brunokawka.poland.sleepcyclealarm.alarms.AlarmsPresenter;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu.MenuActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements
        MainContract.MainView,
        BottomNavigationBar.OnTabSelectedListener,
        Toolbar.OnMenuItemClickListener {

    private final static String TAG = "MainActivityLog";

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    int previousTabPosition; // created for transition direction purposes
    private MainPresenter mainPresenter;

    @BindView(R.id.toolbar)
    Toolbar appToolbar;

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainPresenter = new MainPresenter(this);
        mainPresenter.handleSetTheme(getString(R.string.key_change_theme),
                PreferenceManager.getDefaultSharedPreferences(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();
        setupBottomNavigationBar();
    }

    @Override
    public void setAppTheme(int themeId) {
        getDelegate().setLocalNightMode(themeId);
    }

    private void setupToolbar() {
        setSupportActionBar(appToolbar);
        appToolbar.setOnMenuItemClickListener(this);
    }

    private void setupBottomNavigationBar() {
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
        mainPresenter.handleBottomNavigationTabClick(position, previousTabPosition);
        previousTabPosition = position;
    }

    @Override
    public void navigateToSpecificFragmentWithAnimation(Fragment newFragment, int[] enterExitAnimationPair) {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        ft.setCustomAnimations(enterExitAnimationPair[0], enterExitAnimationPair[1])
                .replace(R.id.main_activity_container, newFragment)
                .commit();
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        mainPresenter.handleMenuItemClick(item);
        return true;
    }

    @Override
    public void openMenuActivityWithItemVariables(int itemId, String itemTitle) {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra(getString(R.string.key_menu_item_id), itemId)
                .putExtra(getString(R.string.key_menu_item_title), itemTitle);
        startActivity(intent);
    }
}
