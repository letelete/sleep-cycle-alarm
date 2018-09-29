package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.events.SetHourButtonClickedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu.MenuActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements
        MainContract.MainView,
        BottomNavigationView.OnNavigationItemSelectedListener,
        Toolbar.OnMenuItemClickListener {

    private final static String TAG = "MainActivityLog";

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private MainPresenter mainPresenter;

    private SharedPreferences sharedPreferences;

    @BindView(R.id.toolbar)
    Toolbar appToolbar;

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationView bottomNavigationBar;

    @BindView(R.id.wakeUpAtFloatingActionButtonExtended)
    Button wakeUpAtActionButton;

    @OnClick(R.id.wakeUpAtFloatingActionButtonExtended)
    public void onWakeUpAtFloatingActionButtonExtendedClicked() {
        EventBus.getDefault().post(new SetHourButtonClickedEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wakeUpAtActionButtonClickedEvent(SetHourButtonClickedEvent setHourButtonClickedEvent) {
        Log.d(TAG, "Event received");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mainPresenter = new MainPresenter(this);
        mainPresenter.handleSetTheme(getString(R.string.key_change_theme), sharedPreferences);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpBottomNavigationBar();
        openLatestFragmentOrDefault(savedInstanceState);

        setupToolbar();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
    public void showWakeUpAtActionButton() {
        animateWakeUpAtButton(View.VISIBLE, 200f, -150f);
    }

    @Override
    public void hideWakeUpAtActionButton() {
        animateWakeUpAtButton(View.GONE, -150f, 200f);
    }

    private void animateWakeUpAtButton(final int finalViewType, final float startPositionY, final float endPositionY) {
        if (wakeUpAtActionButton.getVisibility() != finalViewType) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(wakeUpAtActionButton, "translationY", startPositionY, endPositionY);
            animation.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (finalViewType != View.VISIBLE) {
                        wakeUpAtActionButton.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onAnimationStart(Animator animation) {
                    if (wakeUpAtActionButton.getVisibility() != View.VISIBLE) {
                        wakeUpAtActionButton.setVisibility(View.VISIBLE);
                    }
                }
            });
            animation.start();
        } else {
            Log.e(TAG, "at: animateWakeUpAtButton() - wake up at action button is already " + (finalViewType != View.VISIBLE ? "GONE" : "VISIBLE"));
        }
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        removeWakeUpAtPreferences();
        super.onDestroy();
    }

    @SuppressLint("ApplySharedPref")
    // It has to be .commit(); .apply() doesn't clear preferences on destroy
    private void removeWakeUpAtPreferences() {
        SharedPreferences wakeUpAtPreferences = getSharedPreferences(getString(R.string.wakeupat_preferences_name), MODE_PRIVATE);
        wakeUpAtPreferences.edit().clear().commit();
    }
}
