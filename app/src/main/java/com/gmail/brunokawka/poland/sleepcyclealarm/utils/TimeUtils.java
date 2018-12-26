package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import org.joda.time.DateTime;

public class TimeUtils {

    public static DateTime getClosestTimeRound(DateTime dt) {
        if (isRoundToFullHourNeeded(dt)) {
            return getFullHourRound(dt);
        } else if (isRoundToHalfOfTenNeeded(dt)) {
            return getHalfOfTenRound(dt);
        } else {
            return getTheClosestTenRound(dt);
        }
    }

    private static boolean isRoundToFullHourNeeded(DateTime dt) {
        return dt.getMinuteOfHour() > 57 || dt.getMinuteOfHour() < 3;
    }

    private static boolean isRoundToHalfOfTenNeeded(DateTime dt) {
        int minuteOfMinutesOfHour = dt.getMinuteOfHour() % 10;
        return minuteOfMinutesOfHour >= 3 && minuteOfMinutesOfHour <= 7;
    }

    private static int getDifferenceBetweenMinuteAndHalfOfTen(int minute) {
        int halfOfTen = 5;
        return halfOfTen - (minute % 10);
    }

    private static DateTime getFullHourRound(DateTime dt) {
        return dt.hourOfDay().roundHalfFloorCopy();
    }

    private static DateTime getHalfOfTenRound(DateTime dt) {
        return dt.plusMinutes(getDifferenceBetweenMinuteAndHalfOfTen(dt.getMinuteOfHour()));
    }

    private static DateTime getTheClosestTenRound(DateTime dt) {
        int minuteOfMinutesOfHour = dt.getMinuteOfHour() % 10;
        int diffToTen = 10 - minuteOfMinutesOfHour;
        return minuteOfMinutesOfHour >= diffToTen
                ? dt.plusMinutes(diffToTen)
                : dt.minusMinutes(minuteOfMinutesOfHour);
    }
}
