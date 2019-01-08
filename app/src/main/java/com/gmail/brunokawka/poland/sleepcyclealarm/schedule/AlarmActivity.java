package com.gmail.brunokawka.poland.sleepcyclealarm.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.AlarmContentUtils;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.Const;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ThemeUtils;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmActivity extends AppCompatActivity {

    private AlarmDAO alarmDAO;
    private AlarmController alarmController;
    private long ringDurationMs;

    @BindView(R.id.activityAlarmCurrentHour) protected TextView currentHourTextView;

    @OnClick(R.id.activityAlarmLayout)
    public void onAlarmDismissRequest() {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWindowFlags();
        setAppTheme();

        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        alarmDAO = new AlarmDAO();
        alarmController = new AlarmController(getApplicationContext());

        removeExecutedAlarmFromDatabase();
        showCurrentHour();
        setupRingDuration();
        countDownRingDuration();
    }

    private void setupWindowFlags() {
        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void setAppTheme() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String key = getString(R.string.key_change_theme);
        int themeId = ThemeUtils.getCurrentTheme(pref.getString(key, Const.DEFAULTS.THEME_ID));
        getDelegate().setLocalNightMode(themeId);
    }

    private void removeExecutedAlarmFromDatabase() {
        Intent intent = getIntent();
        if (intent != null) {
            String alarmId = intent.getStringExtra(Const.KEYS.ALARM_ID);
            if (!TextUtils.isEmpty(alarmId)) {
                alarmDAO.removeFromRealmById(alarmId);
            } else {
                Log.e(getClass().getName(),
                        "removeExecutedAlarmFromDatabase(): alarmId is empty");
            }
        } else {
            Log.e(getClass().getName(), "removeExecutedAlarmFromDatabase(): intent == null");
        }
    }

    private void showCurrentHour() {
        String currentHour = AlarmContentUtils.getTitle(DateTime.now());
        currentHourTextView.setText(currentHour);
    }

    private void setupRingDuration() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        String ringDurationString = preferences.getString(getString(R.string.key_ring_duration),
                String.valueOf(Const.DEFAULTS.RING_DURATION_MS));
        ringDurationMs = Long.parseLong(ringDurationString);
    }

    private void countDownRingDuration() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, ringDurationMs);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alarmController.dismissCurrentlyPlayingAlarm();
        alarmDAO.cleanUp();
    }
}
