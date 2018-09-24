package com.gmail.brunokawka.poland.sleepcyclealarm.data;

public class ItemsBuilderData {
    private final static int MAX_AMOUNT_OF_ITEMS_IN_LIST = 8;
    private final static int UPDATE_INTERVAL_IN_MINUTES = 4;
    private final static int ONE_SLEEP_CYCLE_DURATION_IN_MINUTES = 90;
    private final static int TIME_FOR_FALL_ASLEEP_IN_MINUTES = 15; // TODO: It could be an option in sharedPreferences to let user personalize that

    public static int getMaxAmountOfItemsInList() {
        return MAX_AMOUNT_OF_ITEMS_IN_LIST;
    }

    public static int getUpdateIntervalInMinutes() {
        return UPDATE_INTERVAL_IN_MINUTES;
    }

    public static int getOneSleepCycleDurationInMinutes() {
        return ONE_SLEEP_CYCLE_DURATION_IN_MINUTES;
    }

    public static int getTimeForFallAsleepInMinutes() {
        return TIME_FOR_FALL_ASLEEP_IN_MINUTES;
    }

    public static int getTotalOneSleepCycleDurationInMinutes() {
        return ONE_SLEEP_CYCLE_DURATION_IN_MINUTES + TIME_FOR_FALL_ASLEEP_IN_MINUTES;
    }

}
