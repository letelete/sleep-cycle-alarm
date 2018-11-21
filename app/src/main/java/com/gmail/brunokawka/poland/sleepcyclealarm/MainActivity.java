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
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms.AlarmsFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.sleepnow.SleepNowFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ThemeCoordinator;

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

    @BindView(R.id.toolbar) protected Toolbar appToolbar;
    @BindView(R.id.bottom_navigation_bar) protected BottomNavigationView bottomNavigationBar;
    @BindView(R.id.wakeUpAtFloatingActionButtonExtended) protected Button wakeUpAtButton;

    private FragmentManager fragmentManager;
    private MainPresenter mainPresenter;
    private WakeUpAtSetHourButton wakeUpAtSetHourButton;
    private Bundle savedInstanceState;

    private SleepNowFragment sleepNowFragment;
    private WakeUpAtFragment wakeUpAtFragment;
    private AlarmsFragment alarmsFragment;

    @OnClick(R.id.wakeUpAtFloatingActionButtonExtended)
    public void onWakeUpAtFloatingActionButtonExtendedClicked() {
        EventBus.getDefault().post(new SetHourButtonClickedEvent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        setAppTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainPresenter = new MainPresenter(this);

        sleepNowFragment = new SleepNowFragment();
        wakeUpAtFragment = new WakeUpAtFragment();
        alarmsFragment = new AlarmsFragment();

        wakeUpAtSetHourButton = new WakeUpAtSetHourButton(wakeUpAtButton);
        fragmentManager = getSupportFragmentManager();
        mainPresenter.setUpUi(savedInstanceState);
        initRealm();
    }

    private void setAppTheme() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String key = getString(R.string.key_change_theme);
        int themeId = ThemeCoordinator.getCurrentTheme(pref.getString(key, "1"));
        getDelegate().setLocalNightMode(themeId);
    }

    private void initRealm() {
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
    public void setUpBottomNavigationBar() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void openLatestFragment() {
        final int latestFragmentId = savedInstanceState.getInt(getString(R.string.key_last_execution_date));
        bottomNavigationBar.setSelectedItemId(latestFragmentId);
    }

    @Override
    public void openDefaultFragment() {
        final int defaultFragmentId = R.id.action_sleepnow;
        bottomNavigationBar.setSelectedItemId(defaultFragmentId);
    }

    @Override
    public void setUpToolbar() {
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
    public void openSleepNowFragment() {
        replaceFragment(sleepNowFragment);
    }

    @Override
    public void openWakeUpAtFragment() {
        replaceFragment(wakeUpAtFragment);
    }

    @Override
    public void openAlarmsFragment() {
        replaceFragment(alarmsFragment);
    }

    public void replaceFragment(Fragment newFragment) {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.animator.fragment_open_enter, R.animator.fragment_open_exit)
                .replace(R.id.main_activity_container, newFragment)
                .commit();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        mainPresenter.handleMenuItemClick(itemId);
        return true;
    }

    @Override
    public void openSettingsActivity() {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra(getString(R.string.key_settings_tag), R.id.menu_settings);
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
    private void removeWakeUpAtPreferences() {
        SharedPreferences wakeUpAtPreferences = getSharedPreferences(getString(R.string.wakeupat_preferences_name), MODE_PRIVATE);
        wakeUpAtPreferences.edit().clear().commit();
    }
}
