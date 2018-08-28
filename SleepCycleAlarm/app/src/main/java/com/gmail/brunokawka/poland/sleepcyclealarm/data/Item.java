package com.gmail.brunokawka.poland.sleepcyclealarm.data;

public class Item {
    private String title;
    private String summary;
    private int currentHour;
    private int currentMinute;
    private int executionHour;
    private int executionMinute;

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

    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    public int getCurrentMinute() {
        return currentMinute;
    }

    public void setCurrentMinute(int currentMinute) {
        this.currentMinute = currentMinute;
    }

    public int getExecutionHour() {
        return executionHour;
    }

    public void setExecutionHour(int executionHour) {
        this.executionHour = executionHour;
    }

    public int getExecutionMinute() {
        return executionMinute;
    }

    public void setExecutionMinute(int executionMinute) {
        this.executionMinute = executionMinute;
    }

}
