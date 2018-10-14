package com.gmail.brunokawka.poland.sleepcyclealarm.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmActivity extends AppCompatActivity {
    private PowerManager.WakeLock wakeLock;
    private int ringDurationInMillis;

    @BindView(R.id.activityAlarmCurrentHour)
    protected TextView currentHourTextView;

    @OnClick(R.id.activityAlarmDismissAlarmButton)
    public void onAlarmDismissButtonClicked() {
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
    }

    @Override
    public void onBackPressed() {
        dismissAlarm();
    }

    public void dismissAlarm() {
        Log.d(getClass().getName(), "Dismissing the alarm...");
        finish();
    }

    @Override
    public void onDestroy() {
        wakeLock.release();
        super.onDestroy();
    }

    private void setUpRingDuration() {
        final int defaultRingDurationInMillis = 5 * 60 * 1000;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ringDurationInMillis = preferences.getInt(getString(R.string.key_ring_duration), defaultRingDurationInMillis);
    }

    private void setUpCurrentHourTextView() {
        String currentHour = getFormattedCurrentHour();
        currentHourTextView.setText(currentHour);
    }

    private String getFormattedCurrentHour() {
        DateTime currentDate = DateTime.now();
        int hour = currentDate.getHourOfDay();
        int minute = currentDate.getMinuteOfHour();
        return String.valueOf(hour)
                + ":"
                + String.valueOf(minute);
    }
}
