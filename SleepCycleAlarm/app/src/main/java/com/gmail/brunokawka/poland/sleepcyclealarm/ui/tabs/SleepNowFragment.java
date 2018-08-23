package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.model.DataHelper;

import io.realm.Realm;

public class SleepNowFragment extends Fragment {
    private static final String TAG = "SleepNowFragmentLog";

    private Realm realm;
    private Button testAddAlarm;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        View view = layoutInflater.inflate(R.layout.fragment_sleep_now, container, false);
        testAddAlarm = view.findViewById(R.id.testAddAlarm);
        testAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataHelper.addItemAsync(realm);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}
