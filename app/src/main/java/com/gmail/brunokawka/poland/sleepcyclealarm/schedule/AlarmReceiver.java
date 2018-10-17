package com.gmail.brunokawka.poland.sleepcyclealarm.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new AlarmController(context).startAlarm();
    }
}
