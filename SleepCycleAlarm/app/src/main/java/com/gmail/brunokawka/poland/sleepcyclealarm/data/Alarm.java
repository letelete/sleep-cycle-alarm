package com.gmail.brunokawka.poland.sleepcyclealarm.data;

import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Alarm extends RealmObject {
    private static final String TAG = "AlarmLog";

    @PrimaryKey
    @Required
    private String id;

    private int whenSetUpHour;
    private int whenSetUpMinute;
    private int executeHour;
    private int executeMinute;
    private int snoozeDurationInMinutes;
    private boolean isRingingInSilentMode;
    private String Ringtone; // TODO : RINGTONE (Currently create some string for entry testing)
    private int ringDurationInMinutes;
    private int numberOfRepetitionsBeforeAutoSilence;

    public int getWhenSetUpHour() {
        return whenSetUpHour;
    }

    public void setWhenSetUpHour(int whenSetUpHour) {
        this.whenSetUpHour = whenSetUpHour;
    }

    public int getWhenSetUpMinute() {
        return whenSetUpMinute;
    }

    public void setWhenSetUpMinute(int whenSetUpMinute) {
        this.whenSetUpMinute = whenSetUpMinute;
    }

    public int getExecuteHour() {
        return executeHour;
    }

    public void setExecuteHour(int executeHour) {
        this.executeHour = executeHour;
    }

    public int getExecuteMinute() {
        return executeMinute;
    }

    public void setExecuteMinute(int executeMinute) {
        this.executeMinute = executeMinute;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSnoozeDurationInMinutes() {
        return snoozeDurationInMinutes;
    }

    public void setSnoozeDurationInMinutes(int snoozeDurationInMinutes) {
        this.snoozeDurationInMinutes = snoozeDurationInMinutes;
    }

    public String getRingtone() {
        // TODO : RINGTONE (Currently create some string for entry testing)
        return Ringtone;
    }

    public void setRingtone(String ringtone) {
        // TODO : RINGTONE (Currently create some string for entry testing)
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
        return ItemContentBuilder.getTitle(getExecuteHour(), getExecuteMinute());
    }

    public String getSummary() {
        return ItemContentBuilder.getSummary(getWhenSetUpHour(), getWhenSetUpMinute(), getExecuteHour(), getExecuteMinute());
    }

}
