package com.gmail.brunokawka.poland.sleepcyclealarm.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.CustomApp;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AlarmDAO {

    public AlarmDAO() {
        RealmManager.initializeRealmConfig();
        RealmManager.incrementCount();
    }

    public void generateAlarmAndSaveItToRealm(Item item, String ringtone) {
        Log.d(getClass().getName(), "Generating alarm and saving it to realm... (Base on item and ringtone)");
        Alarm alarm = getAlarmFromItemAndRingtone(item, ringtone);
        saveIfNotDuplicate(alarm);
    }

    public void generateAlarmAndSaveItToRealm(Item item) {
        Log.d(getClass().getName(), "Generating alarm and saving it to realm... (Base on item only)");
        Alarm alarm = getAlarmFromItem(item);
        saveIfNotDuplicate(alarm);
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
        final String ringtoneTitle = pref.getString(ctx.getString(R.string.key_ringtone_select), "None");
        final String currentDate = item.getCurrentDate().toString();
        final String executionDate = item.getExecutionDate().toString();

        Alarm alarm = new Alarm();
        alarm.setId(id);
        alarm.setTitle(title);
        alarm.setSummary(summary);
        alarm.setRingtone(ringtoneTitle);
        alarm.setCurrentDate(currentDate);
        alarm.setExecutionDate(executionDate);

        return alarm;
    }

    public void saveIfNotDuplicate(final Alarm alarm) {
        Log.d(getClass().getName(), "Saving to realm...");
        Realm realm = RealmManager.getRealm();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.insertOrUpdate(alarm);
            }
        });
    }

    public void removeFromRealmById(final String id) {
        Log.d(getClass().getName(), "Removing from realm...");
        Realm realm = RealmManager.getRealm();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                Alarm alarm = realm.where(Alarm.class).equalTo("id", id).findFirst();
                if(alarm != null) {
                    alarm.deleteFromRealm();
                }
            }
        });
    }

    public List<Alarm> getListOfAlarms() {
        List<Alarm> alarms = new ArrayList<>();

        Realm realm = RealmManager.getRealm();
        RealmQuery<Alarm> query = realm.where(Alarm.class);
        RealmResults<Alarm> results = query.findAll();
        if (!results.isEmpty()) {
            alarms = results;
        }

        return alarms;
    }

    public void cleanUp() {
        RealmManager.decrementCount();
    }

}
