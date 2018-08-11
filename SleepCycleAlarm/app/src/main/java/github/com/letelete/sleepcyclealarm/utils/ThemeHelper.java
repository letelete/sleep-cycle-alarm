package github.com.letelete.sleepcyclealarm.utils;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

public class ThemeHelper {

    private final static String TAG = "ThemeManagerLog";
    private SharedPreferences sharedPreferences;

    public ThemeHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public int getCurrentTheme(String themeKeyString) {
        int currentTheme;

        String selectedThemeOptionId = sharedPreferences.getString(themeKeyString, "1");
        switch(selectedThemeOptionId) {
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
                Log.wtf(TAG, "Default case in setAppTheme switch. selectedThemeOptionId = " + selectedThemeOptionId);
                currentTheme = AppCompatDelegate.MODE_NIGHT_NO;
                break;
        }

        return currentTheme;
    }
}