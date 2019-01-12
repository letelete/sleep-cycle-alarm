package com.gmail.brunokawka.poland.sleepcyclealarm.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.app.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

public class AlarmDAO {

    public AlarmDAO() {
        RealmManager.incrementCount();
    }

    public void saveIfNotDuplicate(final Alarm alarm) {
        Realm realm = RealmManager.getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                if (isNotDuplicate(alarm, realm)) {
                    realm.insertOrUpdate(alarm);
                }
            }
        });
    }

    private boolean isNotDuplicate(Alarm alarm, Realm realm) {
        Alarm duplicateAlarm = realm.where(Alarm.class)
                .equalTo("executionDate", alarm.getExecutionDate()).findFirst();
        return duplicateAlarm == null;
    }

    public void saveEvenIfDuplicate(final Alarm alarm) {
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
        Realm realm = RealmManager.getRealm();
        RealmQuery<Alarm> query = realm.where(Alarm.class);
        return query.findAll();
    }

    public void cleanUp() {
        RealmManager.decrementCount();
    }

}
