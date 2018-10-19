package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.SetHourButtonClickedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.listeners.OnRealmChangeListener;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.WakeUpAtSetHourButton;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu.MenuActivity;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements
        MainContract.MainView,
        BottomNavigationView.OnNavigationItemSelectedListener,
        Toolbar.OnMenuItemClickListener {

    private FragmentManager fragmentManager;
    private MainPresenter mainPresenter;
    private WakeUpAtSetHourButton wakeUpAtSetHourButton;
    private WakeUpAtFragment wakeUpAtFragment;

    @BindView(R.id.toolbar)
    protected Toolbar appToolbar;

    @BindView(R.id.bottom_navigation_bar)
    protected BottomNavigationView bottomNavigationBar;

    @BindView(R.id.wakeUpAtFloatingActionButtonExtended)
    protected Button wakeUpAtButton;

    @OnClick(R.id.wakeUpAtFloatingActionButtonExtended)
    public void onWakeUpAtFloatingActionButtonExtendedClicked() {
        EventBus.getDefault().post(new SetHourButtonClickedEvent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mainPresenter = new MainPresenter(this);
        mainPresenter.handleSetTheme(getString(R.string.key_change_theme), sharedPreferences);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        wakeUpAtFragment = new WakeUpAtFragment();
        wakeUpAtSetHourButton = new WakeUpAtSetHourButton(wakeUpAtButton);
        fragmentManager = getSupportFragmentManager();
        setUpBottomNavigationBar();
        openLatestFragmentOrDefault(savedInstanceState);
        setupToolbar();
        RealmManager.initializeRealmConfig();
        RealmManager.incrementCount();
        Realm realm = RealmManager.getRealm();
        realm.addChangeListener(new OnRealmChangeListener(getApplicationContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(wakeUpAtFragment);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.key_last_execution_date), bottomNavigationBar.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setAppTheme(int themeId) {
        getDelegate().setLocalNightMode(themeId);
    }

    private void setUpBottomNavigationBar() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(this);
    }

    private void openLatestFragmentOrDefault(Bundle savedInstanceState) {
        final int defaultPosition = R.id.action_sleepnow;
        final int bottomNavigationPosition = savedInstanceState != null
                ? savedInstanceState.getInt(getString(R.string.key_last_execution_date))
                : defaultPosition;
        bottomNavigationBar.setSelectedItemId(bottomNavigationPosition);
    }

    private void setupToolbar() {
        setSupportActionBar(appToolbar);
        tryToHideActionBarTitle();
        appToolbar.setOnMenuItemClickListener(this);
    }

    private void tryToHideActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        } else {
            Log.e(getClass().getName(), "actionBar at tryToHideActionBarTitle is null");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menuItemId = menuItem.getItemId();
        mainPresenter.handleBottomNavigationTabClick(menuItemId);
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
    public void showWakeUpAtActionButton() {
        wakeUpAtSetHourButton.show();
    }

    @Override
    public void hideWakeUpAtActionButton() {
        wakeUpAtSetHourButton.hide();
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
    public void countDownInMillisecondsAndEmitSignalBackAtTheEnd(int milliseconds) {
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

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(wakeUpAtFragment);
    }

    @Override
    public void onDestroy() {
        removeWakeUpAtPreferences();
        RealmManager.decrementCount();
        super.onDestroy();
    }

    @SuppressLint("ApplySharedPref")
    // It has to be .commit(); .apply() doesn't clear preferences on destroy
    private void removeWakeUpAtPreferences() {
        SharedPreferences wakeUpAtPreferences = getSharedPreferences(getString(R.string.wakeupat_preferences_name), MODE_PRIVATE);
        wakeUpAtPreferences.edit().clear().commit();
    }
}
