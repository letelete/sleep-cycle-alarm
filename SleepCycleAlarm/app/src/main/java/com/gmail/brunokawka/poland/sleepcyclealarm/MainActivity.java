package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu.MenuActivity;

import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements
        MainContract.MainView,
        BottomNavigationView.OnNavigationItemSelectedListener,
        Toolbar.OnMenuItemClickListener {

    private final static String TAG = "MainActivityLog";

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private MainPresenter mainPresenter;

    @BindView(R.id.toolbar)
    Toolbar appToolbar;

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainPresenter = new MainPresenter(this);
        mainPresenter.handleSetTheme(getString(R.string.key_change_theme),
                PreferenceManager.getDefaultSharedPreferences(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpBottomNavigationBar();
        setupToolbar();
    }

    @Override
    public void setAppTheme(int themeId) {
        getDelegate().setLocalNightMode(themeId);
    }

    private void setUpBottomNavigationBar() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(this);
        bottomNavigationBar.setSelectedItemId(R.id.action_sleepnow);
    }

    private void setupToolbar() {
        setSupportActionBar(appToolbar);
        appToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mainPresenter.handleBottomNavigationTabClick(menuItem);
        return true;
    }

    @Override
    public void replaceFragment(Fragment newFragment) {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.fragment_open_enter, R.animator.fragment_open_exit)
                .replace(R.id.main_activity_container, newFragment)
                .commit();
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

    @Override
    public void onBackPressed() {
        mainPresenter.handleBackPress();
    }

    @Override
    public void showToastWithDoubleBackMessage() {
        Toast.makeText(getApplicationContext(), getString(R.string.toast_double_back_to_exit), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void countDownInMilliseconds(int milliseconds) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainPresenter.onCountedDown();
            }
        }, milliseconds);
    }

    @Override
    public void moveAppToBack() {
        moveTaskToBack(true);
    }
}
