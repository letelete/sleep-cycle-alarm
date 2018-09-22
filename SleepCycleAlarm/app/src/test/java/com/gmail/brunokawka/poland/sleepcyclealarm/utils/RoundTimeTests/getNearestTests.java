package com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTimeTests;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTime;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// This class use the following naming convention:
// Should_ExpectedBehavior_When_StateUnderTest

public class getNearestTests {

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void Should_RoundToFullHour_When_3MinutesAfterFullHour() {
        String hourToRound = "04/02/2011 06:03";
        String expected = "04/02/2011 06:00";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void Should_RoundToFullHour_When_4MinutesAfterFullHour() {
        String hourToRound = "04/02/2011 06:04";
        String expected = "04/02/2011 06:00";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void Should_RoundMinuteToHalfOfTen_When_1MinuteAheadOfTheToHalfOfTen() {
        String hourToRound = "04/02/2011 06:34";
        String expected = "04/02/2011 06:35";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void Should_RoundMinuteToHalfOfTen_When_1MinuteAfterHalfOfTen() {
        String hourToRound = "04/02/2011 06:36";
        String expected = "04/02/2011 06:35";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void Should_RoundMinuteToHalfOfTen_When_2MinutesAfterHalfOfTen() {
        String hourToRound = "04/02/2011 06:57";
        String expected = "04/02/2011 06:55";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void Should_RoundToFullHour_When_1MinuteAheadOfTheFullHour() {
        String hourToRound = "04/02/2011 06:59";
        String expected = "04/02/2011 07:00";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }
}