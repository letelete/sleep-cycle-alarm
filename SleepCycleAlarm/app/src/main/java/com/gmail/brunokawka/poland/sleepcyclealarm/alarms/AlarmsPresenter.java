package com.gmail.brunokawka.poland.sleepcyclealarm.alarms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.CustomApp;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Alarm;

import java.util.UUID;

import io.realm.Realm;

public class AlarmsPresenter {

    public static AlarmsPresenter getService(Context context) {
        return AlarmsFragment.getAlarmsPresenter();
    }

    public static final String TAG = "AlarmsPresenterLog";

    public interface ViewContract {
        void showAddAlarmDialog();

        void showEditAlarmDialog(Alarm alarm);

        interface DialogContract {
            String getRingtone(); // TODO: not sure how ringtone will be passed like so setted up String for entry testing

            void bind(Alarm alarm);
        }
    }

    private ViewContract viewContract;

    private boolean isDialogShowing;

    boolean hasView() {
        return viewContract != null;
    }

    public void bindView(ViewContract viewContract) {
        this.viewContract = viewContract;
        if(isDialogShowing) {
            showAddDialog();
        }
    }

    public void unbindView() {
        this.viewContract = null;
    }

    public void showAddDialog() {
        if(hasView()) {
            isDialogShowing = true;
            viewContract.showAddAlarmDialog();
        }
    }

    public void dismissAddDialog() {
        isDialogShowing = false;
    }

    public void showEditDialog(Alarm alarm) {
        if(hasView()) {
            viewContract.showEditAlarmDialog(alarm);
        }
    }

    public void saveAlarm(ViewContract.DialogContract dialogContract, final SharedPreferences sharedPreferences, final int hour, final int minute) {
        if(hasView()) {
            Realm realm = RealmManager.getRealm();
            Context ctx = CustomApp.getContext();

            Uri ringtoneUri = Uri.parse(sharedPreferences.getString(ctx.getString(R.string.key_ringtone_select), "DEFAULT_RINGTONE_URI"));
            Ringtone ringtone = RingtoneManager.getRingtone(ctx, ringtoneUri);

            final boolean isSameRingtoneForEveryAlarm = sharedPreferences.getBoolean(ctx.getString(R.string.key_alarms_has_same_ringtone), false);

            final String id = UUID.randomUUID().toString();
            final int snoozeDuration = sharedPreferences.getInt(ctx.getString(R.string.key_alarms_intervals), 5);
            final boolean isRingingInSilentMode = sharedPreferences.getBoolean(ctx.getString(R.string.key_alarm_in_silent_mode), true);
            // TODO : RINGTONE (Currently create some string for entry testing)
            final String ringtoneTitle = isSameRingtoneForEveryAlarm
                    ? ringtone.getTitle(ctx)
                    : dialogContract.getRingtone();
            final int ringDuration = sharedPreferences.getInt(ctx.getString(R.string.key_ring_duration), 5);
            final int numberOfRepetitions = sharedPreferences.getInt(ctx.getString(R.string.key_auto_silence), 3);

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Alarm alarm = new Alarm();
                    alarm.setId(id);
                    alarm.setHour(hour);
                    alarm.setMinute(minute);
                    alarm.setSnoozeDurationInMinutes(snoozeDuration);
                    alarm.setRingingInSilentMode(isRingingInSilentMode);
                    alarm.setRingtone(ringtoneTitle);
                    alarm.setRingDurationInMinutes(ringDuration);
                    alarm.setNumberOfRepetitionsBeforeAutoSilence(numberOfRepetitions);
                    realm.insertOrUpdate(alarm);
                }
            });
        }
    }

    public void deleteAlarmByIdWithDialog(final String id) {
        // TODO: if user swipes list item confirmation dialog will be displayed
    }

    public void deleteAlarmById(final String id) {
        Realm realm = RealmManager.getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Alarm alarm = realm.where(Alarm.class).equalTo("id", id).findFirst();
                if(alarm != null) {
                    alarm.deleteFromRealm();
                }
            }
        });
    }

    public void editAlarm(final ViewContract.DialogContract dialogContract, final String id) {
        Realm realm = RealmManager.getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Alarm alarm = realm.where(Alarm.class).equalTo("id", id).findFirst();
                if(alarm != null) {
                    // TODO: RINGTONE + TIME FOR FALL ASLEEP
                    // Currently create some string for entry testing
                    alarm.setRingtone(dialogContract.getRingtone());
                }
            }
        });
    }
}
