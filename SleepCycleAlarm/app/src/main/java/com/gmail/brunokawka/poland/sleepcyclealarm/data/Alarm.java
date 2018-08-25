package com.gmail.brunokawka.poland.sleepcyclealarm.data;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ListItemContentBuilder;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Alarm extends RealmObject {
    private static final String TAG = "AlarmLog";

    @PrimaryKey
    @Required
    private String id;

    private int hour;
    private int minute;
    private int snoozeDurationInMinutes;
    private boolean isRingingInSilentMode;
    private String Ringtone; // TODO : RINGTONE (Currently create some string for entry testing)
    private int ringDurationInMinutes;
    private int numberOfRepetitionsBeforeAutoSilence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSnoozeDurationInMinutes() {
        return snoozeDurationInMinutes;
    }

    public void setSnoozeDurationInMinutes(int snoozeDurationInMinutes) {
        this.snoozeDurationInMinutes = snoozeDurationInMinutes;
    }

    // TODO : RINGTONE (Currently create some string for entry testing)
    public String getRingtone() {
        return Ringtone;
    }
    // TODO : RINGTONE (Currently create some string for entry testing)
    public void setRingtone(String ringtone) {
        Ringtone = ringtone;
    }

    public boolean isRingingInSilentMode() {
        return isRingingInSilentMode;
    }

    public void setRingingInSilentMode(boolean ringingInSilentMode) {
        isRingingInSilentMode = ringingInSilentMode;
    }

    public int getRingDurationInMinutes() {
        return ringDurationInMinutes;
    }

    public void setRingDurationInMinutes(int ringDurationInMinutes) {
        this.ringDurationInMinutes = ringDurationInMinutes;
    }

    public int getNumberOfRepetitionsBeforeAutoSilence() {
        return numberOfRepetitionsBeforeAutoSilence;
    }

    public void setNumberOfRepetitionsBeforeAutoSilence(int numberOfRepetitionsBeforeAutoSilence) {
        this.numberOfRepetitionsBeforeAutoSilence = numberOfRepetitionsBeforeAutoSilence;
    }

    public String getTitle() {
        return ListItemContentBuilder.getTitle(getHour(), getMinute());
    }

    public String getSummary() {
        return ListItemContentBuilder.getSummary(getHour(), getMinute());
    }

}
