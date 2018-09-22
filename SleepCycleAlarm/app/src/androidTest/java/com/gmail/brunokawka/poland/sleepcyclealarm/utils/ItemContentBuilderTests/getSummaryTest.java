package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilderTests;

import com.gmail.brunokawka.poland.sleepcyclealarm.TestsHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

// This class uses the following naming convention:
// Should_ExpectedBehavior_When_StateUnderTest

@RunWith(MockitoJUnitRunner.class)
public class getSummaryTest {

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

    @Test
    public void testIfCanReturnSummaries() {
        int amountOfUsedItemsOnOneLoopRun = 3;
        int maxIndex = SUMMARY_VARIABLES_FOR_CURRENT_DATE_EQUALS_2220.size() - 1;

        for (int index = 0; index < maxIndex; index += amountOfUsedItemsOnOneLoopRun) {
            String executionDateString = SUMMARY_VARIABLES_FOR_CURRENT_DATE_EQUALS_2220.get(index);
            String sleepDurationString = SUMMARY_VARIABLES_FOR_CURRENT_DATE_EQUALS_2220.get(index+1);
            String sleepQuality = SUMMARY_VARIABLES_FOR_CURRENT_DATE_EQUALS_2220.get(index+2);

            String expected = String.format("%1$s of sleep, %2$s", sleepDurationString, sleepQuality);
            assertEquals(expected, ItemContentBuilder.getSummary(
                    TestsHelper.getDateTimeFromString(CURRENT_DATE_STRING),
                    TestsHelper.getDateTimeFromString(executionDateString).minusMinutes(ItemsBuilderData.getTimeForFallAsleepInMinutes())));
        }
    }
}
