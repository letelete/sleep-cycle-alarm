package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

public class ThemeUtils {

    public static int getCurrentTheme(String themeId) {
        int currentTheme;

        switch(themeId) {
            case "1":
                currentTheme = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case "2":
                currentTheme = AppCompatDelegate.MODE_NIGHT_AUTO;
                break;
            case "3":
                currentTheme = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            default:
                Log.e(ThemeUtils.class.getName(), "Default case in setAppTheme switch");
                currentTheme = AppCompatDelegate.MODE_NIGHT_NO;
                break;
        }

        return currentTheme;
    }
}