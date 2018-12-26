package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TimeUtilsRounderTest {

    private static DateTime getDateTime(String string) {
        return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm").parseDateTime(string);
    }

    @Test
    public void testIfCanRoundToFullHour() {
        assertEquals(getDateTime("01/01/2018 19:00"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 18:58")));
        assertEquals(getDateTime("01/01/2018 20:00"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 19:59")));
        assertEquals(getDateTime("01/01/2018 21:00"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 21:01")));
        assertEquals(getDateTime("01/01/2018 22:00"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 22:02")));
        assertEquals(getDateTime("02/01/2018 00:00"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 23:58")));
        assertEquals(getDateTime("01/01/2018 00:00"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 00:02")));
    }

    @Test
    public void testIfWillNotRoundToFullHour() {
        assertNotEquals(getDateTime("01/01/2018 20:00"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:03")));
        assertNotEquals(getDateTime("01/01/2018 21:00"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:57")));
    }

    @Test
    public void testIfCanRoundToHalfOfTen() {
        assertEquals(getDateTime("01/01/2018 17:45"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 17:43")));
        assertEquals(getDateTime("01/01/2018 18:45"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 18:44")));
        assertEquals(getDateTime("01/01/2018 19:45"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 19:46")));
        assertEquals(getDateTime("01/01/2018 00:45"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 00:47")));
    }

    @Test
    public void testIfWillNotRoundToHalfOfTen() {
        assertNotEquals(getDateTime("01/01/2018 20:35"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:32")));
        assertNotEquals(getDateTime("01/01/2018 20:45"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:49")));
    }

    @Test
    public void testIfCanRoundToClosestTen() {
        assertEquals(getDateTime("01/01/2018 20:10"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:08")));
        assertEquals(getDateTime("01/01/2018 20:10"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:11")));
        assertEquals(getDateTime("01/01/2018 20:20"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:22")));
        assertEquals(getDateTime("01/01/2018 20:30"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:28")));
        assertEquals(getDateTime("01/01/2018 20:40"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:39")));
    }

    @Test
    public void testIfWillNotRoundToClosestTen() {
        assertNotEquals(getDateTime("01/01/2018 20:40"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:43")));
        assertNotEquals(getDateTime("01/01/2018 20:50"), TimeUtils.getClosestTimeRound(getDateTime("01/01/2018 20:47")));
    }
}