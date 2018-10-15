package com.gmail.brunokawka.poland.sleepcyclealarm.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;

import org.joda.time.DateTime;

import java.util.List;

public class AlarmController {
    private final static String KEY_ALARM_ID = "alarm_id";

    private Context context;
    private AlarmDAO alarmDAO;
    private List<Alarm> alarms;

    public AlarmController(Context context) {
        this.context = context;
        alarmDAO = new AlarmDAO();
    }

    public void scheduleAlarms() {
        updateAlarmsList();
        for (Alarm alarm : alarms) {
            scheduleAlarm(alarm);
        }
    }

    public void cancelAlarms() {
        updateAlarmsList();
        for (Alarm alarm : alarms) {
            cancelAlarm(alarm);
        }
    }

    public void dismissCurrentlyPlayingAlarm() {
        context.stopService(new Intent(context, AlarmService.class));
    }

    public void startAlarm() {
        context.startService(new Intent(context, AlarmService.class));
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
        int alarmId = (int) DateTime.parse(alarm.getId()).getMillis();

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(KEY_ALARM_ID, alarmId);

        return PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
