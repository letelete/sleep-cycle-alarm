package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.WakeUpAtStrategy;

import com.gmail.brunokawka.poland.sleepcyclealarm.TestsHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.WakeUpAtBuildingStrategy;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

// This class uses the following naming convention:
// test[Functionality]

public class getItemsTests {

    private ItemsBuilder itemsBuilder;

    private DateTime currentDate = TestsHelper.getDateTimeFromString("02/01/2011 08:00");
    private DateTime executionDate = TestsHelper.getDateTimeFromString("02/01/2011 23:00");
    private DateTime dateToGoToSleep = null;
    private ArrayList<Item> items;

    private List<String> executionHoursForCurrentHourEquals2300 = Arrays.asList(
            "02/01/2011 10:45",
            "02/01/2011 12:15",
            "02/01/2011 13:45",
            "02/01/2011 15:15",
            "02/01/2011 16:45",
            "02/01/2011 18:15",
            "02/01/2011 19:45",
            "02/01/2011 21:15"
    );

    @Before
    public void setUp() {
        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());
        items = itemsBuilder.getItems(currentDate, executionDate);
    }

    @Test
    public void testIfVariablesInitialized() {
        assertNotEquals(null, currentDate);
        assertNotEquals(null, executionDate);
        assertNotEquals(null, itemsBuilder);
        assertNotEquals(null, items);
    }

    @Test
    public void testIfCanReturnDatesToGoToSleep() {
        for (int index = 0; index < executionHoursForCurrentHourEquals2300.size(); index++) {
            dateToGoToSleep = TestsHelper.getDateTimeFromString(executionHoursForCurrentHourEquals2300.get(index));
            assertEquals(dateToGoToSleep, items.get(index).getCurrentDate());
        }
    }

    @Test
    public void testIfCanReturnExecutionDates() {
        for (int index = 0; index < executionHoursForCurrentHourEquals2300.size(); index++) {
            assertEquals(executionDate, items.get(index).getExecutionDate());
        }
    }

    @Test
    public void testIfCanReturnCorrectTitles() {
        for (int index = 0; index < executionHoursForCurrentHourEquals2300.size(); index++) {
            dateToGoToSleep = TestsHelper.getDateTimeFromString(executionHoursForCurrentHourEquals2300.get(index));
            String expected = ItemContentBuilder.getTitle(dateToGoToSleep);

            assertEquals(expected, items.get(index).getTitle());
        }
    }

    @Test
    public void testIfCanReturnCorrectSummaries() {
        for (int index = 0; index < executionHoursForCurrentHourEquals2300.size(); index++) {
            dateToGoToSleep = TestsHelper.getDateTimeFromString(executionHoursForCurrentHourEquals2300.get(index));
            String expected = ItemContentBuilder.getSummary(dateToGoToSleep, executionDate.minusMinutes(ItemsBuilderData.getTimeForFallAsleepInMinutes()));

            assertEquals(expected, items.get(index).getSummary());
        }
    }
}
