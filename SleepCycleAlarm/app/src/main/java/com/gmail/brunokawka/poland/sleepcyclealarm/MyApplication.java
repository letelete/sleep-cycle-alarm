package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gmail.brunokawka.poland.sleepcyclealarm.model.alarms.AlarmsParent;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    private static final String TAG = "MyApplicationLog";

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(AlarmsParent.class);
                    }})
                .name("sleepcyclealarm.realm")
                .build();

        Realm.setDefaultConfiguration(config);

    }
}
