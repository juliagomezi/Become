package com.pes.become.frontend;

public class ActivityDummy {
    private String name;
    private String description;
    private String theme;
    private String startDay;
    private String startTime;
    private String endDay;
    private String endTime;

    private boolean expanded; // true => es veu la descripcio

    public ActivityDummy(String name, String description, String theme, String startDay, String startTime, String endDay, String endTime) {
        this.name = name;
        this.description = description;
        this.theme = theme;
        this.startDay = startDay;
        this.startTime = startTime;
        this.endDay = endDay;
        this.endTime = endTime;
        this.expanded = false;
    }

    private String getEndDay(String endDay) {
        return endDay;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTheme() {
        return this.theme;
    }

    public String getStartDay() {
        return this.startDay;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndDay() { return endDay; }

    public String getEndTime() {
        return this.endTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndDay(String endDay) { this.endDay = endDay; }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean getExpanded() { return expanded; }

    public void setExpanded(boolean expanded) { this.expanded = expanded; }


}
