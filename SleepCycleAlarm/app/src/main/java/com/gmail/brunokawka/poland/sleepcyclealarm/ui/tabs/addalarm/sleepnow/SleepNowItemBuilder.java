package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.sleepnow;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTime;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;

public class SleepNowItemBuilder {
    private static final String TAG = "ItemBuilderLog";

    private static ArrayList<Item> items = new ArrayList<>();
    private static DateTime lastUpdateTime;

    public static ArrayList<Item> getItemsForCurrentDate(DateTime currentTime) {
        if (isUpdateNeeded(currentTime)) {
            lastUpdateTime = currentTime;
            clearArrayIfNotEmpty();
            fillArrayWithItemsBasedOnCurrentTime(currentTime);
        } else {
            Log.d(TAG, "Update not needed");
        }
        return items;
    }

    private static boolean isUpdateNeeded(DateTime newTime) {
        int minutesBetweenLastUpdate = new Period(lastUpdateTime, newTime).getMinutes();
        return minutesBetweenLastUpdate >= ItemsBuilderData.getUpdateIntervalInMinutes() || items.isEmpty();
    }

    private static void clearArrayIfNotEmpty() {
        if (!items.isEmpty()) {
            items.clear();
        } else {
            Log.d(TAG, "Array is empty");
        }
    }

    private static void fillArrayWithItemsBasedOnCurrentTime(DateTime currentTime) {
        DateTime executionTime = currentTime;
        DateTime roundedTime;

        for (int i = 0; i < ItemsBuilderData.getMaxAmountOfItemsInList(); i++) {
            executionTime = getNextAlarmTime(executionTime);
            createNextItemAndAddItToArray(currentTime, executionTime);
        }
    }

    private static DateTime getNextAlarmTime(DateTime currentExecutionTime) {
        return currentExecutionTime.plusMinutes(ItemsBuilderData.getOneSleepCycleDurationInMinutes());
    }

    private static void createNextItemAndAddItToArray(DateTime currentDate, DateTime executionDate) {
        int timeForFallAsleepInMinutes = ItemsBuilderData.getTimeForFallAsleepInMinutes();
        DateTime roundedTime = RoundTime.getNearest(executionDate.plusMinutes(timeForFallAsleepInMinutes));

        Item item = new Item();

        item.setTitle(ItemContentBuilder.getTitle(roundedTime));
        item.setSummary(ItemContentBuilder.getSummary(currentDate, executionDate));
        item.setCurrentDate(currentDate);
        item.setExecutionDate(roundedTime);

        items.add(item);
    }
}
