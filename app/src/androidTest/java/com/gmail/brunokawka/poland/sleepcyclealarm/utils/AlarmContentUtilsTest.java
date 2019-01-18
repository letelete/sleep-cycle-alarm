package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AlarmContentUtilsTest {

    private static final String CURRENT_DATE_STRING = "01/01/1111 22:20";

    private static final List<String> SUMMARY_VARIABLES_FOR_CURRENT_DATE_EQUALS_2220 = Arrays.asList(
            "02/01/1111 00:05", "1h 30min", "Unhealthy",
            "02/01/1111 01:35", "3h", "Unhealthy",
            "02/01/1111 03:05", "4h 30min", "Optimal",
            "02/01/1111 04:35", "6h", "Optimal",
            "02/01/1111 06:05", "7h 30min", "Healthy",
            "02/01/1111 07:35", "9h", "Healthy",
            "02/01/1111 09:05", "10h 30min", "Not recommended",
            "02/01/1111 10:35", "12h", "Not recommended"
    );

    private static DateTime getDateTime(String string) {
        return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm").parseDateTime(string);
    }

    @Test
    public void testIfCanReturnSummaries() {
        int amountOfUsedItemsOnOneLoopRun = 3;
        int maxIndex = SUMMARY_VARIABLES_FOR_CURRENT_DATE_EQUALS_2220.size() - 1;

        String executionDateString;
        String sleepDurationString;
        String sleepQuality;
        String expected;

        DateTime executionDateWithoutTimeToFallAsleep;

        for (int index = 0; index < maxIndex; index += amountOfUsedItemsOnOneLoopRun) {
            executionDateString = SUMMARY_VARIABLES_FOR_CURRENT_DATE_EQUALS_2220.get(index);
            sleepDurationString = SUMMARY_VARIABLES_FOR_CURRENT_DATE_EQUALS_2220.get(index+1);
            sleepQuality = SUMMARY_VARIABLES_FOR_CURRENT_DATE_EQUALS_2220.get(index+2);

            expected = String.format("%1$s of sleep | %2$s", sleepDurationString, sleepQuality);
            executionDateWithoutTimeToFallAsleep = getDateTime(executionDateString).minusMinutes(ItemsBuilderData.getTimeForFallAsleepInMinutes());

            assertEquals(expected, AlarmContentUtils.getSummary(getDateTime(CURRENT_DATE_STRING), executionDateWithoutTimeToFallAsleep));
        }
    }
}