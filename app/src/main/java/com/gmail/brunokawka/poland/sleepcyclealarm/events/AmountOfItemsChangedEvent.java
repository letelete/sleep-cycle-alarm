package com.gmail.brunokawka.poland.sleepcyclealarm.events;

public class AmountOfItemsChangedEvent {

    private int itemsAmount;

    public AmountOfItemsChangedEvent(int itemsAmount) {
        this.itemsAmount = itemsAmount;
    }

    public int getItemsAmount() {
        return itemsAmount;
    }
}
