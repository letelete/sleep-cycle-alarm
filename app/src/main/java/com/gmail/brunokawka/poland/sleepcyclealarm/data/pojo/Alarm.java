package com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Alarm extends RealmObject {

    @PrimaryKey
    @Required
    private String id;

    private String title;
    private String summary;
    private int snoozeDurationInMinutes;
    private boolean isRingingInSilentMode;
    private String Ringtone; // TODO : RINGTONE (Currently create some string for entry testing)
    private int ringDurationInMinutes;
    private int numberOfRepetitionsBeforeAutoSilence;
    private String currentDate;
    private String executionDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }
}
