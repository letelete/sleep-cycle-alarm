package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.ItemBuilderTests;

import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.ItemBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/*
    Naming convention:
    methodName_StateUnderTest_ExpectedBehavior
 */

@RunWith(MockitoJUnitRunner.class)
public class getItemsForCurrentDateTests {

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void getItemsForCurrentDate_12and00_fifthItemCurrentDateEquals12and00() {
        String currentHour = "01/01/1111 12:00";
        int itemIndex = 6;

        DateTime dateCurrent = getDateTimeFormatted(currentHour);
        assertEquals(dateCurrent, ItemBuilder.getItemsForCurrentDate(dateCurrent).get(itemIndex).getCurrentDate());
    }

    @Test
    public void getItemsForCurrentDate_12and00_secondItemExecutionDateEquals15and15() {
        String currentHour = "01/01/1111 12:00";
        String executionHour = "01/01/1111 15:15"; // +15 min for time to fall asleep
        int itemIndex = 1;

        DateTime dateCurrent = getDateTimeFormatted(currentHour);
        DateTime dateExecution = getDateTimeFormatted(executionHour);
        assertEquals(dateExecution, ItemBuilder.getItemsForCurrentDate(dateCurrent).get(itemIndex).getExecutionDate());
    }
}
