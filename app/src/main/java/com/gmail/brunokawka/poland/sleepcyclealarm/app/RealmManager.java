package com.gmail.brunokawka.poland.sleepcyclealarm.app;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmManager {

    private static Realm realm;
    private static RealmConfiguration realmConfiguration;
    private static int activityCount = 0;

    public static void initializeRealmConfig() {
        if(realmConfiguration == null) {
            Log.d(RealmManager.class.getName(), "Initializing Realm configuration.");
            setRealmConfiguration(new RealmConfiguration.Builder()
                    .initialData(new RealmInitialData())
                    .deleteRealmIfMigrationNeeded()
                    .build());
        }
    }

    private static void setRealmConfiguration(RealmConfiguration realmConfiguration) {
        RealmManager.realmConfiguration = realmConfiguration;
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static Realm getRealm() {
        return realm;
    }

    public static void incrementCount() {
        if(activityCount == 0) {
            if(realm != null && !realm.isClosed()) {
                Log.w(RealmManager.class.getName(), "Unexpected open Realm found.");
                realm.close();
            }
            Log.d(RealmManager.class.getName(),
                    "Incrementing Activity Count [0]: opening Realm.");
            realm = Realm.getDefaultInstance();
        }
        activityCount++;
        Log.d(RealmManager.class.getName(), "Increment: Count [" + activityCount + "]");
    }

    public static void decrementCount() {
        activityCount--;
        Log.d(RealmManager.class.getName(), "Decrement: Count [" + activityCount + "]");
        if(activityCount <= 0) {
            Log.d(RealmManager.class.getName(), "Decrementing Activity Count: closing Realm.");
            activityCount = 0;
            realm.close();
            // realmConfiguration null on Android 6 (Test with xiaomi Mi4)
            if (realmConfiguration == null) initializeRealmConfig();
            if(Realm.compactRealm(realmConfiguration)) {
                Log.d(RealmManager.class.getName(), "Realm compacted successfully.");
            }
            realm = null;
        }
    }
}
