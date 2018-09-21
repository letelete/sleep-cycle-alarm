package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilderTests;

import com.gmail.brunokawka.poland.sleepcyclealarm.TestsHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

// This class uses the following naming convention:
// Should_ExpectedBehavior_When_StateUnderTest

@RunWith(MockitoJUnitRunner.class)
public class getSummaryTest {

    @Test
    public void Should_Return1h30minOfSleepAndUnHealthy_When_SleepDurationEquals1h30min() {
        String currentDateString = "01/01/1111 10:30";
        String executionDateString = "02/01/1111 12:00";
        String sleepDuration = "1h 30min";
        String sleepQuality = "Unhealthy";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        assertEquals(expected, ItemContentBuilder.getSummary(
                        TestsHelper.getDateTimeFromString(currentDateString),
                        TestsHelper.getDateTimeFromString(executionDateString)));
    }

    @Test
    public void Should_Return6hOfSleepAndOptimal_When_SleepDurationEquals6h() {
        String currentDateString = "01/01/1111 02:00";
        String executionDateString = "01/01/1111 08:00";
        String sleepDuration = "6h";
        String sleepQuality = "Optimal";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        assertEquals(expected, ItemContentBuilder.getSummary(
                TestsHelper.getDateTimeFromString(currentDateString),
                TestsHelper.getDateTimeFromString(executionDateString)));
    }

    @Test
    public void Should_Return9h30minOfSleepAndHealthy_When_SleepDurationEquals9h30min() {
        String currentDateString = "01/01/1111 23:00";
        String executionDateString = "02/01/1111 08:30";
        String sleepDuration = "9h 30min";
        String sleepQuality = "Healthy";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        assertEquals(expected, ItemContentBuilder.getSummary(
                TestsHelper.getDateTimeFromString(currentDateString),
                TestsHelper.getDateTimeFromString(executionDateString)));
    }

    @Test
    public void Should_Return11hOfSleepAndNotRecommended_When_SleepDurationEquals11h() {
        String currentDateString = "01/01/1111 01:30";
        String executionDateString = "01/01/1111 12:30";
        String sleepDuration = "11h";
        String sleepQuality = "Not recommended";

        String expected = String.format("%1$s of sleep, %2$s", sleepDuration, sleepQuality);
        assertEquals(expected, ItemContentBuilder.getSummary(
                TestsHelper.getDateTimeFromString(currentDateString),
                TestsHelper.getDateTimeFromString(executionDateString)));
    }
}
