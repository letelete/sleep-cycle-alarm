package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTime;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;

public class WakeUpAtItemBuilder {
    private static final String TAG = "WakeUpAtItemBuilderLog";

    private static ArrayList<Item> items = new ArrayList<>();
    private static DateTime lastUpdateDate;

    public static ArrayList<Item> getItemsForExecutionDate(DateTime executionDate) {
        DateTime currentDate = DateTime.now();
        if (isUpdateNeeded(currentDate, executionDate)) {
            lastUpdateDate = currentDate;
            clearGivenArrayIfNotEmpty();
            fillArrayWithItemsBasedOnExecutionDate(executionDate);
        } else {
            Log.d(TAG, "Update not needed");
        }
        return items;
    }

    private static boolean isUpdateNeeded(DateTime currentDate, DateTime executionDate) {
        int minutesBetweenLastUpdate = new Period(lastUpdateDate, currentDate).getMinutes();
        int updateIntervalInMinutes = ItemsBuilderData.getUpdateIntervalInMinutes();

        return minutesBetweenLastUpdate >= updateIntervalInMinutes
                || items.isEmpty()
                || executionDate.getMillis() != currentDate.getMillis();
    }

    private static void clearGivenArrayIfNotEmpty() {
        if (!items.isEmpty()) {
            items.clear();
        } else {
            Log.d(TAG, "Array is empty");
        }
    }

    private static void fillArrayWithItemsBasedOnExecutionDate(DateTime executionDate) {
        DateTime timeToGoToSleep = executionDate;
        DateTime currentDate = DateTime.now();

        int maxAmountOfItemsInList = ItemsBuilderData.getMaxAmountOfItemsInList();
        Log.d(TAG, "maxAmountOfItemsInList: " + String.valueOf(maxAmountOfItemsInList));

        for (int itemCounter = 0; itemCounter < maxAmountOfItemsInList; itemCounter++) {

            timeToGoToSleep = getNextAlarmTime(timeToGoToSleep);

            if (isPossibleToCreateNextItem(timeToGoToSleep, currentDate)) {
                createNextItemAndAddItToArray(timeToGoToSleep, executionDate);
            } else {
                break;
            }
        }
    }

    private static DateTime getNextAlarmTime(DateTime currentExecutionDate) {
        int sleepCycleDuration = ItemsBuilderData.getOneSleepCycleDurationInMinutes();
        return currentExecutionDate.minusMinutes(sleepCycleDuration);
    }

    public static boolean isPossibleToCreateNextItem(DateTime timeToGoToSleep, DateTime currentDate) {
        if (timeToGoToSleep.getDayOfYear() == currentDate.getDayOfYear()) {
            return timeToGoToSleep.getMillis() > currentDate.getMillis();
        } else {
            return timeToGoToSleep.getDayOfYear() > currentDate.getDayOfYear();
        }
    }

    private static void createNextItemAndAddItToArray(DateTime timeToGoToSleep, DateTime executionDate) {
        int timeForFallAsleepInMinutes = ItemsBuilderData.getTimeForFallAsleepInMinutes();
        DateTime roundedTime = RoundTime.getNearest(timeToGoToSleep.minusMinutes(timeForFallAsleepInMinutes));

        Item item = new Item();

        item.setTitle(ItemContentBuilder.getTitle(roundedTime));
        item.setSummary(ItemContentBuilder.getSummary(timeToGoToSleep, executionDate));
        item.setCurrentDate(timeToGoToSleep);
        item.setExecutionDate(executionDate);

        items.add(0, item); // every new item's being add at the beginning of array because we want our array to be sorted by hour descending
    }
}
