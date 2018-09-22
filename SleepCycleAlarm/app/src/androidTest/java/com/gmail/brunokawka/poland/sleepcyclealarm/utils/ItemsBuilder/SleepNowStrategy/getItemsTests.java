package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.SleepNowStrategy;

import com.gmail.brunokawka.poland.sleepcyclealarm.TestsHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.SleepNowBuildingStrategy;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class getItemsTests {

    private ItemsBuilder itemsBuilder;
    private DateTime currentDate;
    private ArrayList<Item> items;

    private List<String> executionHoursForCurrentHourEquals2220 = Arrays.asList(
            "02/01/1111 00:05",
            "02/01/1111 01:35",
            "02/01/1111 03:05",
            "02/01/1111 04:35",
            "02/01/1111 06:05",
            "02/01/1111 07:35",
            "02/01/1111 09:05",
            "02/01/1111 10:35"
    );

    @Before
    public void setUp() {
        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new SleepNowBuildingStrategy());

        String currentDateString = "01/01/1111 22:20";
        currentDate = TestsHelper.getDateTimeFromString(currentDateString);

        items =  itemsBuilder.getItems(currentDate, null);
    }

    @Test
    public void areVariablesInitialized() {
        assertNotEquals(null, currentDate);
        assertNotEquals(null, itemsBuilder);
        assertNotEquals(null, items);
    }

    @Test
    public void testIfCanReturnCurrentDates() {
        for (int index = 0; index < executionHoursForCurrentHourEquals2220.size(); index++) {
            assertEquals(currentDate, items.get(index).getCurrentDate());
        }
    }

    @Test
    public void testIfCanReturnExecutionDates() {
        DateTime executionDate;
        for (int index = 0; index < executionHoursForCurrentHourEquals2220.size(); index++) {
            executionDate = TestsHelper.getDateTimeFromString(executionHoursForCurrentHourEquals2220.get(index));
            assertEquals(executionDate, items.get(index).getExecutionDate());
        }
    }

    @Test
    public void testIfCanReturnCorrectTitles() {
        for (int index = 0; index < executionHoursForCurrentHourEquals2220.size(); index++) {
            DateTime executionDate = TestsHelper.getDateTimeFromString(executionHoursForCurrentHourEquals2220.get(index));
            String expected = ItemContentBuilder.getTitle(executionDate);

            assertEquals(expected, items.get(index).getTitle());
        }
    }

    @Test
    public void testIfCanReturnCorrectSummaries() {
        for (int index = 0; index < executionHoursForCurrentHourEquals2220.size(); index++) {
            DateTime executionDate = TestsHelper.getDateTimeFromString(executionHoursForCurrentHourEquals2220.get(index));
            String expected = ItemContentBuilder.getSummary(currentDate, executionDate.minusMinutes(ItemsBuilderData.getTimeForFallAsleepInMinutes()));

            assertEquals(expected, items.get(index).getSummary());
        }
    }
}
