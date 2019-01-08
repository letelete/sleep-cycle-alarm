package com.gmail.brunokawka.poland.sleepcyclealarm.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.Const;

import org.joda.time.DateTime;

import java.util.UUID;

public class AlarmController {

    private Context context;
    private AlarmManager alarmManager;

    public AlarmController(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(Alarm alarm) {
        PendingIntent setAlarmPendingIntent
                = getAlarmPendingIntent(alarm, PendingIntent.FLAG_UPDATE_CURRENT);
        long executionDateMs = DateTime.parse(alarm.getExecutionDate()).getMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, executionDateMs,
                    setAlarmPendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, executionDateMs, setAlarmPendingIntent);
        }
    }

    public void cancelAlarm(Alarm alarm) {
        PendingIntent cancelServicePendingIntent = getAlarmPendingIntent(alarm, 0);
        alarmManager.cancel(cancelServicePendingIntent);
    }

    public void dismissCurrentlyPlayingAlarm() {
        context.stopService(new Intent(context, AlarmService.class));
    }

    private PendingIntent getAlarmPendingIntent(Alarm alarm, int flag) {
        String alarmId = alarm.getId();
        String ringtone = alarm.getRingtone();
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra(Const.KEYS.ALARM_ID, alarmId);
        alarmIntent.putExtra(Const.KEYS.RINGTONE_ID, ringtone);

        int serviceId = getMostSignificantBits(alarmId);

        return PendingIntent.getBroadcast(context, serviceId, alarmIntent, flag);
    }

    private int getMostSignificantBits(String id) {
        return (int) Math
                .abs(UUID.fromString(id).getMostSignificantBits());
    }
}
