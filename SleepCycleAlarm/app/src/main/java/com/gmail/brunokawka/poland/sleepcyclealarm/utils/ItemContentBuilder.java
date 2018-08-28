package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import android.content.Context;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.CustomApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ItemContentBuilder {

    private static final String TAG = "ItemContentBuilderLog";

    private static String getString(int resourceId) {
        Context ctx = CustomApp.getContext();
        return ctx.getString(resourceId);
    }

    public static String getTitle(int executionHour, int executionMinute) {
        return getFormattedTime(executionHour, executionMinute);
    }

    public static String getSummary(int currentHour, int currentMinute, int executionHour, int executionMinute) {
        String itemSummary = getString(R.string.list_element_summary_changeable);

        String currentTimeFormatted = getFormattedTime(currentHour, currentMinute);
        String executeTimeFormatted = getFormattedTime(executionHour, executionMinute);
        String dateFormat = "HH:mm";

        if (isDateFormatToCalculateTimeDifferenceNeeded(currentHour, executionHour)) {
            currentTimeFormatted = "01/01/0001 " + currentTimeFormatted;
            executeTimeFormatted = "01/02/0001 " + executeTimeFormatted;
            dateFormat = "MM/dd/yyyy " + dateFormat;
        }

        long difference = getTimeDifference(dateFormat, currentTimeFormatted, executeTimeFormatted);
        long diffHours = getHour(difference);
        long diffMinutes = getMinute(difference);

        return String.format(itemSummary,
                getSleepDurationString(diffHours, diffMinutes), getSleepHealthStatus(diffHours));
    }

    private static String getFormattedTime(int hour, int minute) {
        return getFormattedHourOrMinute(hour)
                + ":"
                + getFormattedHourOrMinute(minute);

    }

    private static long getTimeDifference(String dateFormat, String currentTime, String executeTime) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.UK);

        try {
            Date currentDate = format.parse(currentTime);
            Date executionDate = format.parse(executeTime);
            return executionDate.getTime() - currentDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static long getHour(long difference) {
        return difference / (60 * 60 * 1000) % 24;
    }

    private static long getMinute(long difference) {
        return difference / (60 * 1000) % 60;
    }

    private static boolean isDateFormatToCalculateTimeDifferenceNeeded(int currentHour, int executionHour) {
        return currentHour > executionHour;
    }

    private static String getFormattedHourOrMinute(int hourOrMinute) {
        return hourOrMinute < 10 ? "0" + String.valueOf(hourOrMinute) : String.valueOf(hourOrMinute);
    }

    private static String getSleepDurationString(long hour, long minute) {


        String hourMark = getString(R.string.global_hour_mark);
        String minuteMark = getString(R.string.global_minute_mark);

       return hour
               + hourMark
               + (minute <= 0 ? "" : " ")
               + (minute <= 0 ? "" : minute)
               + (minute <= 0 ? "" : minuteMark);
    }

    private static String getSleepHealthStatus(long hoursOfSleep) {


        String stateUnhealthy = getString(R.string.sleep_state_unhealthy);
        String stateOptimal = getString(R.string.sleep_state_optimal);
        String stateHealthy = getString(R.string.sleep_state_healthy);
        String stateNotRecommended = getString(R.string.sleep_state_notrecommended);

        if(hoursOfSleep < 4) {
            return stateUnhealthy;
        } else if (hoursOfSleep < 7) {
            return stateOptimal;
        } else if (hoursOfSleep <= 9) {
            return stateHealthy;
        } else {
            return stateNotRecommended;
        }
    }
}
