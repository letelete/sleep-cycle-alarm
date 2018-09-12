package com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";
    private static boolean isFirstRun;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);

        isFirstRun = true;

        bindPreferenceToListener(findPreference(getString(R.string.key_change_theme)));
        bindPreferenceToListener(findPreference(getString(R.string.key_ring_duration)));
        bindPreferenceToListener(findPreference(getString(R.string.key_alarms_intervals)));
        bindPreferenceToListener(findPreference(getString(R.string.key_auto_silence)));

        isFirstRun = false;
    }

    private void bindPreferenceToListener(Preference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);

        preferenceChangeListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                if(isRecreateRequired(preference)) {
                    recreateAndShowToastIfActivityNotNull();
                }

            } else {
                preference.setSummary(stringValue);
            }

            return true;
        }
    };

    private boolean isRecreateRequired(Preference preference) {
        String key = preference.getKey();
        Log.d(TAG, isFirstRun ? "Is first run" : "Its not a first run");
        return key.equals(getString(R.string.key_change_theme)) && !isFirstRun;
    }

    private void recreateAndShowToastIfActivityNotNull() {
        if (getActivity() != null) {
            Log.d(TAG, "Recreating...");

            showInformativeToast();
            getActivity().recreate();
        } else {
            Log.d(TAG, "Activity is null");
        }
    }

    private void showInformativeToast() {
        Toast.makeText(getActivity(), getString(R.string.toast_what_if_theme_didnt_apply), Toast.LENGTH_SHORT).show();
    }
}