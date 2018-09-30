package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import org.joda.time.DateTime;

import java.util.List;

public class ItemsBuilder extends ItemsBuilderAbstraction {
    private static final String TAG = "ItemsBuilderLog";

    private List<Item> items;

    @Override
    public List<Item> getItems(DateTime currentDate, DateTime executionDate) {
        return getBuildingStrategy().getArrayOfItems(currentDate, executionDate);
    }

    @Override
    public DateTime getNextAlarmDate(DateTime executionDate) {
        return getBuildingStrategy().getNextAlarmDate(executionDate);
    }

    @Override
    public boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime executionDate) {
        return getBuildingStrategy().isPossibleToCreateNextItem(currentDate, executionDate);
    }
}
