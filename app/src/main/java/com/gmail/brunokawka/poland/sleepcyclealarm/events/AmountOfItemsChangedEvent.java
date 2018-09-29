package com.gmail.brunokawka.poland.sleepcyclealarm.events;

public class AmountOfItemsChangedEvent {
    private static final String LOG = "SetAlarmEventLog";

    private int itemsAmount;

    public AmountOfItemsChangedEvent(int itemsAmount) {
        this.itemsAmount = itemsAmount;
    }

    public int getItemsAmount() {
        return itemsAmount;
    }
}
