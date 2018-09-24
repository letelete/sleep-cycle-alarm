package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.WakeUpAtStrategy;


// This class uses the following naming convention:
// Should_ExpectedBehavior_When_StateUnderTest

import com.gmail.brunokawka.poland.sleepcyclealarm.TestsHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.WakeUpAtBuildingStrategy;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class isPossibleToCreateNextItemTests {

    private ItemsBuilder itemsBuilder;

    @Before
    public void setUp() {
        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());
    }

    @Test
    public void Should_True_When_CurrentDateIsBeforeDateToGoToSleepAndHasEnoughTimeToReachSleepCycle() {
        String currentDate = "04/02/2011 18:30";
        String dateToGoToSleep = "04/02/2011 23:00";

        assertTrue(itemsBuilder.isPossibleToCreateNextItem(
                TestsHelper.getDateTimeFromString(currentDate),
                TestsHelper.getDateTimeFromString(dateToGoToSleep)));
    }

    @Test
    public void Should_False_When_CurrentDateIsAfterDateToGoToSleep() {
        String currentDate = "04/02/2011 20:30";
        String dateToGoToSleep = "04/02/2011 20:20";

        assertFalse(itemsBuilder.isPossibleToCreateNextItem(
                TestsHelper.getDateTimeFromString(currentDate),
                TestsHelper.getDateTimeFromString(dateToGoToSleep)));
    }

    @Test
    public void Should_True_When_DateOfCurrentDateIsBeforeDateOfDateToGoToSleep() {
        String currentDate = "04/02/2011 20:30";
        String dateToGoToSleep = "05/02/2011 20:20";

        assertTrue(itemsBuilder.isPossibleToCreateNextItem(
                TestsHelper.getDateTimeFromString(currentDate),
                TestsHelper.getDateTimeFromString(dateToGoToSleep)));
    }

    @Test
    public void Should_False_When_CurrentDateIsBeforeDateToGoToSleepButLengthBetweenTheseTwoIsLessThanSleepCycleDuration() {
        String currentDate = "04/02/2011 23:53";
        String dateToGoToSleep = "05/02/2011 00:53";

        assertFalse(itemsBuilder.isPossibleToCreateNextItem(
                TestsHelper.getDateTimeFromString(currentDate),
                TestsHelper.getDateTimeFromString(dateToGoToSleep)));
    }

    @Test
    public void Should_True_When_CurrentDateIsBeforeDateToGoToSleepAndHasEnoughTimeToReachSleepCycle2() {
        String currentDate = "04/02/2011 00:30";
        String dateToGoToSleep = "04/02/2011 02:30";

        assertTrue(itemsBuilder.isPossibleToCreateNextItem(
                TestsHelper.getDateTimeFromString(currentDate),
                TestsHelper.getDateTimeFromString(dateToGoToSleep)));
    }

    @Test
    public void Should_True_When_CurrentDateIsBeforeDateToGoToSleepAndHasEnoughTimeToReachSleepCycle3() {
        String currentDate = "04/02/2011 11:54";
        String dateToGoToSleep = "04/02/2011 14:54";

        assertTrue(itemsBuilder.isPossibleToCreateNextItem(
                TestsHelper.getDateTimeFromString(currentDate),
                TestsHelper.getDateTimeFromString(dateToGoToSleep)));
    }
}
