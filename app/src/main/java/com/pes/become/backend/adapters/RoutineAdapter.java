package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Routine;

public class RoutineAdapter {
    private static RoutineAdapter instance;

    public static RoutineAdapter getInstance(){
        if(instance == null){
            instance = new RoutineAdapter();
        }
        return instance;
    }

    public void createRoutine(String name) {
        new Routine(name);
    }

    public void addActivity(String routineName, Activity activity) {
        //metode per obtenir rutina de DB
        //crida sobre la rutina per afegir activitat
        //routine.addActivity(activity)
    }
}
