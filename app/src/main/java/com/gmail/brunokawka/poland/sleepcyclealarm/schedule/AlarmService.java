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
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.Const;

public class AlarmService extends Service {

    private final static String HANDLER_THREAD_NAME = "HANDLER_THREAD_NAME";

    private final static int PAUSE_BETWEEN_VIBRATE_DELAY_IN_MS = 2000;
    private final static int VIBRATION_DURATION_IN_MS = 1000;
    private final static int VOLUME_INCREASE_DELAY_IN_MS = 600;

    private final static float VOLUME_INCREASE_STEP = 0.01f;
    private final static float MAX_VOLUME = 1.0f;

    private SharedPreferences preferences;
    private MediaPlayer player;
    private Vibrator vibrator;
    private float volumeLevel = 0.0f;
    private String alarmRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            .toString();
    private String alarmId = "";

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
        if (intent != null && intent.getExtras() != null) {
            setupAlarmVariablesWithPassedData(intent.getExtras());
        } else {
            Log.e(getClass().getName(), "onStartCommand(): setting up default variables");
        }
        startPlayer();
        startAlarmActivity();
        return START_NOT_STICKY;
    }

    private void setupAlarmVariablesWithPassedData(Bundle extras) {
        setupAlarmId(extras);
        setupRingtone(extras);
    }

    private void setupAlarmId(Bundle extras) {
        if (extras.getString(Const.KEYS.ALARM_ID) != null) {
            alarmId = extras.getString(Const.KEYS.ALARM_ID);
        } else {
            Log.e(getClass().getName(), "setupAlarmId(): extras value == null");
        }
    }

    private void setupRingtone(Bundle extras) {
        if (extras.getString(Const.KEYS.RINGTONE_ID) != null) {
            alarmRingtone = extras.getString(Const.KEYS.RINGTONE_ID);
        } else {
            Log.e(getClass().getName(), "setupRingtone() extras value == null");
        }
    }

    private void startPlayer() {
        player = new MediaPlayer();

        try {
            if (isVibrateEnabled()) {
                postVibrationHandler();
            }
            player.setDataSource(this, Uri.parse(alarmRingtone));
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
            Log.e(getClass().getName(), "startPlayer(): " + e.getMessage());
            stopSelf();
        }
    }

    private boolean isVibrateEnabled() {
        return preferences.getBoolean(getString(R.string.key_alarm_vibrate_when_ringing), true);
    }

    private void postVibrationHandler() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        handler.post(vibrationRunnable);
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
        alarmActivityIntent.putExtra(Const.KEYS.ALARM_ID, alarmId);
        startActivity(alarmActivityIntent);
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

    private void cleanUpPlayerIfPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
