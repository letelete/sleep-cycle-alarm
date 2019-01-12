package com.gmail.brunokawka.poland.sleepcyclealarm.events;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

public class AddAlarmEvent {

    private Item item;

    public AddAlarmEvent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
