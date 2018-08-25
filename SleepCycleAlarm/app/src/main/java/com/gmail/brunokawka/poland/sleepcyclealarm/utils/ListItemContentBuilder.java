package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import android.content.Context;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.CustomApp;

public class ListItemContentBuilder {

    public static String getTitle(int hour, int minute) {
        return hour
                + ":"
                + minute
                + (minute == 0 ? minute : "");
    }

    public static String getSummary(int hour, int minute) {
        Context ctx = CustomApp.getContext();
        String itemSummary = ctx.getString(R.string.list_element_summary_changeable);

        return String.format(itemSummary,
                getSleepDurationString(hour, minute), getSleepHealthStatus(hour));
    }

    private static String getSleepDurationString(int hour, int minute) {
        Context ctx = CustomApp.getContext();

        String hourMark = ctx.getString(R.string.global_hour_mark);
        String minuteMark = ctx.getString(R.string.global_minute_mark);

       return hour
               + hourMark
               + (minute <= 0 ? "" : " ")
               + (minute <= 0 ? "" : minute)
               + (minute <= 0 ? "" : minuteMark);
    }

    private static String getSleepHealthStatus(int hoursOfSleep) {
        Context ctx = CustomApp.getContext();

        String stateUnhealthy = ctx.getString(R.string.sleep_state_unhealthy);
        String stateOptimal = ctx.getString(R.string.sleep_state_optimal);
        String stateHealthy = ctx.getString(R.string.sleep_state_healthy);
        String stateNotRecommended = ctx.getString(R.string.sleep_state_notrecommended);

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
