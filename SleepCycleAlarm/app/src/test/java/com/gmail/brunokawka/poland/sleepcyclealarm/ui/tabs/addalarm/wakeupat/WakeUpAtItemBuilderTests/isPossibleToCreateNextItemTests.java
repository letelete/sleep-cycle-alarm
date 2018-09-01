package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtItemBuilderTests;

import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtItemBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class isPossibleToCreateNextItemTests {

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void Should_True_When_CurrentDateIsBeforeTimeToGoToSleep() {
        String currentDate = "04/02/2011 18:30";
        String timeToGoToSleep = "04/02/2011 21:50";

        DateTime timeToGoToSleepDate = getDateTimeFormatted(timeToGoToSleep);
        DateTime currentDateDate = getDateTimeFormatted(currentDate);
        assertTrue(WakeUpAtItemBuilder.isPossibleToCreateNextItem(timeToGoToSleepDate, currentDateDate));
    }

    @Test
    public void Should_False_When_CurrentDateIsAfterTimeToGoToSleep() {
        String currentDate = "04/02/2011 20:30";
        String timeToGoToSleep = "04/02/2011 20:20";

        DateTime timeToGoToSleepDate = getDateTimeFormatted(timeToGoToSleep);
        DateTime currentDateDate = getDateTimeFormatted(currentDate);
        assertFalse(WakeUpAtItemBuilder.isPossibleToCreateNextItem(timeToGoToSleepDate, currentDateDate));
    }

    @Test
    public void Should_True_When_DateOfCurrentDateIsBeforeDateOfTimeToGoToSleep() {
        String currentDate = "04/02/2011 20:30";
        String timeToGoToSleep = "05/02/2011 20:20";

        DateTime timeToGoToSleepDate = getDateTimeFormatted(timeToGoToSleep);
        DateTime currentDateDate = getDateTimeFormatted(currentDate);
        assertTrue(WakeUpAtItemBuilder.isPossibleToCreateNextItem(timeToGoToSleepDate, currentDateDate));
    }
}