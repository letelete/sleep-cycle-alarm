package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import android.support.v7.app.AppCompatDelegate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ThemeUtilsTest {

    @Test
    public void testIfCanReturnCurrentTheme() {
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, ThemeUtils.getCurrentTheme("1"));
        assertEquals(AppCompatDelegate.MODE_NIGHT_AUTO, ThemeUtils.getCurrentTheme("2"));
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, ThemeUtils.getCurrentTheme("3"));
    }

    @Test
    public void testIfCanReturnDefaultMode() {
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, ThemeUtils.getCurrentTheme("0"));
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, ThemeUtils.getCurrentTheme("4"));
    }
}