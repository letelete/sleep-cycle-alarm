package com.gmail.brunokawka.poland.sleepcyclealarm.model.alarms;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AlarmsParent extends RealmObject {
    private static final String TAG = "AlarmsParentLog";

    @SuppressWarnings("unused")
    private RealmList<Alarm> itemList;

    public RealmList<Alarm> getItemList() {
        return itemList;
    }
}
