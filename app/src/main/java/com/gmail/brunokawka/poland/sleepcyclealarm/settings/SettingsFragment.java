package com.gmail.brunokawka.poland.sleepcyclealarm.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceGroupAdapter;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
                    ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }

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
                        iconFrame.setVisibility(preference.getIcon() == null
                                ? View.GONE
                                : View.VISIBLE);
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

            viewGroup.setPaddingRelative(0,
                    viewGroup.getPaddingTop(),
                    viewGroup.getPaddingEnd(),
                    viewGroup.getPaddingBottom());
        }
    }
}
