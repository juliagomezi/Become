package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

public class ActivityAdapter {

    private static ActivityAdapter instance;

    public static ActivityAdapter getInstance(){
        if(instance == null){
            instance = new ActivityAdapter();
        }
        return instance;
    }

    public Activity createActivity(String name, String description, String themeString, int iniH, int iniM, int endH, int endM, Day day) throws InvalidTimeIntervalException, InvalidTimeException {
        Theme theme = new Theme(themeString);
        return new Activity(name, description, theme, iniH, iniM, endH, endM, day);
    }
}
