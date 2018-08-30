package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.Date;

public class ItemBuilder {
    private static final String TAG = "ItemBuilderLog";

    private static ArrayList<Item> items = new ArrayList<>();
    private final static int maxAmountOfItemsInList = 8;

    private static final int updateIntervalInMinutes = 4;
    private static DateTime lastUpdateTime;

    // Every sleep cycle is divisible by 90 minutes
    private static final int minutes = 90;
    private static final int timeForFallAsleepInMinutes = 15; // TODO: It could be an option in sharedPreferences to let user personalize that

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
        return minutesBetweenLastUpdate >= updateIntervalInMinutes || items.isEmpty();
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

        for (int i = 0; i < maxAmountOfItemsInList; i++) {
            Item item = new Item();

            executionTime = getNextAlarmTime(executionTime);
            roundedTime = getNearestRoundOfTime(executionTime.plusMinutes(timeForFallAsleepInMinutes));

            item.setTitle(ItemContentBuilder.getTitle(roundedTime));
            item.setSummary(ItemContentBuilder.getSummary(currentTime, executionTime));
            item.setCurrentDate(currentTime);
            item.setExecutionDate(roundedTime);

            items.add(item);
        }
    }

    private static DateTime getNextAlarmTime(DateTime currentExecutionTime) {
        return currentExecutionTime.plusMinutes(minutes);
    }

    public static DateTime getNearestRoundOfTime(DateTime dt) {
        if (isRoundToFullHourNeeded(dt)) {
            return dt.hourOfDay().roundHalfFloorCopy();
        } else if (isRoundToHalfOfTenNeeded(dt)) {
            return dt.plusMinutes(getDifferenceBetweenMinuteAndHalfOfTen(dt.getMinuteOfHour()));
        } else {
            int windowMinutes = 5;
            return dt.withMinuteOfHour((dt.getMinuteOfHour() / windowMinutes) * windowMinutes)
                    .minuteOfDay().roundFloorCopy();
        }
    }

    private static boolean isRoundToFullHourNeeded(DateTime dt) {
        return dt.getMinuteOfHour() > 57 || dt.getMinuteOfHour() < 5;
    }

    private static boolean isRoundToHalfOfTenNeeded(DateTime dt) {
        return dt.getMinuteOfHour() % 10 >= 3;
    }

    private static int getDifferenceBetweenMinuteAndHalfOfTen(int minute) {
        int halfOfTen = 5;
        return halfOfTen - (minute % 10);
    }
}
