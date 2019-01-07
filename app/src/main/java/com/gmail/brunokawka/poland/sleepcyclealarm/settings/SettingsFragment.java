package com.gmail.brunokawka.poland.sleepcyclealarm.settings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.view.View;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String TAG = "SETTINGS_FRAGMENT_TAG";
    private boolean isConfigurationApplied;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        bindPreferenceToListener(getPreferenceById(R.string.key_change_theme));
        bindPreferenceToListener(getPreferenceById(R.string.key_ring_duration));
        isConfigurationApplied = true;
    }

    private Preference getPreferenceById(int id) {
        return findPreference(getString(id));
    }

    private void bindPreferenceToListener(Preference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);
        preferenceChangeListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference
            .OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            if (preference.getTitle().equals(getString(R.string.pref_change_theme_title))
                    && isConfigurationApplied) {
                applyNewTheme();
            }

            return true;
        }
    };

    private void applyNewTheme() {
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getContext() != null) {
            view.setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.color_primary));
        }

    }
}
