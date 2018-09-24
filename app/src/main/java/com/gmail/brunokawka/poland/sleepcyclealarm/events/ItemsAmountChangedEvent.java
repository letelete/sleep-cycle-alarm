package com.gmail.brunokawka.poland.sleepcyclealarm.events;

public class ItemsAmountChangedEvent {
    private static final String LOG = "SetAlarmEventLog";

    private int itemsAmount;

    public ItemsAmountChangedEvent(int itemsAmount) {
        this.itemsAmount = itemsAmount;
    }

    public int getItemsAmount() {
        return itemsAmount;
    }
}
