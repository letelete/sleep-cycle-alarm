package com.gmail.brunokawka.poland.sleepcyclealarm;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTime;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// This class uses the following naming convention:
// test[Feature being tested]

public class DateTimeTests {

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void testIfIntervalWorksAsIThinkItDoes() {
        String currentDate = "04/02/2011 06:00";
        String executionDate = "04/02/2011 08:00";

        long durationInMinutes = 120;

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedExecutionDate = getDateTimeFormatted(executionDate);
        long oneMinDurationInMillis = 1000 * 60;
        long resultInMinutes = new Interval(formattedCurrentDate, formattedExecutionDate).toDurationMillis() / oneMinDurationInMillis;
        assertEquals(durationInMinutes, resultInMinutes);
    }

    @Test
    public void testIfIntervalWorksAsIThinkItDoes2() {
        String currentDate = "04/02/2011 23:00";
        String executionDate = "05/02/2011 04:00";

        long durationInMinutes = 300;

        DateTime formattedCurrentDate = getDateTimeFormatted(currentDate);
        DateTime formattedExecutionDate = getDateTimeFormatted(executionDate);
        long oneMinDurationInMillis = 1000 * 60;
        long resultInMinutes = new Interval(formattedCurrentDate, formattedExecutionDate).toDurationMillis() / oneMinDurationInMillis;
        assertEquals(durationInMinutes, resultInMinutes);
    }
}
