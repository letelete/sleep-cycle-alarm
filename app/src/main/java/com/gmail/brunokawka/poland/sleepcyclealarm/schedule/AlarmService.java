package com.gmail.brunokawka.poland.sleepcyclealarm.schedule;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;

public class AlarmService extends Service {

    private static final String KEY_RINGTONE_ID = "ringtone_id";
    private static final String KEY_ALARM_ID = "alarm_id";
    private final static String HANDLER_THREAD_NAME = "alarm_service";

    private final static int PAUSE_BETWEEN_VIBRATE_DELAY_IN_MS = 2000;
    private final static int VIBRATION_DURATION_IN_MS = 1000;
    private final static int VOLUME_INCREASE_DELAY_IN_MS = 600;

    private final static float VOLUME_INCREASE_STEP = 0.01f;
    private final static float MAX_VOLUME = 1.0f;

    private String ringtonePassedInIntent;
    private String alarmIdPassedInIntent;
    private SharedPreferences preferences;
    private MediaPlayer player;
    private Vibrator vibrator;
    private float volumeLevel = 0.0f;

    private Handler handler = new Handler();
    private Runnable vibrationRunnable = new Runnable() {
        @Override
        public void run() {
            vibrator.vibrate(VIBRATION_DURATION_IN_MS);
            handler.postDelayed(vibrationRunnable,
                    VIBRATION_DURATION_IN_MS + PAUSE_BETWEEN_VIBRATE_DELAY_IN_MS);
        }
    };

    private Runnable gentleVolumeRunnable = new Runnable() {
        @Override
        public void run() {
            if (player != null && volumeLevel < MAX_VOLUME) {
                volumeLevel += VOLUME_INCREASE_STEP;
                player.setVolume(volumeLevel, volumeLevel);
                handler.postDelayed(gentleVolumeRunnable, VOLUME_INCREASE_DELAY_IN_MS);
            }
        }
    };

    @Override
    public void onCreate() {
        HandlerThread ht = new HandlerThread(HANDLER_THREAD_NAME);
        ht.start();
        handler = new Handler(ht.getLooper());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setAlarmVariablesByValuesPassedInIntent(intent);
        startPlayer();
        startAlarmActivity();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        cleanUpPlayerIfPlaying();
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void setAlarmVariablesByValuesPassedInIntent(Intent intent) {
        ringtonePassedInIntent = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
        alarmIdPassedInIntent = "DEFAULT_ID";

        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                tryToSetAlarmIdVariable(extras);
                tryToSetAlarmRingtoneVariable(extras);
            } else {
                Log.e(getClass().getName(), "Extras is null");
            }
        } else {
            Log.e(getClass().getName(), "Intent is null... Setting default values");
        }
    }

    private void tryToSetAlarmIdVariable(Bundle extras) {
        if (extras.getString(KEY_ALARM_ID) != null) {
            alarmIdPassedInIntent = extras.getString(KEY_ALARM_ID);
            Log.d(getClass().getName(), "ALARM_ID Overridden! New value: " + alarmIdPassedInIntent);
        } else {
            Log.e(getClass().getName(), "ALARM_ID is null");
        }
    }

    private void tryToSetAlarmRingtoneVariable(Bundle extras) {
        if (extras.getString(KEY_RINGTONE_ID) != null) {
            ringtonePassedInIntent = extras.getString(KEY_RINGTONE_ID);
            Log.d(getClass().getName(), "KEY_RINGTONE_ID Overridden! New value: " + ringtonePassedInIntent);
        } else {
            Log.e(getClass().getName(), "KEY_RINGTONE_ID is null");
        }
    }

    private void startPlayer() {
        player = new MediaPlayer();

        try {
            postVibrationHandlerIfVibrationEnabled();
            player.setDataSource(this, Uri.parse(ringtonePassedInIntent));
            player.setLooping(true);
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setVolume(volumeLevel, volumeLevel);
            player.prepare();
            player.start();
            postDelayedVolumeHandler();
        } catch (Exception e) {
            if (player.isPlaying()) {
                player.stop();
            }
            Log.e(getClass().getName(), "Error while starting player. Error message: " + e.getMessage());
            stopSelf();
        }
    }

    private void postVibrationHandlerIfVibrationEnabled() {
        if (isVibrateEnabled()) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            handler.post(vibrationRunnable);
        } else {
            Log.d(getClass().getName(), "Phone vibration when ringing is disabled");
        }
    }

    private boolean isVibrateEnabled() {
        return preferences.getBoolean(getString(R.string.key_alarm_vibrate_when_ringing), true);
    }

    private void postDelayedVolumeHandler() {
        handler.postDelayed(gentleVolumeRunnable, VOLUME_INCREASE_DELAY_IN_MS);
    }

    private void startAlarmActivity() {
        Intent alarmActivityIntent = new Intent(Intent.ACTION_MAIN);
        alarmActivityIntent.setComponent(new ComponentName(this, AlarmActivity.class));
        alarmActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        alarmActivityIntent.putExtra(KEY_ALARM_ID, alarmIdPassedInIntent);
        startActivity(alarmActivityIntent);
    }

    private void cleanUpPlayerIfPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
