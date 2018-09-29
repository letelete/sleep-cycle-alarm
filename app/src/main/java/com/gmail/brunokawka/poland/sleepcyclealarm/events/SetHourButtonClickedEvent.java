package com.gmail.brunokawka.poland.sleepcyclealarm.events;

import android.util.Log;

public class SetHourButtonClickedEvent {
    private static final String TAG = "WakeUpAtButtonEventLog";

    public SetHourButtonClickedEvent() {
        Log.d(TAG, "WakeUpAt activity button clicked");
    }
}
