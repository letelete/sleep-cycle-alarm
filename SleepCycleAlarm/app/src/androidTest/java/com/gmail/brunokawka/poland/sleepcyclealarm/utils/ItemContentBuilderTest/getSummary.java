package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilderTest;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/*
    Naming convention:
    methodName_StateUnderTest_ExpectedBehavior
 */

@RunWith(MockitoJUnitRunner.class)
public class getSummary {

    @Test
    public void getSummary_10and30and12and0_1h30minOfSleepUnhealthy() {
        int currentHour = 10;
        int currentMinute = 30;
        int executionHour = 12;
        int executionMinute = 0;

        String sleepDuration = "1h 30min";
        String sleepQuality = "Unhealthy";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        assertEquals(expected, ItemContentBuilder.getSummary(currentHour, currentMinute, executionHour, executionMinute));
    }

    @Test
    public void getSummary_2and0and8and0_6hOfSleepHealthy() {
        int currentHour = 2;
        int currentMinute = 0;
        int executionHour = 8;
        int executionMinute = 0;

        String sleepDuration = "6h";
        String sleepQuality = "Optimal";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        assertEquals(expected, ItemContentBuilder.getSummary(currentHour, currentMinute, executionHour, executionMinute));
    }

    @Test
    public void getSummary_23and0and8and30_9h30minOfSleepHealthy() {
        int currentHour = 23;
        int currentMinute = 0;
        int executionHour = 8;
        int executionMinute = 30;

        String sleepDuration = "9h 30min";
        String sleepQuality = "Healthy";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        assertEquals(expected, ItemContentBuilder.getSummary(currentHour, currentMinute, executionHour, executionMinute));
    }

    @Test
    public void getSummary_1and30and12and30_11hOfSleepHealthy() {
        int currentHour = 1;
        int currentMinute = 30;
        int executionHour = 12;
        int executionMinute = 30;

        String sleepDuration = "11h";
        String sleepQuality = "Not recommended";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        assertEquals(expected, ItemContentBuilder.getSummary(currentHour, currentMinute, executionHour, executionMinute));
    }
}
