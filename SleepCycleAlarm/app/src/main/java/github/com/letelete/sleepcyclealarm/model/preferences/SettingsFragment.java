package github.com.letelete.sleepcyclealarm.model.preferences;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import github.com.letelete.sleepcyclealarm.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);
    }
}
