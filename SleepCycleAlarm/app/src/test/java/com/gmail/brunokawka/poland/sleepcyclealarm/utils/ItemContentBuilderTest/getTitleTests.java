package com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilderTest;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/*
    Naming convention:
    methodName_StateUnderTest_ExpectedBehavior
 */

public class getTitleTests {

    @Test
    public void getTitle_6and30_6Colon30() {
        String expected = "06:30";
        int executionHour = 6;
        int executionMinute = 30;
        assertEquals(expected, ItemContentBuilder.getTitle(executionHour, executionMinute));
    }

    @Test
    public void getTitle_0and3_00Colon03() {
        String expected = "00:03";
        int executionHour = 0;
        int executionMinute = 3;
        assertEquals(expected, ItemContentBuilder.getTitle(executionHour, executionMinute));
    }

    @Test
    public void getTitle_23and59_23Colon59() {
        String expected = "23:59";
        int executionHour = 23;
        int executionMinute = 59;
        assertEquals(expected, ItemContentBuilder.getTitle(executionHour, executionMinute));
    }

    @Test
    public void getTitle_12and3_12Colon03() {
        String expected = "12:03";
        int executionHour = 12;
        int executionMinute = 3;
        assertEquals(expected, ItemContentBuilder.getTitle(executionHour, executionMinute));
    }

    @Test
    public void getTitle_0and0_00Colon00() {
        String expected = "00:00";
        int executionHour = 0;
        int executionMinute = 0;
        assertEquals(expected, ItemContentBuilder.getTitle(executionHour, executionMinute));
    }
}
