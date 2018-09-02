package com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTimeTests;

import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.sleepnow.SleepNowItemBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.RoundTime;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.*;

public class getNearestTests {

    private DateTime getDateTimeFormatted(String executionHour) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        return formatter.parseDateTime(executionHour);
    }

    @Test
    public void roundTime_6and03_6and00() {
        String hourToRound = "04/02/2011 06:03";
        String expected = "04/02/2011 06:00";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void roundTime_6and04_6and00() {
        String hourToRound = "04/02/2011 06:04";
        String expected = "04/02/2011 06:00";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void roundTime_6and34_6and35() {
        String hourToRound = "04/02/2011 06:34";
        String expected = "04/02/2011 06:35";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void roundTime_6and36_6and35() {
        String hourToRound = "04/02/2011 06:36";
        String expected = "04/02/2011 06:35";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void roundTime_6and57_6and55() {
        String hourToRound = "04/02/2011 06:57";
        String expected = "04/02/2011 06:55";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }

    @Test
    public void roundTime_6and59_7and00() {
        String hourToRound = "04/02/2011 06:59";
        String expected = "04/02/2011 07:00";

        DateTime dateToRound = getDateTimeFormatted(hourToRound);
        DateTime dateExpected = getDateTimeFormatted(expected);
        assertEquals(dateExpected, RoundTime.getNearest(dateToRound));
    }
}