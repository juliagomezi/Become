package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Classe que defineix una rutina
 */
public class Routine {
    /**
     * Nom de la rutina
     */
    private String name;
    /**
     * Mapa amb totes les activitats ordenades temporalment
     */
    private SortedMap<Day, ArrayList<Activity>> activities;

    /**
     * Creadora de la rutina
     * @param name nom de la rutina
     */
    public Routine(String name){
        this.name = name;
        activities = new TreeMap<>();
    }

    /**
     * Getter del nom
     * @return nom de la rutina
     */
    public String getName(){
        return this.name;
    }

    /**
     * Métode per buidar les activitats d'una rutina
     */
    public void clearActivities() {
        activities = new TreeMap<>();
        for(int i=0; i<7; ++i){
            ArrayList<Activity> tmp = new ArrayList<>();
            activities.put(Day.values()[i], tmp);
        }
    }

    /**
     * Metode que afegeix una activitat a la rutina
     * @param activity activitat a afegir
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void addActivity(Activity activity) throws OverlappingActivitiesException {
        if(!checkOverlappings(activity)) {
            ArrayList<Activity> actDay = getActivitiesByDay(activity.getDay());
            actDay.add(activity);
            Collections.sort(actDay);
        } else throw new OverlappingActivitiesException();
    }

    /**
     * Getter de totes les activitats d'un dia
     * @param day dia a consultar
     * @return les activitats de la rutina al dia indicat
     */
    public ArrayList<Activity> getActivitiesByDay(Day day){
        return activities.get(day);
    }

    /**
     * Metode per actualitzar els parametres d'una activitat de la rutina
     * @param oldTime interval desactualitzat
     * @param oldDay dia desactualitzat
     * @param newact nova activitat
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void updateActivity(TimeInterval oldTime, Day oldDay, Activity newact) throws OverlappingActivitiesException {
        if(!checkOverlappings(newact)) {
            ArrayList<Activity> a = activities.get(oldDay);
            for (Activity act : a) {
                if (act.getInterval().compareTo(oldTime) == 0) {
                    activities.get(oldDay).remove(act); //Eliminem del dia anterior
                    activities.get(newact.getDay()).add(newact); //Posem al dia nou
                    break;
                }
            }
        } else throw new OverlappingActivitiesException();
    }

    /**
     * Metode per eliminar una activitat de la rutina
     * @param time hora de l'activitat a esborrar
     * @param day dia de l'activitat
     */
    public void deleteActivity(TimeInterval time, Day day) {
        ArrayList<Activity> a = activities.get(day);
        for (Activity act : a) {
            if (act.getInterval().compareTo(time) == 0) {
                activities.get(day).remove(act);
                break;
            }
        }
    }

    /**
     * Funcio que comprova si una activitat donada es solapa temporalment amb alguna del seu mateix dia
     * @param a activitat a comparar
     * @return true si hi ha solapament, false altrament
     */
    private boolean checkOverlappings(Activity a) {
        ArrayList<Activity> acts = activities.get(a.getDay());
        for (Activity activity : acts) {
            if (activity.compareTo(a) == 0) {
                return true;
            }
        }
        return false;
    }
}
