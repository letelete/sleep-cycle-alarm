package com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import org.joda.time.DateTime;

import java.util.List;

public abstract class ItemsBuilderAbstraction {


    private ItemsBuilderStrategy buildingStrategy;

    public void setBuildingStrategy(ItemsBuilderStrategy buildingStrategy) {
        this.buildingStrategy = buildingStrategy;
    }

    ItemsBuilderStrategy getBuildingStrategy() {
        return this.buildingStrategy;
    }

    abstract List<Item> getItems(DateTime currentDate, DateTime executionDate);

    abstract DateTime getNextAlarmDate(DateTime executionDate);

    abstract boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime executionDate);

}
