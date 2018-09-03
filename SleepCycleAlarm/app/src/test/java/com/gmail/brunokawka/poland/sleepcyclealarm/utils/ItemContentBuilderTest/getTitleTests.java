package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilderTest;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// This class use the following naming convention:
// Should_ExpectedBehavior_When_StateUnderTest

public class getTitleTests {

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void getTitle_6and30_6Colon30() {
        String executionHour = "04/02/2011 06:30";
        String expected = "06:30";

        DateTime dateTime = getDateTimeFormatted(executionHour);
        assertEquals(expected, ItemContentBuilder.getTitle(dateTime));
    }

    @Test
    public void getTitle_0and3_00Colon03() {
        String executionHour = "04/02/2011 00:03";
        String expected = "00:03";

        DateTime dateTime = getDateTimeFormatted(executionHour);
        assertEquals(expected, ItemContentBuilder.getTitle(dateTime));
    }

    @Test
    public void getTitle_23and59_23Colon59() {
        String executionHour = "04/02/2011 23:59";
        String expected = "23:59";

        DateTime dateTime = getDateTimeFormatted(executionHour);
        assertEquals(expected, ItemContentBuilder.getTitle(dateTime));
    }

    @Test
    public void getTitle_12and3_12Colon03() {
        String executionHour = "04/02/2011 12:03";
        String expected = "12:03";

        DateTime dateTime = getDateTimeFormatted(executionHour);
        assertEquals(expected, ItemContentBuilder.getTitle(dateTime));
    }

    @Test
    public void getTitle_0and0_00Colon00() {
        String executionHour = "04/02/2011 00:00";
        String expected = "00:00";

        DateTime dateTime = getDateTimeFormatted(executionHour);
        assertEquals(expected, ItemContentBuilder.getTitle(dateTime));
    }


}
