package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtItemBuilderTests;

import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtItemsBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// This class uses the following naming convention:
// Should_ExpectedBehavior_When_StateUnderTest

public class isPossibleToCreateNextItemTests {

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void Should_True_When_CurrentDateIsBeforeDateToGoToSleepAndHasEnoughTimeToReachSleepCycle() {
        String currentDate = "04/02/2011 18:30";
        String dateToGoToSleep = "04/02/2011 23:00";

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedDateToGoToSleep = getDateTimeFormatted(dateToGoToSleep);
        assertTrue(WakeUpAtItemsBuilder.isPossibleToCreateNextItem(formattedCurrentDate, formattedDateToGoToSleep));
    }

    @Test
    public void Should_False_When_CurrentDateIsAfterDateToGoToSleep() {
        String currentDate = "04/02/2011 20:30";
        String dateToGoToSleep = "04/02/2011 20:20";

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedDateToGoToSleep = getDateTimeFormatted(dateToGoToSleep);
        assertFalse(WakeUpAtItemsBuilder.isPossibleToCreateNextItem(formattedCurrentDate, formattedDateToGoToSleep));
    }

    @Test
    public void Should_True_When_DateOfCurrentDateIsBeforeDateOfDateToGoToSleep() {
        String currentDate = "04/02/2011 20:30";
        String dateToGoToSleep = "05/02/2011 20:20";

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedDateToGoToSleep = getDateTimeFormatted(dateToGoToSleep);
        assertTrue(WakeUpAtItemsBuilder.isPossibleToCreateNextItem(formattedCurrentDate, formattedDateToGoToSleep));
    }

    @Test
    public void Should_False_When_CurrentDateIsBeforeDateToGoToSleepButLengthBetweenTheseTwoIsLessThanSleepCycleDuration() {
        String currentDate = "04/02/2011 23:53";
        String dateToGoToSleep = "05/02/2011 00:53";

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedDateToGoToSleep = getDateTimeFormatted(dateToGoToSleep);
        assertFalse(WakeUpAtItemsBuilder.isPossibleToCreateNextItem(formattedCurrentDate, formattedDateToGoToSleep));
    }

    @Test
    public void Should_True_When_CurrentDateIsBeforeDateToGoToSleepAndHasEnoughTimeToReachSleepCycle2() {
        String currentDate = "04/02/2011 00:30";
        String dateToGoToSleep = "04/02/2011 02:30";

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedDateToGoToSleep = getDateTimeFormatted(dateToGoToSleep);
        assertTrue(WakeUpAtItemsBuilder.isPossibleToCreateNextItem(formattedCurrentDate, formattedDateToGoToSleep));
    }

    @Test
    public void Should_True_When_CurrentDateIsBeforeDateToGoToSleepAndHasEnoughTimeToReachSleepCycle3() {
        String currentDate = "04/02/2011 11:54";
        String dateToGoToSleep = "04/02/2011 14:54";

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedDateToGoToSleep = getDateTimeFormatted(dateToGoToSleep);
        assertTrue(WakeUpAtItemsBuilder.isPossibleToCreateNextItem(formattedCurrentDate, formattedDateToGoToSleep));
    }
}