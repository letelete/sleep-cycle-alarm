package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.SleepNowStrategy;


// This class uses the following naming convention:
// test[Feature being tested]

import com.gmail.brunokawka.poland.sleepcyclealarm.TestsHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.SleepNowBuildingStrategy;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class isPossibleToCreateNextItemTests {

    private ItemsBuilder itemsBuilder;

    @Before
    public void setUp() {
        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new SleepNowBuildingStrategy());
    }

    @Test
    public void testIfReturnsTrue() {
        String currentDate = "04/02/2011 18:30";
        String executionDate = "04/02/2011 23:00";

        assertTrue(itemsBuilder.isPossibleToCreateNextItem(
                TestsHelper.getDateTimeFromString(currentDate),
                TestsHelper.getDateTimeFromString(executionDate)));
    }

}
