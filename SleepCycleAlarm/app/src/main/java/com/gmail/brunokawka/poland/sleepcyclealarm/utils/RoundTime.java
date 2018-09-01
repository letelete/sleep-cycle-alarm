package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import org.joda.time.DateTime;

public class RoundTime {
    public static DateTime getNearest(DateTime dt) {
        if (isRoundToFullHourNeeded(dt)) {
            return dt.hourOfDay().roundHalfFloorCopy();
        } else if (isRoundToHalfOfTenNeeded(dt)) {
            return dt.plusMinutes(getDifferenceBetweenMinuteAndHalfOfTen(dt.getMinuteOfHour()));
        } else {
            int windowMinutes = 5;
            return dt.withMinuteOfHour((dt.getMinuteOfHour() / windowMinutes) * windowMinutes)
                    .minuteOfDay().roundFloorCopy();
        }
    }

    private static boolean isRoundToFullHourNeeded(DateTime dt) {
        return dt.getMinuteOfHour() > 57 || dt.getMinuteOfHour() < 5;
    }

    private static boolean isRoundToHalfOfTenNeeded(DateTime dt) {
        return dt.getMinuteOfHour() % 10 >= 3;
    }

    private static int getDifferenceBetweenMinuteAndHalfOfTen(int minute) {
        int halfOfTen = 5;
        return halfOfTen - (minute % 10);
    }
}
