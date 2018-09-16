package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.sleepnow.SleepNowItemsBuilderTest;

import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.sleepnow.SleepNowItemsBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

// This class uses the following naming convention:
// test[Feature being tested]

@RunWith(MockitoJUnitRunner.class)
public class getItemsForCurrentDateTests {

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void testIfCanReturnCurrentDate() {
        String currentHour = "01/01/1111 12:00";
        int itemIndex = 6;

        DateTime dateCurrent = getDateTimeFormatted(currentHour);
        assertEquals(dateCurrent, SleepNowItemsBuilder.getItemsForCurrentDate(dateCurrent).get(itemIndex).getCurrentDate());
    }

    @Test
    public void testIfCanReturnExecutionDate() {
        String currentHour = "01/01/1111 12:00";
        String executionHour = "01/01/1111 15:15"; // +15 min for time to fall asleep
        int itemIndex = 1;

        DateTime dateCurrent = getDateTimeFormatted(currentHour);
        DateTime dateExecution = getDateTimeFormatted(executionHour);
        assertEquals(dateExecution, SleepNowItemsBuilder.getItemsForCurrentDate(dateCurrent).get(itemIndex).getExecutionDate());
    }
}
