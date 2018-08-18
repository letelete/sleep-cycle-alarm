package github.com.letelete.sleepcyclealarm.model.alarms;

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Alarm extends RealmObject {
    public static final String FIELD_ID = "id";

    private static AtomicInteger INTEGER_COUNTER = new AtomicInteger(0);

    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public static void create(Realm realm) {
        AlarmsParent parent = realm.where(AlarmsParent.class).findFirst();
        RealmList<Alarm> alarms = parent.getItemList();
        Alarm counterObject = realm.createObject(Alarm.class, getIncrementation());
        alarms.add(counterObject);
    }

    private static int getIncrementation() {
        return INTEGER_COUNTER.getAndIncrement();
    }

    public static void delete(Realm realm, long id) {
        Alarm item = realm.where(Alarm.class).equalTo(FIELD_ID, id).findFirst();
        if(item != null) {
            item.deleteFromRealm();
        }
    }
}
