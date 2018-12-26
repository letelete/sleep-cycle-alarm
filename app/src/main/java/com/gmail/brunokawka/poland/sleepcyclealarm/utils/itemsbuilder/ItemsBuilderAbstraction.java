package com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import org.joda.time.DateTime;

import java.util.List;

public abstract class ItemsBuilderAbstraction {
    private ItemsBuilderStrategy buildingStrategy;

    public void setBuildingStrategy(ItemsBuilderStrategy buildingStrategy) {
        this.buildingStrategy = buildingStrategy;
    }

    public ItemsBuilderStrategy getBuildingStrategy() {
        return this.buildingStrategy;
    }

    public abstract List<Item> getItems(DateTime currentDate, DateTime executionDate);

    public abstract DateTime getNextAlarmDate(DateTime executionDate);

    public abstract boolean isPossibleToCreateNextItem(DateTime currentDate,
                                                       DateTime executionDate);

}
