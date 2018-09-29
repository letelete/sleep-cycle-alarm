package com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo;

import org.joda.time.DateTime;

public class Item {
    private String title;
    private String summary;
    private DateTime currentDate;
    private DateTime executionDate;

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

    public DateTime getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(DateTime currentDate) {
        this.currentDate = currentDate;
    }

    public DateTime getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(DateTime executionDate) {
        this.executionDate = executionDate;
    }
}
