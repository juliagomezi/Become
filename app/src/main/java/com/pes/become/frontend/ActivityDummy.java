package com.pes.become.frontend;

public class ActivityDummy {
    private String name;
    private String description;
    private String theme;
    private String weekDay;
    private String startTime;
    private String endTime;


    public ActivityDummy(String name, String description, String theme, String weekDay, String startTime, String endTime) {
        this.name = name;
        this.description = description;
        this.theme = theme;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getWeekDay() {
        return this.weekDay;
    }

    public String getStartTime() {
        return this.startTime;
    }

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

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
