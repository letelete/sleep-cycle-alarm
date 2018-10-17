package com.gmail.brunokawka.poland.sleepcyclealarm.listeners;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gmail.brunokawka.poland.sleepcyclealarm.events.RealmChangeEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.schedule.AlarmController;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms.AlarmsFragment;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class OnRealmChangeListener implements RealmChangeListener<Realm> {

    private AlarmsFragment alarmsFragment;
    private AlarmController alarmController;

    public OnRealmChangeListener(Context context) {
        initEventBus();
        alarmsFragment = new AlarmsFragment();
        alarmController = new AlarmController(context);
    }

    @Override
    public void onChange(@NonNull Realm realm) {
        updateAlarms();
        EventBus.getDefault().post(new RealmChangeEvent());
    }

    private void initEventBus() {
        EventBus.getDefault().register(alarmsFragment);
    }

    private void updateAlarms() {
        alarmController.rescheduleAlarms();
    }
}
