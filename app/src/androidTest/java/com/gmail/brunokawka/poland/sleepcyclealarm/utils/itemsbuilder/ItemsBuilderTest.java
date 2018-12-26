package com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.ItemsBuilderData;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.AlarmContentUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class ItemsBuilderTest {
    private ItemsBuilder itemsBuilder;

    private final DateTime CURRENT_DATE_FOR_SLEEPNOW_BUILDING_STRATEGY = getDateTime("01/01/1111 22:20");
    private final List<String> EXPECTED_FOR_SLEEPNOW_BUILDING_STRATEGY = Arrays.asList(
            "02/01/1111 00:05",
            "02/01/1111 01:35",
            "02/01/1111 03:05",
            "02/01/1111 04:35",
            "02/01/1111 06:05",
            "02/01/1111 07:35",
            "02/01/1111 09:05",
            "02/01/1111 10:35"
    );
    private List<Item> itemsForSleepNowBuildingStrategy;

    private final DateTime CURRENT_DATE_FOR_WAKEUPAT_BUILDING_STRATEGY = getDateTime("02/01/2011 08:00");
    private final DateTime EXECUTION_DATE_FOR_WAKEUPAT_BUILDING_STRATEGY = getDateTime("02/01/2011 23:00");
    private final List<String> EXPECTED_FOR_WAKEUPAT_BUILDING_STRATEGY = Arrays.asList(
            "02/01/2011 10:45",
            "02/01/2011 12:15",
            "02/01/2011 13:45",
            "02/01/2011 15:15",
            "02/01/2011 16:45",
            "02/01/2011 18:15",
            "02/01/2011 19:45",
            "02/01/2011 21:15"
    );
    private List<Item> itemsForWakeUpAtBuildingStrategy;


    private static DateTime getDateTime(String string) {
        return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm").parseDateTime(string);
    }

    @Before
    public void setUp() {
        itemsBuilder = new ItemsBuilder();

        itemsBuilder.setBuildingStrategy(new SleepNowBuildingStrategy());
        itemsForSleepNowBuildingStrategy = itemsBuilder.getItems(CURRENT_DATE_FOR_SLEEPNOW_BUILDING_STRATEGY, null);

        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());
        itemsForWakeUpAtBuildingStrategy = itemsBuilder.getItems(CURRENT_DATE_FOR_WAKEUPAT_BUILDING_STRATEGY, EXECUTION_DATE_FOR_WAKEUPAT_BUILDING_STRATEGY);
    }

    @Test
    public void testIfVariablesInitialized() {
        assertNotEquals(null, itemsBuilder);
        assertNotEquals(null, itemsForSleepNowBuildingStrategy);
        assertNotEquals(null, itemsForWakeUpAtBuildingStrategy);
    }

    @Test
    public void testIfCanReturnCurrentDatesForSleepNowBuildingStrategy() {
        for (int index = 0; index < EXPECTED_FOR_SLEEPNOW_BUILDING_STRATEGY.size(); index++) {
            assertEquals(CURRENT_DATE_FOR_SLEEPNOW_BUILDING_STRATEGY, itemsForSleepNowBuildingStrategy.get(index).getCurrentDate());
        }
    }

    @Test
    public void testIfCanReturnCurrentDatesForWakeUpAtBuildingStrategy() {
        DateTime expected;
        for (int index = 0; index < EXPECTED_FOR_WAKEUPAT_BUILDING_STRATEGY.size(); index++) {
            expected = getDateTime(EXPECTED_FOR_WAKEUPAT_BUILDING_STRATEGY.get(index));
            assertEquals(expected, itemsForWakeUpAtBuildingStrategy.get(index).getCurrentDate());
        }
    }

    @Test
    public void testIfCanReturnExecutionDatesForSleepNowBuildingStrategy() {
        DateTime expected;
        for (int index = 0; index < EXPECTED_FOR_SLEEPNOW_BUILDING_STRATEGY.size(); index++) {
            expected = getDateTime(EXPECTED_FOR_SLEEPNOW_BUILDING_STRATEGY.get(index));
            assertEquals(expected, itemsForSleepNowBuildingStrategy.get(index).getExecutionDate());
        }
    }

    @Test
    public void testIfCanReturnExecutionDatesForWakeUpAtBuildingStrategy() {
        for (int index = 0; index < EXPECTED_FOR_WAKEUPAT_BUILDING_STRATEGY.size(); index++) {
            assertEquals(EXECUTION_DATE_FOR_WAKEUPAT_BUILDING_STRATEGY, itemsForWakeUpAtBuildingStrategy.get(index).getExecutionDate());
        }
    }

    @Test
    public void testIfCanReturnCorrectTitlesForSleepNowBuildingStrategy() {
        DateTime executionDate;
        String expected;
        for (int index = 0; index < EXPECTED_FOR_SLEEPNOW_BUILDING_STRATEGY.size(); index++) {
            executionDate = getDateTime(EXPECTED_FOR_SLEEPNOW_BUILDING_STRATEGY.get(index));
            expected = AlarmContentUtils.getTitle(executionDate);

            assertEquals(expected, itemsForSleepNowBuildingStrategy.get(index).getTitle());
        }
    }

    @Test
    public void testIfCanReturnCorrectTitlesForWakeUpAtBuildingStrategy() {
        String expected;
        for (int index = 0; index < EXPECTED_FOR_WAKEUPAT_BUILDING_STRATEGY.size(); index++) {
            expected = AlarmContentUtils.getTitleForWakeUpAt(getDateTime(EXPECTED_FOR_WAKEUPAT_BUILDING_STRATEGY.get(index)),
                    EXECUTION_DATE_FOR_WAKEUPAT_BUILDING_STRATEGY);
            assertThat(expected, is(itemsForWakeUpAtBuildingStrategy.get(index).getTitle()));
        }
    }

    @Test
    public void testIfCanReturnCorrectSummariesForSleepNowBuildingStrategy() {
        DateTime expectedDate;
        String expected;
        for (int index = 0; index < EXPECTED_FOR_SLEEPNOW_BUILDING_STRATEGY.size(); index++) {
            expectedDate = getDateTime(EXPECTED_FOR_SLEEPNOW_BUILDING_STRATEGY.get(index));
            expected = AlarmContentUtils.getSummary(CURRENT_DATE_FOR_SLEEPNOW_BUILDING_STRATEGY, expectedDate.minusMinutes(ItemsBuilderData.getTimeForFallAsleepInMinutes()));

            assertEquals(expected, itemsForSleepNowBuildingStrategy.get(index).getSummary());
        }
    }

    @Test
    public void testIfCanReturnCorrectSummariesForWakeUpAtBuildingStrategy() {
        DateTime expectedDate;
        String expected;
        DateTime executionDateWithoutTimeToFallAsleep;
        for (int index = 0; index < EXPECTED_FOR_WAKEUPAT_BUILDING_STRATEGY.size(); index++) {
            expectedDate = getDateTime(EXPECTED_FOR_WAKEUPAT_BUILDING_STRATEGY.get(index));
            executionDateWithoutTimeToFallAsleep = EXECUTION_DATE_FOR_WAKEUPAT_BUILDING_STRATEGY.minusMinutes(ItemsBuilderData.getTimeForFallAsleepInMinutes());
            expected = AlarmContentUtils.getSummary(expectedDate, executionDateWithoutTimeToFallAsleep);

            assertEquals(expected, itemsForWakeUpAtBuildingStrategy.get(index).getSummary());
        }
    }
}