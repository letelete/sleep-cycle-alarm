package com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity
    implements MenuContract.MenuView,
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    private MenuPresenter menuPresenter;

    @BindView(R.id.activityTitleTextView)
    protected TextView activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        menuPresenter = new MenuPresenter(this);
        menuPresenter.handleSetTheme(getString(R.string.key_change_theme),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        menuPresenter.initializeValueByKeysAndPassedIntent(getString(R.string.key_menu_item_id),
                getString(R.string.key_menu_item_title),
                getIntent());

        menuPresenter.handleSetActivityTitle();

        menuPresenter.performActionDependingOnMenuItemIdKey(savedInstanceState);
    }

    public void onCloseActivityButtonClick(View view) {
        menuPresenter.handleCloseActivityButton();
    }

    @Override
    public void setAppTheme(int themeId) {
        getDelegate().setLocalNightMode(themeId);
    }

    @Override
    public void setActivityTitle(String title) {
        activityTitle.setText(title);
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
        ft.replace(R.id.menu_activity_container, fragment, SettingsFragment.TAG);
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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);
        ft.add(R.id.menu_activity_container, fragment, preferenceScreen.getKey());
        ft.addToBackStack(preferenceScreen.getKey());
        ft.commit();
        return true;
    }
}