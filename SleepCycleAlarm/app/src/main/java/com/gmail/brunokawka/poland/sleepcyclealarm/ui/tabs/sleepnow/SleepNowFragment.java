package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.sleepnow;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SleepNowFragment extends Fragment {
    private static final String TAG = "SleepNowFragmentLog";

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  layoutInflater.inflate(R.layout.fragment_sleep_now, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
