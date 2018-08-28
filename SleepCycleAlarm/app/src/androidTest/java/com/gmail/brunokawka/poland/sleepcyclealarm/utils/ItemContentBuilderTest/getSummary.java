package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilderTest;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/*
    Naming convention:
    methodName_StateUnderTest_ExpectedBehavior
 */

@RunWith(MockitoJUnitRunner.class)
public class getSummary {

    @Before
    public void setUp() {
        String dateFormat = "MM/dd/yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.UK);
    }

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void getSummary_10and30and12and0_1h30minOfSleepUnhealthy() {
        String currentDateString = "01/01/1111 10:30";
        String executionDateString = "02/01/1111 12:00";
        String sleepDuration = "1h 30min";
        String sleepQuality = "Unhealthy";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        DateTime currentDate = getDateTimeFormatted(currentDateString);
        DateTime executionDate = getDateTimeFormatted(executionDateString);
        assertEquals(expected, ItemContentBuilder.getSummary(currentDate, executionDate));
    }

    @Test
    public void getSummary_2and0and8and0_6hOfSleepHealthy() {
        String currentDateString = "01/01/1111 02:00";
        String executionDateString = "01/01/1111 08:00";
        String sleepDuration = "6h";
        String sleepQuality = "Optimal";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        DateTime currentDate = getDateTimeFormatted(currentDateString);
        DateTime executionDate = getDateTimeFormatted(executionDateString);
        assertEquals(expected, ItemContentBuilder.getSummary(currentDate, executionDate));
    }

    @Test
    public void getSummary_23and0and8and30_9h30minOfSleepHealthy() {
        String currentDateString = "01/01/1111 23:00";
        String executionDateString = "02/01/1111 08:30";
        String sleepDuration = "9h 30min";
        String sleepQuality = "Healthy";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        DateTime currentDate = getDateTimeFormatted(currentDateString);
        DateTime executionDate = getDateTimeFormatted(executionDateString);
        assertEquals(expected, ItemContentBuilder.getSummary(currentDate, executionDate));
    }

    @Test
    public void getSummary_1and30and12and30_11hOfSleepNotRecommended() {
        String currentDateString = "01/01/1111 01:30";
        String executionDateString = "01/01/1111 12:30";
        String sleepDuration = "11h";
        String sleepQuality = "Not recommended";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        DateTime currentDate = getDateTimeFormatted(currentDateString);
        DateTime executionDate = getDateTimeFormatted(executionDateString);
        assertEquals(expected, ItemContentBuilder.getSummary(currentDate, executionDate));
    }
}
