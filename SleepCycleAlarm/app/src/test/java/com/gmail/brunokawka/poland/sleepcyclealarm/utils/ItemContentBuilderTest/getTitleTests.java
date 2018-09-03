package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilderTest;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// This class use the following naming convention:
// test[Feature being tested]

public class getTitleTests {

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void testIfAddAColonBetweenHourAndMinuteAndIfInFrontOfHourIs0() {
        String executionHour = "04/02/2011 06:30";
        String expected = "06:30";

        DateTime dateTime = getDateTimeFormatted(executionHour);
        assertEquals(expected, ItemContentBuilder.getTitle(dateTime));
    }

    @Test
    public void testIfAddAColonBetweenHourAndMinuteAndIfInFrontOfHourAndMinuteIs0() {
        String executionHour = "04/02/2011 00:03";
        String expected = "00:03";

        DateTime dateTime = getDateTimeFormatted(executionHour);
        assertEquals(expected, ItemContentBuilder.getTitle(dateTime));
    }

    @Test
    public void testIfAddAColonBetweenHourAndMinuteAndIf2DigitsHourIsSavedCorrectly() {
        String executionHour = "04/02/2011 23:59";
        String expected = "23:59";

        DateTime dateTime = getDateTimeFormatted(executionHour);
        assertEquals(expected, ItemContentBuilder.getTitle(dateTime));
    }

    @Test
    public void testIfAddAColonBetweenHourAndMinuteAndIfAll4DigitsAreEquals0() {
        String executionHour = "04/02/2011 00:00";
        String expected = "00:00";

        DateTime dateTime = getDateTimeFormatted(executionHour);
        assertEquals(expected, ItemContentBuilder.getTitle(dateTime));
    }


}
