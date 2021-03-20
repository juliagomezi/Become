package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.ActivityKey;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;
import com.pes.become.backend.persistence.ControllerPersistence;

public class DomainAdapterFactory {
    private static DomainAdapterFactory instance;
    private static ControllerPersistence controllerPersistence = new ControllerPersistence();
    private static RoutineAdapter routineAdapter;
    private static ActivityAdapter activityAdapter;

    public static DomainAdapterFactory getInstance(){
        if(instance == null){
            instance = new DomainAdapterFactory();
        }
        return instance;
    }

    private static void getInstanceRoutineAdapter(){
        if(routineAdapter == null){
            routineAdapter = RoutineAdapter.getInstance();
        }
    }

    private static void getInstanceActivityAdapter(){
        if(activityAdapter == null){
            activityAdapter = ActivityAdapter.getInstance();
        }
    }

    public void createRoutine(String name){
        getInstanceRoutineAdapter();
        routineAdapter.createRoutine(name);
        controllerPersistence.createNewRoutine(name);
    }

    public void createActivity(String routineName, String name, String description, String theme, int iniH, int iniM, int endH, int endM, String iniDayString, String endDayString) throws InvalidTimeIntervalException, InvalidDayIntervalException, InvalidTimeException, OverlappingActivitiesException {
        getInstanceActivityAdapter();
        getInstanceRoutineAdapter();
        Day iniDay = Day.valueOf(iniDayString);
        Day endDay = Day.valueOf(endDayString);
        int comparison = iniDay.compareTo(endDay); //negatiu si iniDay<endDay; 0 si iguals; positiu si iniDay>endDay
        if(comparison < 0){ //activitat en dies diferents
            //creacio activitat dia 1
            Activity activity1 = activityAdapter.createActivity(name, description, theme, iniH, iniM, 23, 59, iniDay);
            //routineAdapter.addActivity(routineName, activity1);
            String beginTime = iniH + ":" + iniM;
            String endTime = "23:59";
            controllerPersistence.createActivity(routineName, name, theme, description, iniDayString, beginTime, endTime);
            //creacio activitat dia 2
            Activity activity2 = activityAdapter.createActivity(name, description, theme, 0, 0, endH, endM, endDay);
            //routineAdapter.addActivity(routineName, activity2);
            beginTime = "00:00";
            endTime = endH + ":" + endM;
            controllerPersistence.createActivity(routineName, name, theme, description, iniDayString, beginTime, endTime);
        }
        else if(comparison == 0){
            Activity activity = activityAdapter.createActivity(name, description, theme, iniH, iniM, endH, endM, iniDay);
            routineAdapter.addActivity(routineName, activity);
            String beginTime = iniH + ":" + endM;
            String endTime = endH + ":" + endM;
            controllerPersistence.createActivity(routineName, name, theme, description, iniDayString, beginTime, endTime);
        }
        else throw new InvalidDayIntervalException("Error: el dia de fi és anterior al dia d'inici");
    }

    //WIP
    public void updateActivty(ActivityKey activityKey, String name, String description, int iniH, int iniM, int endH, int endM, String iniDayString, String endDayString) throws InvalidDayIntervalException {
        getInstanceActivityAdapter();
        getInstanceRoutineAdapter();
        Day iniDay = Day.valueOf(iniDayString);
        Day endDay = Day.valueOf(endDayString);
        int comparison = iniDay.compareTo(endDay); //negatiu si iniDay<endDay; 0 si iguals; positiu si iniDay>endDay
        if(comparison < 0){ //activitat en dies diferents

        }
        else if(comparison == 0){

        }
        else throw new InvalidDayIntervalException("Error: el dia de fi és anterior al dia d'inici");
    }
}
