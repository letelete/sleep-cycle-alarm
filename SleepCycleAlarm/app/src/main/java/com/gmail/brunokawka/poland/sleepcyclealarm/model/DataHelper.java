package com.gmail.brunokawka.poland.sleepcyclealarm.model;

import android.support.annotation.NonNull;

import com.gmail.brunokawka.poland.sleepcyclealarm.model.alarms.Alarm;
import io.realm.Realm;

public class DataHelper {
    private static final String TAG = "DataHelperLog";

    public static void addItemAsync(Realm realm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                Alarm.create(realm);
            }
        });
    }

    public static void deleteItemAsync(Realm realm, final long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                Alarm.delete(realm, id);
            }
        });
    }
}
