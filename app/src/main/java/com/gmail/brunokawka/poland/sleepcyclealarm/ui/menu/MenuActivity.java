package com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity
    implements MenuContract.MenuView,
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    @BindView(R.id.activity_menu_toolbar) protected Toolbar toolbar;

    private MenuPresenter menuPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        menuPresenter = new MenuPresenter(this);
        menuPresenter.handleSetTheme(getString(R.string.key_change_theme),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        menuPresenter.setUpActivity(getString(R.string.key_menu_item_id), getString(R.string.key_menu_item_title), getIntent());
        menuPresenter.performActionDependingOnMenuItemIdKey(savedInstanceState);
    }

    @Override
    public void setUpToolbar() {
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen) {
        String preferenceKey = preferenceScreen.getKey();
        menuPresenter.handlePreferenceScreenChange(preferenceKey);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT,preferenceKey);
        fragment.setArguments(args);
        ft.add(R.id.activity_menu_container, fragment, preferenceKey);
        ft.addToBackStack(preferenceKey);
        ft.commit();
        return true;
    }

    private void setToolbarBackButtonIcon(int button_close_activity_drawable) {
        if (getSupportActionBar()!=null)
        this.getSupportActionBar().setHomeAsUpIndicator(button_close_activity_drawable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handleIconAndTitleDependsOnActiveFragment();
    }

    private void handleIconAndTitleDependsOnActiveFragment() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.activity_menu_container);
        if(frag instanceof PreferenceFragmentCompat) {
            Log.d(getClass().getName(), "Fragment is instanceof PreferenceFragmentCompat");
            menuPresenter.handlePreferenceScreenChange(((PreferenceFragmentCompat)frag).getPreferenceScreen().getKey());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setAppTheme(int themeId) {
        getDelegate().setLocalNightMode(themeId);
    }

    @Override
    public void setActivityTitle(String title) {
        if (getSupportActionBar()!=null)
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void openPreferenceFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SettingsFragment.TAG);
        if (fragment == null) {
            fragment = new SettingsFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_menu_container, fragment, SettingsFragment.TAG);
        ft.commit();
    }

    @Override
    public void openStandardFragment() {
        //TODO:
    }

    @Override
    public void showErrorAndFinish(int resourceMsgReference) {
        String errorMsg = getResources().getString(resourceMsgReference);
        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void setToolbarBackIcon() {
        setToolbarBackButtonIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
    }

    @Override
    public void setToolbarCloseIcon() {
        setToolbarBackButtonIcon(R.drawable.button_close_activity_drawable);
    }

    @Override
    public boolean isPreferenceKeyEqualsToOneOfCategory(String preferenceKey) {
        return preferenceKey.equals(getString(R.string.pref_theme_category))
                || preferenceKey.equals(getString(R.string.pref_alarm_category));
    }
}