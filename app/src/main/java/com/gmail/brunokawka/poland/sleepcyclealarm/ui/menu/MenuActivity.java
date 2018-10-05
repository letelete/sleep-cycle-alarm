package com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;

import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity
    implements MenuContract.MenuView,
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    private MenuPresenter menuPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        menuPresenter = new MenuPresenter(this);
        menuPresenter.handleSetTheme(getString(R.string.key_change_theme),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        settingToolbar();
        menuPresenter.initializeValueByKeysAndPassedIntent(getString(R.string.key_menu_item_id),
                getString(R.string.key_menu_item_title),
                getIntent());

        menuPresenter.handleSetActivityTitle();

        menuPresenter.performActionDependingOnMenuItemIdKey(savedInstanceState);
    }

    private void settingToolbar() {
        final Toolbar toolbar = findViewById(R.id.activity_menu_toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    public void handleScreenChange(String key){
        setActivityTitle(key);
        //2 conditions to handle the situation when a fragment is not yet created and recreated on theme change. TODO:make it better
        if (key.equals(getString(R.string.pref_theme_category)) || key.equals(getString(R.string.pref_alarm_category))){
            setToolbarBackButtonIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        }else{
            setToolbarBackButtonIcon(R.drawable.button_close_activity_drawable);
        }
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
            handleScreenChange(((PreferenceFragmentCompat)frag).getPreferenceScreen().getKey());
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
    public void findPreferenceFragment() {

    }


    @Override
    public void openNewPreferenceFragment() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SettingsFragment.TAG);
        if (fragment == null) {
            fragment = new SettingsFragment();
        }


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_menu_container, fragment, SettingsFragment.TAG);
        ft.commit();
    }

    @Override
    public void findStandardFragment() {
        //TODO:
    }

    @Override
    public void openNewStandardFragment() {
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
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat,
            PreferenceScreen preferenceScreen) {
        handleScreenChange(preferenceScreen.getKey());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);
        ft.add(R.id.activity_menu_container, fragment, preferenceScreen.getKey());
        ft.addToBackStack(preferenceScreen.getKey());
        ft.commit();
        return true;
    }
}