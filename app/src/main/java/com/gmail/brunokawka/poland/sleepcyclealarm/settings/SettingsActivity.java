package com.gmail.brunokawka.poland.sleepcyclealarm.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.Const;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {


    @BindView(R.id.tb_settings)
    Toolbar tbSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setAppTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setupToolbar();

        if (savedInstanceState == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(SettingsFragment.TAG);
            if (fragment == null) {
                fragment = new SettingsFragment();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_settings_container, fragment, SettingsFragment.TAG);
            ft.commit();
        }
    }

    private void setAppTheme() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String key = getString(R.string.key_change_theme);
        int themeId = ThemeUtils.getCurrentTheme(pref.getString(key, Const.DEFAULTS.THEME_ID));
        getDelegate().setLocalNightMode(themeId);
    }

    private void setupToolbar() {
        setSupportActionBar(tbSettings);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_close);
            ab.setTitle(getString(R.string.menu_settings));
            ab.setDisplayShowTitleEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
