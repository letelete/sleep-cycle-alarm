package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTime;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.ArrayList;

public class WakeUpAtItemsBuilder {
    private static final String TAG = "WakeUpAtItemsBuilderLog";

    private static ArrayList<Item> items = new ArrayList<>();
    private static DateTime lastUpdateDate;
    private static DateTime currentDate;
    private static DateTime executionDate;

    public static ArrayList<Item> getItemsForExecutionDate(DateTime currentDate, DateTime executionDate) {
        if (executionDate != null) {
            setCurrentDate(currentDate);
            setExecutionDate(executionDate);

            if (isUpdateNeeded()) {
                updateLastUpdateDate();
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

    private static void setCurrentDate(DateTime currentDate) {
        WakeUpAtItemsBuilder.currentDate = currentDate;
    }

    private static void setExecutionDate(DateTime executionDate) {
        WakeUpAtItemsBuilder.executionDate = executionDate;
    }

    private static boolean isUpdateNeeded() {
        int minutesBetweenLastUpdate = new Period(lastUpdateDate, currentDate).getMinutes();
        int updateIntervalInMinutes = ItemsBuilderData.getUpdateIntervalInMinutes();

        return minutesBetweenLastUpdate >= updateIntervalInMinutes
                || items.isEmpty()
                || executionDate.getMillis() != currentDate.getMillis();
    }

    private static void updateLastUpdateDate() {
        lastUpdateDate = currentDate;
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
            if (isPossibleToCreateNextItem(currentDate, timeToGoToSleep)) {
                timeToGoToSleep = getNextAlarmDate(timeToGoToSleep);
                createNextItemAndAddItToArray(timeToGoToSleep);
            } else {
                Log.d(TAG, "Its not possible to create next item");
                break;
            }
        }
    }

    private static DateTime getNextAlarmDate(DateTime timeToGoToSleep) {
        int sleepCycleDuration = ItemsBuilderData.getOneSleepCycleDurationInMinutes();
        return timeToGoToSleep.minusMinutes(sleepCycleDuration);
    }

    public static boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime timeToGoToSleep) {
        if (timeToGoToSleep.isAfter(currentDate)) {
            long oneMinuteInMillis = 1000 * 60;
            long periodInMinutes = new Interval(currentDate, timeToGoToSleep).toDurationMillis() / oneMinuteInMillis;
            int sleepCycleDurationInTotal = ItemsBuilderData.getTotalOneSleepCycleDurationInMinutes();
            return (periodInMinutes >= sleepCycleDurationInTotal);
        } else {
            return false;
        }
    }

    private static void createNextItemAndAddItToArray(DateTime timeToGoToSleep) {
        int timeForFallAsleepInMinutes = ItemsBuilderData.getTimeForFallAsleepInMinutes();
        DateTime roundedTime = RoundTime.getNearest(timeToGoToSleep.minusMinutes(timeForFallAsleepInMinutes));

        Item item = new Item();

        item.setTitle(ItemContentBuilder.getTitle(roundedTime));
        item.setSummary(ItemContentBuilder.getSummary(timeToGoToSleep, executionDate));
        item.setCurrentDate(roundedTime);
        item.setExecutionDate(executionDate);

        items.add(0, item); // every new item's being add at the beginning of array because we want our array to be sorted by hour descending
    }
}
