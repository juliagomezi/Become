package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import java.util.SortedMap;

/**
 *
 */
public class Routine {
    /**
     *
     */
    private String name;
    /**
     *
     */
    private SortedMap<ActivityKey, Activity> activities;

    /**
     *
     * @param name
     */
    public Routine(String name){
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     *
     * @param activity
     */
    public void addActivity(Activity activity){
        ActivityKey activityKey = new ActivityKey(activity.getName(), this.name, activity.getDay(), activity.getInterval());
        activities.put(activityKey, activity);
    }

    /**
     *
     * @param activityKey
     * @return
     */
    public Activity getActivity(ActivityKey activityKey){
        return activities.get(activityKey);
    }

    /**
     *
     * @param activityKey
     * @param name
     * @param description
     * @param iniH
     * @param iniM
     * @param endH
     * @param endM
     * @param dayString
     * @throws InvalidTimeIntervalException
     * @throws InvalidTimeException
     */
    public void updateActivity(ActivityKey activityKey, String name, String description, int iniH, int iniM, int endH, int endM, String dayString) throws InvalidTimeIntervalException, InvalidTimeException {
        Activity activity = getActivity(activityKey);
        activity.setName(name);
        activity.setDescription(description);
        activity.setInterval(iniH, iniM, endH, endM);
        activity.setDay(Day.valueOf(dayString));
        deleteActivity(activityKey);
        addActivity(activity);
    }

    /**
     *
     * @param activityKey
     */
    public void deleteActivity(ActivityKey activityKey){
        activities.remove(activityKey);
    }
}
