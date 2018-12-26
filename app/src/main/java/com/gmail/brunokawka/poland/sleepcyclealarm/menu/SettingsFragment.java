package com.gmail.brunokawka.poland.sleepcyclealarm.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceGroupAdapter;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat  {

    public final static String TAG = "settings_fragment";

    private static boolean isFirstRun;


    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);

        isFirstRun = true;

        if (Objects.equals(rootKey, getString(R.string.pref_theme_category))) {
            bindPreferenceToListener(findPreference(getString(R.string.key_change_theme)));
        }else if (Objects.equals(rootKey, getString(R.string.pref_alarm_category))) {
            bindPreferenceToListener(findPreference(getString(R.string.key_ring_duration)));
        }

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
        Log.d(getClass().getName(), isFirstRun ? "Is first run" : "Its not a first run");
        return key.equals(getString(R.string.key_change_theme)) && !isFirstRun;
    }

    private void recreateAndShowToastIfActivityNotNull() {
        if (getActivity() != null) {
            Log.d(getClass().getName(), "Recreating...");

            showInformativeToast();
            getActivity().recreate();
        } else {
            Log.d(getClass().getName(), "Activity is null");
        }
    }

    private void showInformativeToast() {
        Toast.makeText(getActivity(), getString(R.string.toast_what_if_theme_didnt_apply), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the default white background in the view so as to avoid transparency
        Context context = getContext();
        if (context!=null)
        view.setBackgroundColor(
                ContextCompat.getColor(context, R.color.color_primary));


    }

    @Override
    protected RecyclerView.Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        return new PreferenceGroupAdapter(preferenceScreen) {
            @SuppressLint("RestrictedApi")
            @Override
            public void onBindViewHolder(PreferenceViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                Preference preference = getItem(position);
                if (preference instanceof PreferenceCategory)
                    setZeroPaddingToLayoutChildren(holder.itemView);
                else {
                    View iconFrame = holder.itemView.findViewById(R.id.icon_frame);
                    if (iconFrame != null) {
                        iconFrame.setVisibility(preference.getIcon() == null ? View.GONE : View.VISIBLE);
                    }
                }
            }
        };
    }

    private void setZeroPaddingToLayoutChildren(View view) {
        if (!(view instanceof ViewGroup))
            return;
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            setZeroPaddingToLayoutChildren(viewGroup.getChildAt(i));

                viewGroup.setPaddingRelative(0, viewGroup.getPaddingTop(), viewGroup.getPaddingEnd(), viewGroup.getPaddingBottom());

        }
    }


}