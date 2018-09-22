package com.gmail.brunokawka.poland.sleepcyclealarm.application;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmManager {
    private static final String TAG = "RealmManagerLog";

    private static Realm realm;
    private static RealmConfiguration realmConfiguration;

    public static void initializeRealmConfig() {
        if(realmConfiguration == null) {
            Log.d(TAG, "Initializing Realm configuration.");
            setRealmConfiguration(new RealmConfiguration.Builder()
                    .initialData(new RealmInitialData())
                    .deleteRealmIfMigrationNeeded()
                    .build());
        }
    }

    public static void setRealmConfiguration(RealmConfiguration realmConfiguration) {
        RealmManager.realmConfiguration = realmConfiguration;
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private static int activityCount = 0;

    public static Realm getRealm() {
        return realm;
    }

    public static void incrementCount() {
        if(activityCount == 0) {
            if(realm != null) {
                if(!realm.isClosed()) {
                    Log.w(TAG, "Unexpected open Realm found.");
                    realm.close();
                }
            }
            Log.d(TAG, "Incrementing Activity Count [0]: opening Realm.");
            realm = Realm.getDefaultInstance();
        }
        activityCount++;
        Log.d(TAG, "Increment: Count [" + activityCount + "]");
    }

    public static void decrementCount() {
        activityCount--;
        Log.d(TAG, "Decrement: Count [" + activityCount + "]");
        if(activityCount <= 0) {
            Log.d(TAG, "Decrementing Activity Count: closing Realm.");
            activityCount = 0;
            realm.close();
            if(Realm.compactRealm(realmConfiguration)) {
                Log.d(TAG, "Realm compacted successfully.");
            }
            realm = null;
        }
    }
}
