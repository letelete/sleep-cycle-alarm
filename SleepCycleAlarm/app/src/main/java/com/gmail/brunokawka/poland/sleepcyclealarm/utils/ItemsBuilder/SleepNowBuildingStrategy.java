package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTime;

import org.joda.time.DateTime;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class SleepNowBuildingStrategy implements ItemsBuilderStrategy {
    private static final String TAG = "SleepNowStrategyLog";

    private ArrayList<Item> items;

    private DateTime currentDate;
    private DateTime executionDate;

    private boolean isFirstAlarmDate = true;

    @Override
    public ArrayList<Item> getArrayOfItems(DateTime currentDate, @Nullable DateTime executionDate) {
        items = new ArrayList<>();
        this.currentDate = currentDate;
        this.executionDate = currentDate;

        for (int i = 0; i < ItemsBuilderData.getMaxAmountOfItemsInList(); i++) {
            if (isPossibleToCreateNextItem(currentDate, this.executionDate)) {
                this.executionDate = getNextAlarmDate(this.executionDate);
                createNextItemAndAddItToArray();
            } else {
                Log.e(TAG, "Its not possible to create next item");
            }

            if (isFirstAlarmDate) {
                isFirstAlarmDate = false;
            }
        }

        return items;
    }

    @Override
    public DateTime getNextAlarmDate(DateTime executionDate) {
        return executionDate.plusMinutes(isFirstAlarmDate
                ? ItemsBuilderData.getTotalOneSleepCycleDurationInMinutes()
                : ItemsBuilderData.getOneSleepCycleDurationInMinutes()
        );
    }

    @Override
    public boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime dateToGoToSleep) {
        return true;
    }

    private void createNextItemAndAddItToArray() {
        DateTime executionDateWithRoundedTime = RoundTime.getNearest(executionDate);

        Item item = new Item();

        item.setTitle(ItemContentBuilder.getTitle(executionDateWithRoundedTime));
        item.setSummary(ItemContentBuilder.getSummary(currentDate, executionDate.minusMinutes(ItemsBuilderData.getTimeForFallAsleepInMinutes())));
        item.setCurrentDate(currentDate);
        item.setExecutionDate(executionDateWithRoundedTime);

        items.add(item);
    }
}
