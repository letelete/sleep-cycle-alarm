package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import org.joda.time.DateTime;

import java.util.ArrayList;

public interface ItemsBuilderStrategy {
    ArrayList<Item> getArrayOfItems(DateTime currentDate, DateTime executionDate);

    DateTime getNextAlarmDate(DateTime executionDate);

    boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime dateToGoToSleep);
}
