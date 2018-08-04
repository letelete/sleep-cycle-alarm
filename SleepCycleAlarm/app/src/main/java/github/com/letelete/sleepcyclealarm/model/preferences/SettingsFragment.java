package github.com.letelete.sleepcyclealarm.model.preferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import github.com.letelete.sleepcyclealarm.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);

        bindPreferenceSummaryToValue(findPreference("language_select"));
        bindPreferenceSummaryToValue(findPreference("ring_duration"));
        bindPreferenceSummaryToValue(findPreference("alarms_intervals"));
        bindPreferenceSummaryToValue(findPreference("auto_silence"));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

            } else {
                preference.setSummary(stringValue);

            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
}
