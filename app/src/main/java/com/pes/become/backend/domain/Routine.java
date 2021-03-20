package com.pes.become.backend.domain;

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
     */
    public void deleteActivity(ActivityKey activityKey){
        activities.remove(activityKey);
    }
}
