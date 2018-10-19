package com.gmail.brunokawka.poland.sleepcyclealarm.schedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmActivity extends AppCompatActivity {
    private static final String KEY_ALARM_ID = "alarm_id";

    @BindView(R.id.activityAlarmCurrentHour) protected TextView currentHourTextView;

    private PowerManager.WakeLock wakeLock;
    private int ringDurationInMillis;
    private String alarmId;

    @OnClick(R.id.activityAlarmLayout)
    public void onAlarmDismissRequest() {
        dismissAlarm();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
            PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, getClass().getName());
        setUpRingDuration();
        wakeLock.acquire(ringDurationInMillis);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

        setUpCurrentHourTextView();
        removeExecutedAlarmFromDatabase();
    }

    @Override
    public void onBackPressed() {
        dismissAlarm();
    }

    @Override
    public void onDestroy() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        super.onDestroy();
    }

    public void dismissAlarm() {
        Log.d(getClass().getName(), "Dismissing the alarm...");
        new AlarmController(this).dismissCurrentlyPlayingAlarm();
        finish();
    }

    private void setUpRingDuration() {
        final int defaultRingDurationInMillis = 5 * 60 * 1000;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ringDurationString = preferences.getString(getString(R.string.key_ring_duration), String.valueOf(defaultRingDurationInMillis));
        ringDurationInMillis = Integer.parseInt(ringDurationString);
    }

    private void setUpCurrentHourTextView() {
        String currentHour = getFormattedCurrentHour();
        currentHourTextView.setText(currentHour);
    }

    private String getFormattedCurrentHour() {
        DateTime currentDate = DateTime.now();
        return ItemContentBuilder.getTitle(currentDate);
    }

    private void removeExecutedAlarmFromDatabase() {
        Intent intent = getIntent();
        if (intent != null) {
            alarmId = intent.getStringExtra(KEY_ALARM_ID);
            if (alarmId != null) {
                Log.d(getClass().getName(), "Removing already executed alarm from Realm | alarm id: " + alarmId);
                new AlarmDAO().removeFromRealmById(alarmId);
            } else {
                Log.e(getClass().getName(), "Error while removing executed alarm from Realm | alarmId is null");
            }
        }
    }
}
