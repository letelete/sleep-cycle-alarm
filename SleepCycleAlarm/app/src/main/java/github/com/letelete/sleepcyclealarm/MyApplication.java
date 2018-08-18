package github.com.letelete.sleepcyclealarm;

import android.app.Application;
import android.support.annotation.NonNull;

import github.com.letelete.sleepcyclealarm.model.alarms.AlarmsParent;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        realm.createObject(AlarmsParent.class);
                    }
                }).build();
        Realm.deleteRealm(realmConfiguration);
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
