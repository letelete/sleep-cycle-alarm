package com.gmail.brunokawka.poland.sleepcyclealarm.events;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

public class ItemInListClickedEvent {

    private Item item;

    public ItemInListClickedEvent(Item item) {
        this.item = item;
        Log.d(getClass().getName(), item.getTitle());
    }

    public Item getItem() {
        return item;
    }
}
