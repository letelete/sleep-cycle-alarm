package com.gmail.brunokawka.poland.sleepcyclealarm.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Intent service = new Intent(context, AlarmService.class);
        if (extras != null) {
            service.putExtras(extras);
        } else {
            Log.e(getClass().getName(), "onReceive(): extras == null");
        }
        context.startService(service);
    }
}
