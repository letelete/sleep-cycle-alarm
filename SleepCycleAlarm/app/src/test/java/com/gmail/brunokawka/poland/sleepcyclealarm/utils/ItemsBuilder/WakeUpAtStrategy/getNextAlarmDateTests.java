package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.WakeUpAtStrategy;

import com.gmail.brunokawka.poland.sleepcyclealarm.TestsHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.WakeUpAtBuildingStrategy;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// This class uses the following naming convention:
// test[Feature being tested]

public class getNextAlarmDateTests {

    private ItemsBuilder itemsBuilder;

    @Before
    public void setUp() {
        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());
    }

    @Test
    public void testIfCanReturnNextAlarmDate() {
        String nextAlarmDate = "04/02/2011 23:15";
        String executionDate = "05/02/2011 01:00";

        assertEquals(TestsHelper.getDateTimeFromString(nextAlarmDate), itemsBuilder
                .getNextAlarmDate(TestsHelper.getDateTimeFromString(executionDate)));
    }

    @Test
    public void testIfCanReturnNextAlarmDate2() {
        String nextAlarmDate = "04/02/2011 13:15";
        String executionDate = "04/02/2011 15:00";

        assertEquals(TestsHelper.getDateTimeFromString(nextAlarmDate), itemsBuilder
                .getNextAlarmDate(TestsHelper.getDateTimeFromString(executionDate)));
    }

    @Test
    public void testIfCanReturnNextAlarmDate3() {
        String nextAlarmDate = "04/02/2011 22:17";
        String executionDate = "05/02/2011 00:02";

        assertEquals(TestsHelper.getDateTimeFromString(nextAlarmDate), itemsBuilder
                .getNextAlarmDate(TestsHelper.getDateTimeFromString(executionDate)));
    }
}
