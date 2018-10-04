package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ItemContentBuilderTest {

    private static DateTime getDateTime(String string) {
        return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm").parseDateTime(string);
    }

    @Test
    public void testIfCanReturnTitle() {
        assertEquals("06:30", ItemContentBuilder.getTitle(getDateTime("04/02/2011 06:30")));
        assertEquals("00:03", ItemContentBuilder.getTitle(getDateTime("04/02/2011 00:03")));
        assertEquals("23:59", ItemContentBuilder.getTitle(getDateTime("04/02/2011 23:59")));
        assertEquals("00:00", ItemContentBuilder.getTitle(getDateTime("04/02/2011 00:00")));
    }

    @Test
    public void testIfCanReturnTitleForWakeUpAt() {
        assertThat("23:00 -> 06:30",
                is(ItemContentBuilder.getTitleForWakeUpAt(getDateTime("01/01/2001 23:00"), getDateTime("02/01/2001 6:30"))));
        assertThat("00:00 -> 01:30",
                is(ItemContentBuilder.getTitleForWakeUpAt(getDateTime("02/01/2001 00:00"), getDateTime("02/01/2001 01:30"))));
    }
}