package github.com.letelete.sleepcyclealarm.model;

import android.support.annotation.NonNull;

import github.com.letelete.sleepcyclealarm.model.alarms.Alarm;
import io.realm.Realm;

public class DataHelper {

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
