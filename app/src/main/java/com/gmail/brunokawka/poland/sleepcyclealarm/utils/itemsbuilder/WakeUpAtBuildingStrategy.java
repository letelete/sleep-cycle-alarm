package com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.AlarmContentUtils;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.TimeUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

public class WakeUpAtBuildingStrategy implements ItemsBuilderStrategy {
    private List<Item> items;
    private DateTime executionDate;
    private DateTime timeToGoToSleep;

    @Override
    public List<Item> getArrayOfItems(DateTime currentDate, DateTime executionDate) {
        items = new ArrayList<>();
        this.executionDate = executionDate;
        this.timeToGoToSleep = executionDate;

        int maxAmountOfItemsInList = ItemsBuilderData.getMaxAmountOfItemsInList();

        for (int itemCounter = 0; itemCounter < maxAmountOfItemsInList; itemCounter++) {
            if (isPossibleToCreateNextItem(currentDate, timeToGoToSleep)) {
                timeToGoToSleep = getNextAlarmDate(timeToGoToSleep);
                createNextItemAndAddItToArray();
            } else {
                break;
            }
        }

        return items;
    }

    @Override
    public DateTime getNextAlarmDate(DateTime executionDate) {
        return executionDate.minusMinutes(ItemsBuilderData.getOneSleepCycleDurationInMinutes());
    }

    @Override
    public boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime dateToGoToSleep) {
        if (dateToGoToSleep.isAfter(currentDate)) {
            long oneMinuteInMillis = 1000 * 60;
            long periodInMinutes = new Interval(currentDate, dateToGoToSleep)
                    .toDurationMillis() / oneMinuteInMillis;

            return (periodInMinutes >= ItemsBuilderData.getTotalOneSleepCycleDurationInMinutes());
        } else {
            return false;
        }
    }

    private void createNextItemAndAddItToArray() {
        int timeForFallAsleepInMinutes = ItemsBuilderData.getTimeForFallAsleepInMinutes();
        DateTime dateToGoToSleepPlusTimeToFallAsleepWithRoundedTime = TimeUtils
                .getClosestTimeRound(timeToGoToSleep.minusMinutes(timeForFallAsleepInMinutes));

        Item item = new Item();

        item.setTitle(AlarmContentUtils
                .getTitleForWakeUpAt(dateToGoToSleepPlusTimeToFallAsleepWithRoundedTime,
                        executionDate));
        item.setSummary(AlarmContentUtils.getSummary(timeToGoToSleep, executionDate));
        item.setCurrentDate(dateToGoToSleepPlusTimeToFallAsleepWithRoundedTime);
        item.setExecutionDate(executionDate);

        items.add(0, item);
    }
}
