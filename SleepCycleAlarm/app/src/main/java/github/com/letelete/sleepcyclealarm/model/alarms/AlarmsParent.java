package github.com.letelete.sleepcyclealarm.model.alarms;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AlarmsParent extends RealmObject {
    private RealmList<Alarm> itemList;

    public RealmList<Alarm> getItemList() {
        return itemList;
    }
}
