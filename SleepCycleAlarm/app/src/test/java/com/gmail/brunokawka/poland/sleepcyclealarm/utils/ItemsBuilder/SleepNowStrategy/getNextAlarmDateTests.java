package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.SleepNowStrategy;

import com.gmail.brunokawka.poland.sleepcyclealarm.TestsHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.SleepNowBuildingStrategy;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// This class uses the following naming convention:
// test[Feature being tested]

public class getNextAlarmDateTests {

    private ItemsBuilder itemsBuilder;

    @Before
    public void setUp() {
        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new SleepNowBuildingStrategy());
    }

    @Test
    public void testIfCanReturnNextAlarmDate() {
        String nextAlarmDate = "05/02/2011 02:45";
        String executionDate = "05/02/2011 01:00";

        assertEquals(TestsHelper.getDateTimeFromString(nextAlarmDate), itemsBuilder
                .getNextAlarmDate(TestsHelper.getDateTimeFromString(executionDate)));
    }

    @Test
    public void testIfCanReturnNextAlarmDate2() {
        String nextAlarmDate = "04/02/2011 01:40";
        String executionDate = "03/02/2011 23:55";

        assertEquals(TestsHelper.getDateTimeFromString(nextAlarmDate), itemsBuilder
                .getNextAlarmDate(TestsHelper.getDateTimeFromString(executionDate)));
    }

    @Test
    public void testIfCanReturnNextAlarmDate3() {
        String nextAlarmDate = "05/02/2011 00:00";
        String executionDate = "04/02/2011 22:15";

        assertEquals(TestsHelper.getDateTimeFromString(nextAlarmDate), itemsBuilder
                .getNextAlarmDate(TestsHelper.getDateTimeFromString(executionDate)));
    }
}
