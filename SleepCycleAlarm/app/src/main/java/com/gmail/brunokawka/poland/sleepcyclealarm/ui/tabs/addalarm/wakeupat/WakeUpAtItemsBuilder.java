package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTime;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;

public class WakeUpAtItemsBuilder {
    private static final String TAG = "WakeUpAtItemBuilderLog";

    private static ArrayList<Item> items = new ArrayList<>();
    private static DateTime lastUpdateDate;
    private static DateTime currentDate;
    private static DateTime executionDate;

    public static ArrayList<Item> getItemsForExecutionDate(DateTime executionDate) {
        if (executionDate != null) {
            setCurrentDate();
            setExecutionDate(executionDate);

            if (isUpdateNeeded()) {
                lastUpdateDate = currentDate;
                clearArrayIfNotEmpty();
                fillArrayWithItemsBasedOnExecutionDate();
            } else {
                Log.d(TAG, "Update not needed");
            }
        } else {
            Log.d(TAG, "Execution date is null");
        }

        return items;
    }

    private static void setCurrentDate() {
        currentDate = DateTime.now();
    }

    private static void setExecutionDate(DateTime newExecutionDate) {
        executionDate = newExecutionDate;
    }

    private static boolean isUpdateNeeded() {
        int minutesBetweenLastUpdate = new Period(lastUpdateDate, currentDate).getMinutes();
        int updateIntervalInMinutes = ItemsBuilderData.getUpdateIntervalInMinutes();

        return minutesBetweenLastUpdate >= updateIntervalInMinutes
                || items.isEmpty()
                || executionDate.getMillis() != currentDate.getMillis();
    }

    private static void clearArrayIfNotEmpty() {
        if (!items.isEmpty()) {
            items.clear();
        } else {
            Log.d(TAG, "Array is empty");
        }
    }

    private static void fillArrayWithItemsBasedOnExecutionDate() {
        DateTime timeToGoToSleep = executionDate;

        int maxAmountOfItemsInList = ItemsBuilderData.getMaxAmountOfItemsInList();
        for (int itemCounter = 0; itemCounter < maxAmountOfItemsInList; itemCounter++) {

            timeToGoToSleep = getNextAlarmDate(timeToGoToSleep);

            if (isPossibleToCreateNextItem(timeToGoToSleep)) {
                createNextItemAndAddItToArray(timeToGoToSleep);
            } else {
                break;
            }
        }
    }

    private static DateTime getNextAlarmDate(DateTime timeToGoToSleep) {
        int sleepCycleDuration = ItemsBuilderData.getOneSleepCycleDurationInMinutes();
        return timeToGoToSleep.minusMinutes(sleepCycleDuration);
    }

    public static boolean isPossibleToCreateNextItem(DateTime timeToGoToSleep) {
        if (timeToGoToSleep.getDayOfYear() == currentDate.getDayOfYear()) {
            return timeToGoToSleep.getMillis() > currentDate.getMillis();
        } else {
            return timeToGoToSleep.getDayOfYear() > currentDate.getDayOfYear();
        }
    }

    private static void createNextItemAndAddItToArray(DateTime timeToGoToSleep) {
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
