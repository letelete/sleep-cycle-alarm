package com.gmail.brunokawka.poland.sleepcyclealarm;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

public class TestsHelper {
    public static DateTime getDateTimeFromString(String executionHour) {
        return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm").parseDateTime(executionHour);
    }
}
