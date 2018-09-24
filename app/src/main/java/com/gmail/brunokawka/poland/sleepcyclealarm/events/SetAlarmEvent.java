package com.gmail.brunokawka.poland.sleepcyclealarm.events;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;

public class SetAlarmEvent {

    private static final String LOG = "SetAlarmEventLog";

    private Item item;

    public SetAlarmEvent(Item item) {
        this.item = item;
        Log.d("SetAlarmEvent", item.getTitle());
    }

    public Item getItem() {
        return item;
    }
}
