package com.gmail.brunokawka.poland.sleepcyclealarm.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.CustomApp;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import java.util.UUID;

import io.realm.Realm;

public class AlarmDAO {

    public AlarmDAO() {
        RealmManager.initializeRealmConfig();
        RealmManager.incrementCount();
    }

    public void generateAlarmAndSaveItToRealm(Item item, String ringtone) {
        Log.d(getClass().getName(), "Generating alarm and saving it to realm... (Base on item and ringtone)");
        Alarm alarm = getAlarmFromItemAndRingtone(item, ringtone);
        saveToRealm(alarm);
    }

    public void generateAlarmAndSaveItToRealm(Item item) {
        Log.d(getClass().getName(), "Generating alarm and saving it to realm... (Base on item only)");
        Alarm alarm = getAlarmFromItem(item);
        saveToRealm(alarm);
    }

    public Alarm getAlarmFromItemAndRingtone(Item item, String ringtone) {
        Log.d(getClass().getName(), "Returning alarm from item and ringtone...");
        Alarm alarm = getAlarmFromItem(item);

        Log.d(getClass().getName(), "Overriding ringtone to given one...");
        alarm.setRingtone(ringtone);
        return alarm;
    }

    private Alarm getAlarmFromItem(Item item) {
        Log.d(getClass().getName(), "Getting alarm from item...");

        Context ctx = CustomApp.getContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);

        final String id = UUID.randomUUID().toString();

        final String title = item.getTitle();

        final String summary = item.getSummary();

        final String ringtoneTitle = pref.getString(ctx.getString(R.string.key_ringtone_select), "None"); // TODO : RINGTONE (Currently create some string for entry testing)

        final boolean isRingingInSilentMode = pref.getBoolean(ctx.getString(R.string.key_alarm_in_silent_mode), true);

        final int snoozeDuration = Integer.parseInt(pref.getString(ctx.getString(R.string.key_alarms_intervals), "5"));

        final int ringDuration = Integer.parseInt(pref.getString(ctx.getString(R.string.key_ring_duration), "5"));

        final int numberOfRepetitions = Integer.parseInt(pref.getString(ctx.getString(R.string.key_auto_silence), "3"));

        Alarm alarm = new Alarm();
        alarm.setId(id);
        alarm.setTitle(title);
        alarm.setSummary(summary);
        alarm.setSnoozeDurationInMinutes(snoozeDuration);
        alarm.setRingingInSilentMode(isRingingInSilentMode);
        alarm.setRingtone(ringtoneTitle);
        alarm.setRingDurationInMinutes(ringDuration);
        alarm.setNumberOfRepetitionsBeforeAutoSilence(numberOfRepetitions);

        return alarm;
    }

    public void saveToRealm(final Alarm alarm) {
        Log.d(getClass().getName(), "Saving to realm...");
        Realm realm = RealmManager.getRealm();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(alarm);
            }
        });
    }

    public void removeFromRealmById(final String id) {
        Log.d(getClass().getName(), "Removing from realm...");
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

    public void cleanUp() {
        RealmManager.decrementCount();
    }

}
