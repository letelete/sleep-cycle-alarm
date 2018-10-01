package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import android.support.v7.app.AppCompatDelegate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ThemeCoordinatorTest {

    @Test
    public void testIfCanReturnCurrentTheme() {
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, ThemeCoordinator.getCurrentTheme("1"));
        assertEquals(AppCompatDelegate.MODE_NIGHT_AUTO, ThemeCoordinator.getCurrentTheme("2"));
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, ThemeCoordinator.getCurrentTheme("3"));
    }

    @Test
    public void testIfCanReturnDefaultMode() {
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, ThemeCoordinator.getCurrentTheme("0"));
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, ThemeCoordinator.getCurrentTheme("4"));
    }
}