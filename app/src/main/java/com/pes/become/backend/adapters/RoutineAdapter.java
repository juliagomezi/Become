package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.ActivityKey;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

/**
 * Classe encarregada de gestionar l'acces a la classe Routine
 */
public class RoutineAdapter {

    /**
     * Unica instancia de la classe
     */
    private static RoutineAdapter instance;

    /**
     * Metode per obtenir la instancia de la classe
     * @return instancia
     */
    public static RoutineAdapter getInstance(){
        if(instance == null){
            instance = new RoutineAdapter();
        }
        return instance;
    }

    //tot aixo s'haura de canviar perque la clau de rutina es nom+correuUsuari
    /**
     * Metode per a crear una nova rutina
     * @param name nom de la rutina
     */
    public void createRoutine(String name) {
        new Routine(name);
    }

    /**
     * Metode per a afegir una activitat a una rutina
     * @param routineName nom de la rutina
     * @param activity instancia de l'activitat a afegir
     */
    public void addActivity(String routineName, Activity activity) {
        //metode per obtenir rutina de DB
        //crida sobre la rutina per afegir activitat
        //routine.addActivity(activity)
    }

    /**
     * Metode per actualitzar els parametres d'una activitat d'una rutina
     * @param routineName nom de la rutina que conte l'activitat
     * @param activityKey clau de l'activitat a modificar
     * @param name nou nom de l'activitat
     * @param description nova descripcio de l'activitat
     * @param iniH nova hora d'inici de l'activitat
     * @param iniM nous minuts d'inici de l'activitat
     * @param endH nova hora de fi de l'activitat
     * @param endM nous minuts de fi de l'activitat
     * @param day nou dia de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidTimeException es llença si les hores o minuts no tenen format valid
     */
    public void updateActivity(String routineName, ActivityKey activityKey, String name, String description, int iniH, int iniM, int endH, int endM, Day day){
        //metode per obtenir rutina de DB
        //routine.updateActivity(activityKey, name, description, iniH, iniM, endH, endM, day)
    }
}
