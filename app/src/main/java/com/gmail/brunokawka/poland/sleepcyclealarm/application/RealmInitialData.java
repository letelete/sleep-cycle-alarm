package com.gmail.brunokawka.poland.sleepcyclealarm.application;


import io.realm.Realm;

public class RealmInitialData implements Realm.Transaction {
    @Override
    public void execute(Realm realm) {

    }

    @Override
    public int hashCode() {
        return RealmInitialData.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof RealmInitialData;
    }
}