package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTime;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;

public class WakeUpAtBuildingStrategy implements ItemsBuilderStrategy {
    private static final String TAG = "WakeUpAtStrategyLog";

    private ArrayList<Item> items;

    private DateTime executionDate;
    private DateTime timeToGoToSleep;

    @Override
    public ArrayList<Item> getArrayOfItems(DateTime currentDate, DateTime executionDate) {
        items = new ArrayList<>();
        this.executionDate = executionDate;
        this.timeToGoToSleep = executionDate;

        int maxAmountOfItemsInList = ItemsBuilderData.getMaxAmountOfItemsInList();

        for (int itemCounter = 0; itemCounter < maxAmountOfItemsInList; itemCounter++) {
            if (isPossibleToCreateNextItem(currentDate, timeToGoToSleep)) {
                timeToGoToSleep = getNextAlarmDate(executionDate);
                createNextItemAndAddItToArray();
            } else {
                Log.d(TAG, "Its not possible to create next item");
                break;
            }
        }

        return items;
    }

    @Override
    public DateTime getNextAlarmDate(DateTime executionDate) {
        return executionDate.minusMinutes(ItemsBuilderData.getTotalOneSleepCycleDurationInMinutes());
    }

    @Override
    public boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime dateToGoToSleep) {
        if (dateToGoToSleep.isAfter(currentDate)) {
            long oneMinuteInMillis = 1000 * 60;
            long periodInMinutes = new Interval(currentDate, dateToGoToSleep).toDurationMillis() / oneMinuteInMillis;

            return (periodInMinutes >= ItemsBuilderData.getTotalOneSleepCycleDurationInMinutes());
        } else {
            return false;
        }
    }

    private void createNextItemAndAddItToArray() {
        DateTime dateToGoToSleepWithoutTimeToForAsleep = executionDate.minus(ItemsBuilderData.getTimeForFallAsleepInMinutes());
        DateTime dateToGoToSleepWithRoundedTime = RoundTime.getNearest(dateToGoToSleepWithoutTimeToForAsleep);

        Item item = new Item();

        item.setTitle(ItemContentBuilder.getTitle(dateToGoToSleepWithRoundedTime));
        item.setSummary(ItemContentBuilder.getSummary(dateToGoToSleepWithoutTimeToForAsleep, executionDate));
        item.setCurrentDate(dateToGoToSleepWithRoundedTime);
        item.setExecutionDate(executionDate);

        items.add(0, item);
    }
}
