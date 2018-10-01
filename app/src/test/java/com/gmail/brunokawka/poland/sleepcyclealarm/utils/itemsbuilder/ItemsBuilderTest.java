package com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ItemsBuilderTest {

    private ItemsBuilder itemsBuilder;

    private static DateTime getDateTime(String string) {
        return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm").parseDateTime(string);
    }

    @Before
    public void setUp() {
        itemsBuilder = new ItemsBuilder();
    }

    @Test
    public void testIfCanReturnNextAlarmDateForSleepNowStrategy() {
        itemsBuilder.setBuildingStrategy(new SleepNowBuildingStrategy());

        assertEquals(getDateTime("05/02/2011 02:30"), itemsBuilder.getNextAlarmDate(getDateTime("05/02/2011 01:00")));
        assertEquals(getDateTime("06/02/2011 01:25"), itemsBuilder.getNextAlarmDate(getDateTime("05/02/2011 23:55")));
        assertEquals(getDateTime("05/02/2011 00:00"), itemsBuilder.getNextAlarmDate(getDateTime("04/02/2011 22:30")));
    }

    @Test
    public void testIfCanReturnNextAlarmDateForWakeUpAtStrategy() {
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());

        assertEquals(getDateTime("04/02/2011 23:30"), itemsBuilder.getNextAlarmDate(getDateTime("05/02/2011 01:00")));
        assertEquals(getDateTime("05/02/2011 13:30"), itemsBuilder.getNextAlarmDate(getDateTime("05/02/2011 15:00")));
        assertEquals(getDateTime("04/02/2011 22:32"), itemsBuilder.getNextAlarmDate(getDateTime("05/02/2011 00:02")));
    }

    @Test
    public void testIfIsPossibleToCreateNextItemForSleepNowStrategy() {
        itemsBuilder.setBuildingStrategy(new SleepNowBuildingStrategy());
        assertTrue(itemsBuilder.isPossibleToCreateNextItem(getDateTime("05/02/2011 22:32"), getDateTime("05/02/2011 00:02")));
    }

    @Test
    public void testIfIsPossibleToCreateNextItemForWakeUpAtBuildingStrategy() {
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());
        assertTrue(itemsBuilder.isPossibleToCreateNextItem(getDateTime("04/02/2011 18:30"), getDateTime("04/02/2011 23:00")));
        assertTrue(itemsBuilder.isPossibleToCreateNextItem(getDateTime("04/02/2011 20:30"), getDateTime("05/02/2011 20:20")));
        assertTrue(itemsBuilder.isPossibleToCreateNextItem(getDateTime("04/02/2011 00:30"), getDateTime("04/02/2011 02:30")));
        assertTrue(itemsBuilder.isPossibleToCreateNextItem(getDateTime("04/02/2011 11:54"), getDateTime("04/02/2011 14:54")));
    }

    @Test
    public void testIfItsNotPossibleToCreateNextItemForWakeUpAtBuildingStrategy() {
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());
        assertFalse(itemsBuilder.isPossibleToCreateNextItem(getDateTime("04/02/2011 20:30"), getDateTime("04/02/2011 20:20")));
        assertFalse(itemsBuilder.isPossibleToCreateNextItem(getDateTime("04/02/2011 23:53"), getDateTime("05/02/2011 00:53")));
    }

    @Test
    public void testIfCanReturnEmptyListWhenItsNotPossibleToCreateNextItemForWakeUpAtBuildingStrategy() {
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());
        List<Item> returnedList = itemsBuilder.getItems(getDateTime("04/02/2011 20:00"), getDateTime("04/02/2011 21:00"));
        assertTrue(returnedList.isEmpty());
        assertNotNull(returnedList);
    }
}