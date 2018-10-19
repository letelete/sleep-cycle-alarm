package com.gmail.brunokawka.poland.sleepcyclealarm.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public class AlarmController {
    private final static String KEY_ALARM_ID = "alarm_id";
    private static final String KEY_RINGTONE_ID = "ringtone_id";

    private Context context;
    private AlarmDAO alarmDAO;
    private List<Alarm> alarms;

    public AlarmController(Context context) {
        this.context = context;
        alarmDAO = new AlarmDAO();
    }

    public void rescheduleAlarms() {
        if (alarms != null) {
            cancelAllAlarms();
        }
        updateDataAndScheduleAllAlarms();
    }

    public void dismissCurrentlyPlayingAlarm() {
        context.stopService(new Intent(context, AlarmService.class));
    }

    public void startAlarm(Bundle extras) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtras(extras);
        context.startService(intent);
    }

    private void updateDataAndScheduleAllAlarms() {
        updateAlarmsList();
        for (Alarm alarm : alarms) {
            scheduleAlarm(alarm);
        }
    }

    private void cancelAllAlarms() {
        for (Alarm alarm : alarms) {
            cancelAlarm(alarm);
        }
    }

    private void updateAlarmsList() {
        alarms = alarmDAO.getListOfAlarms();
        Log.d(getClass().getName(), "Alarms list updated. With " + alarms.size() + " objects now");
    }

    private void scheduleAlarm(Alarm alarm) {
        PendingIntent pendingIntent = createPendingIntent(context, alarm);
        long executionDateInMillis = DateTime.parse(alarm.getExecutionDate()).getMillis();
        setAlarm(context, executionDateInMillis, pendingIntent);
    }

    private void setAlarm(Context context, long executionDateInMillis, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, executionDateInMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, executionDateInMillis, pendingIntent);
            }
        }
    }

    private void cancelAlarm(Alarm alarm) {
        PendingIntent pendingIntent = createPendingIntent(context, alarm);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private PendingIntent createPendingIntent(Context context, Alarm alarm) {
        String alarmId = alarm.getId();
        String ringtone = alarm.getRingtone();
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(KEY_ALARM_ID, alarmId);
        intent.putExtra(KEY_RINGTONE_ID, ringtone);
        int alarmIdLeastSignificantBits = (int) Math.abs(UUID.fromString(alarmId).getLeastSignificantBits());

        return PendingIntent.getBroadcast(context, alarmIdLeastSignificantBits, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
