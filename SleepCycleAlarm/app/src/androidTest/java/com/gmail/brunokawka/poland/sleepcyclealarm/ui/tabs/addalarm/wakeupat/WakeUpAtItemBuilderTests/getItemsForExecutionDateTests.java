package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtItemBuilderTests;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtItemsBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

// This class uses the following naming convention:
// test[Feature being tested]

public class getItemsForExecutionDateTests {
    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void testIfCanReturnLastDateToGoToSleep() {
        String currentDate = "02/01/1111 01:00";
        String executionDate = "02/01/1111 07:00";
        String dateToGoToSleep = "02/01/1111 05:15";

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedExecutionDate = getDateTimeFormatted(executionDate);
        DateTime formattedDateToGoToSleep = getDateTimeFormatted(dateToGoToSleep);
        ArrayList<Item> items = WakeUpAtItemsBuilder.getItemsForExecutionDate(formattedCurrentDate, formattedExecutionDate);

        int index = items.size() - 1;

        assertEquals(formattedDateToGoToSleep, items.get(index).getCurrentDate());
    }

    @Test
    public void testIfCanReturnFirstDateToGoToSleep() {
        String currentDate = "01/01/1111 14:53";
        String executionDate = "02/01/1111 07:00";
        String dateToGoToSleep = "01/01/1111 18:45";

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedExecutionDate = getDateTimeFormatted(executionDate);
        DateTime formattedDateToGoToSleep = getDateTimeFormatted(dateToGoToSleep);
        ArrayList<Item> items = WakeUpAtItemsBuilder.getItemsForExecutionDate(formattedCurrentDate, formattedExecutionDate);

        int index = 0;

        assertEquals(formattedDateToGoToSleep, items.get(index).getCurrentDate());
    }

    @Test
    public void testIfCanReturnFirstDateWithOnlyOneItem() {
        String currentDate = "01/01/1111 13:30";
        String executionDate = "01/01/1111 15:30";
        String dateToGoToSleep = "01/01/1111 13:45";

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedExecutionDate = getDateTimeFormatted(executionDate);
        DateTime formattedDateToGoToSleep = getDateTimeFormatted(dateToGoToSleep);
        ArrayList<Item> items = WakeUpAtItemsBuilder.getItemsForExecutionDate(formattedCurrentDate, formattedExecutionDate);

        int index = 0;

        assertEquals(formattedDateToGoToSleep, items.get(index).getCurrentDate());
    }
}