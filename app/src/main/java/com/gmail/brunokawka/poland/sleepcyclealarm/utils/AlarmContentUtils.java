package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import android.content.Context;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.app.CustomApp;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

public class AlarmContentUtils {

    private static String getString(int resourceId) {
        Context ctx = CustomApp.getContext();
        return ctx.getString(resourceId);
    }

    public static String getTitle(String executionDateString) {
        DateTime executionDate = DateTime.parse(executionDateString);
        return getTitle(executionDate);
    }

    public static String getTitle(DateTime executionDate) {
        return getFormattedTime(executionDate);
    }

    public static String getTitleForWakeUpAt(DateTime timeToGoToSleep, DateTime executionDate) {
        return new StringBuilder().append(getFormattedTime(timeToGoToSleep))
                .append(" -> ")
                .append(getFormattedTime(executionDate))
                .toString();
    }

    public static DateTime getDateConvertedToSimple(DateTime date) {
        return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm").parseDateTime(
                date.getDayOfMonth() + "/"
                        + date.getMonthOfYear() + "/"
                        + date.getYear() + " "
                        + date.getHourOfDay()
                        + ":" + date.getMinuteOfHour());
    }


    public static String getSummary(DateTime currentDate, DateTime executionDate) {
        String itemSummary = getString(R.string.list_element_summary_changeable);

        Period periodOfTime = getPeriodOfTime(currentDate, executionDate);
        long diffHours = periodOfTime.getHours();
        long diffMinutes = periodOfTime.getMinutes();

        return String.format(itemSummary,
                getSleepDurationString(diffHours, diffMinutes), getSleepHealthStatus(diffHours));
    }

    private static String getFormattedTime(DateTime date) {
        long hour = date.getHourOfDay();
        long minute = date.getMinuteOfHour();
        return getFormattedHourOrMinute(hour)
                + ":"
                + getFormattedHourOrMinute(minute);
    }

    private static Period getPeriodOfTime(DateTime currentDate, DateTime executionDate) {
        return new Period(currentDate, executionDate);
    }

    private static String getFormattedHourOrMinute(long hourOrMinute) {
        return hourOrMinute < 10
                ? "0" + String.valueOf(hourOrMinute)
                : String.valueOf(hourOrMinute);
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
