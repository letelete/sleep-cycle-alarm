package com.gmail.brunokawka.poland.sleepcyclealarm.app;


import android.util.Log;

import io.realm.Realm;

public class RealmInitialData implements Realm.Transaction {
    @Override
    public void execute(Realm realm) {
        Log.d(getClass().getName(), "Execute RealmInitialData");
    }

    @Override
    public int hashCode() {
        return RealmInitialData.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RealmInitialData;
    }
}