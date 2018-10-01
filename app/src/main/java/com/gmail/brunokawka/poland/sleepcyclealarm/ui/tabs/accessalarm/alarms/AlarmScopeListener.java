package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.support.v4.app.Fragment;

import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;

public class AlarmScopeListener extends Fragment {
    private AlarmsPresenter alarmsPresenter;

    public AlarmScopeListener() {
        setRetainInstance(true);
        RealmManager.incrementCount();
        alarmsPresenter = new AlarmsPresenter();
    }

    @Override
    public void onDestroy() {
        RealmManager.decrementCount();
        super.onDestroy();
    }

    public AlarmsPresenter getPresenter() {
        return alarmsPresenter;
    }
}
