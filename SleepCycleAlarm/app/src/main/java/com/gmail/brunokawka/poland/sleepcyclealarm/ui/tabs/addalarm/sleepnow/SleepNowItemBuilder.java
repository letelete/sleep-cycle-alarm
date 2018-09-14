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
    private static final String TAG = "SleepNowItemBuilderLog";

    private static ArrayList<Item> items = new ArrayList<>();
    private static DateTime lastUpdateDate;
    private static DateTime currentDate;
    private static DateTime executionDate;

    public static ArrayList<Item> getItemsForCurrentDate(DateTime currentDate) {
        setCurrentDate();

        if (isUpdateNeeded()) {
            lastUpdateDate = currentDate;
            clearArrayIfNotEmpty();
            fillArrayWithItemsBasedOnCurrentDate();
        } else {
            Log.d(TAG, "Update not needed");
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
        return minutesBetweenLastUpdate >= ItemsBuilderData.getUpdateIntervalInMinutes() || items.isEmpty();
    }

    private static void clearArrayIfNotEmpty() {
        if (!items.isEmpty()) {
            items.clear();
        } else {
            Log.d(TAG, "Array is empty");
        }
    }

    private static void fillArrayWithItemsBasedOnCurrentDate() {
        setExecutionDate(currentDate);

        for (int i = 0; i < ItemsBuilderData.getMaxAmountOfItemsInList(); i++) {
            setExecutionDate(getNextAlarmDate());
            createNextItemAndAddItToArray();
        }
    }

    private static DateTime getNextAlarmDate() {
        return executionDate.plusMinutes(ItemsBuilderData.getOneSleepCycleDurationInMinutes());
    }

    private static void createNextItemAndAddItToArray() {
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
