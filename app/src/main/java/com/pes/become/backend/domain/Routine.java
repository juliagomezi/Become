package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import java.util.SortedMap;

/**
 * Classe que defineix una rutina
 */
public class Routine {
    /**
     * Nom de la rutina
     */
    private String name;
    /**
     * Mapa amb totes les activitats ordenades temporalment. La clau es la clau primaria d'activitat, i el valor l'activitat
     */
    private SortedMap<ActivityKey, Activity> activities;

    /**
     * Creadora de la rutina
     * @param name nom de la rutina
     */
    public Routine(String name){
        this.name = name;
    }

    /**
     * Getter del nom
     * @return nom de la rutina
     */
    public String getName(){
        return this.name;
    }

    /**
     * Metode que afegeix una activitat a la rutina
     * @param activity activitat a afegir
     */
    public void addActivity(Activity activity){
        ActivityKey activityKey = new ActivityKey(activity.getName(), this.name, activity.getDay(), activity.getInterval());
        activities.put(activityKey, activity);
    }

    /**
     * Getter d'una instancia d'activitat
     * @param activityKey clau de l'activitat que volem
     * @return instancia de l'activitat identificada per la clau
     */
    public Activity getActivity(ActivityKey activityKey){
        return activities.get(activityKey);
    }

    /**
     * Metode per actualitzar els parametres d'una activitat de la rutina
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
    public void updateActivity(ActivityKey activityKey, String name, String description, int iniH, int iniM, int endH, int endM, Day day) throws InvalidTimeIntervalException, InvalidTimeException {
        Activity activity = getActivity(activityKey);
        activity.update(name, description, iniH, iniM, endH, endM, day);
        deleteActivity(activityKey);
        addActivity(activity);
    }

    /**
     * Metode per eliminar una activitat
     * @param activityKey clau que identifica la activitat a eliminar
     */
    public void deleteActivity(ActivityKey activityKey){
        activities.remove(activityKey);
    }
}
