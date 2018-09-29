package com.gmail.brunokawka.poland.sleepcyclealarm.events;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

public class ItemInListClickedEvent {

    private static final String LOG = "SetAlarmEventLog";

    private Item item;

    public ItemInListClickedEvent(Item item) {
        this.item = item;
        Log.d("ItemInListClickedEvent", item.getTitle());
    }

    public Item getItem() {
        return item;
    }
}
