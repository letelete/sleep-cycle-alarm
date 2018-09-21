package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.SleepNowStrategy;

import com.gmail.brunokawka.poland.sleepcyclealarm.TestsHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.SleepNowBuildingStrategy;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class getItemsTests {

    private ItemsBuilder itemsBuilder;

    private List<String> executionHoursForCurrentHourEquals1200 = Arrays.asList(
            "01/01/1111 13:45",
            "01/01/1111 15:15",
            "01/01/1111 16:45",
            "01/01/1111 18:15",
            "01/01/1111 19:45",
            "01/01/1111 21:15",
            "02/01/1111 22:45",
            "02/01/1111 00:15 "
    );

    @Before
    public void setUp() {
        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new SleepNowBuildingStrategy());
    }

    @Test
    public void testIfCanReturnCurrentDates() {
        String currentDateString = "01/01/1111 12:00";
        for (int index = 0; index < executionHoursForCurrentHourEquals1200.size(); index++) {
            DateTime currentDate = TestsHelper.getDateTimeFromString(currentDateString);

            assertEquals(currentDate, itemsBuilder
                    .getItems(currentDate, null).get(index).getCurrentDate());
        }
    }

    @Test
    public void testIfCanReturnExecutionDates() {
        String currentDateString = "01/01/1111 12:00";
        for (int index = 0; index < executionHoursForCurrentHourEquals1200.size(); index++) {
            String executionDateString = executionHoursForCurrentHourEquals1200.get(index);

            DateTime currentDate = TestsHelper.getDateTimeFromString(currentDateString);

            DateTime expected = TestsHelper.getDateTimeFromString(executionDateString);

            assertEquals(expected, itemsBuilder.getItems(currentDate, null).get(index).getExecutionDate());
        }
    }

    @Test
    public void testIfCanReturnCorrectTitles() {
        String currentDateString = "01/01/1111 12:00";
        for (int index = 0; index < executionHoursForCurrentHourEquals1200.size(); index++) {
            String executionDateString = executionHoursForCurrentHourEquals1200.get(index);

            DateTime executionDate = TestsHelper.getDateTimeFromString(executionDateString);
            DateTime currentDate = TestsHelper.getDateTimeFromString(currentDateString);

            String expected = ItemContentBuilder.getTitle(executionDate);

            assertEquals(expected, itemsBuilder.getItems(currentDate, null).get(index).getTitle());
        }
    }

    @Test
    public void testIfCanReturnCorrectSummaries() {
        String currentDateString = "01/01/1111 12:00";
        for (int index = 0; index < executionHoursForCurrentHourEquals1200.size(); index++) {
            String executionDateString = executionHoursForCurrentHourEquals1200.get(index);

            DateTime executionDate = TestsHelper.getDateTimeFromString(executionDateString);
            DateTime currentDate = TestsHelper.getDateTimeFromString(currentDateString);


            String expected = ItemContentBuilder.getSummary(currentDate, executionDate.minusMinutes(ItemsBuilderData.getTimeForFallAsleepInMinutes()));

            assertEquals(expected, itemsBuilder.getItems(currentDate, null).get(index).getSummary());
        }
    }
}
