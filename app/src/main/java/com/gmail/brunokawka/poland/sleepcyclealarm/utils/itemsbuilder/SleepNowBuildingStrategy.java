package com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.AlarmContentUtils;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.TimeUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class SleepNowBuildingStrategy implements ItemsBuilderStrategy {
    private List<Item> items;
    private DateTime currentDate;
    private DateTime executionDate;

    @Override
    public List<Item> getArrayOfItems(DateTime currentDate, @Nullable DateTime executionDate) {
        items = new ArrayList<>();
        this.currentDate = currentDate;
        this.executionDate = currentDate;

        for (int i = 0; i < ItemsBuilderData.getMaxAmountOfItemsInList(); i++) {
            if (isPossibleToCreateNextItem(currentDate, this.executionDate)) {
                this.executionDate = getNextAlarmDate(this.executionDate);
                createNextItemAndAddItToArray();
            }
        }

        return items;
    }

    @Override
    public DateTime getNextAlarmDate(DateTime executionDate) {
        return executionDate.plusMinutes(ItemsBuilderData.getOneSleepCycleDurationInMinutes());
    }

    @Override
    public boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime dateToGoToSleep) {
        return true;
    }

    private void createNextItemAndAddItToArray() {
        DateTime executionDatePlusTimeToFallAsleepWithRoundedTime = TimeUtils
                .getClosestTimeRound(executionDate
                        .plusMinutes(ItemsBuilderData.getTimeForFallAsleepInMinutes()));

        Item item = new Item();

        item.setTitle(AlarmContentUtils.getTitle(executionDatePlusTimeToFallAsleepWithRoundedTime));
        item.setSummary(AlarmContentUtils.getSummary(currentDate, executionDate));
        item.setCurrentDate(currentDate);
        item.setExecutionDate(executionDatePlusTimeToFallAsleepWithRoundedTime);

        items.add(item);
    }
}
