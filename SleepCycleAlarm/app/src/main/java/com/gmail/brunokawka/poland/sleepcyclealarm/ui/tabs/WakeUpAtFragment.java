package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;

public class WakeUpAtFragment extends Fragment {
    private static final String TAG = "WakeUpAtFragmentLog";

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragment_wake_up_at, container, false);
    }
}
