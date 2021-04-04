package com.pes.become.backend.domain;

import android.util.Log;

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
        clearActivities();
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
    public void createActivity(Activity activity) throws OverlappingActivitiesException {
        if(!checkOverlappings(activity)) {
            ArrayList<Activity> actDay = getActivitiesByDay(activity.getDay());
            actDay.add(activity);
            Collections.sort(actDay); //NO FUNCIONA, NO ORDENA
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
     * @param newActivity nova activitat
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void updateActivity(TimeInterval oldTime, Day oldDay, Activity newActivity) throws OverlappingActivitiesException {
        if(!checkOverlappings(newActivity)) { //ARA MATEIX NO VA EL UPDATE PERQUE EL CHECKOVERLAPPING SEMPRE RETORNA TRUE (COMAPARA AMB SI MATEIXA, NOMES S'ARREGLA AMB IDs)
            ArrayList<Activity> a = activities.get(oldDay);
            for (Activity act : a) {
                if (act.getInterval().compareTo(oldTime) == 0) {
                    a.remove(act);
                    activities.get(newActivity.getDay()).add(newActivity);
                    Collections.sort(activities.get(newActivity.getDay())); //NO FUNCIONA, NO ORDENA
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
        ArrayList<Activity> acts = activities.get(day);
        for (Activity activity : acts) {
            if (activity.getInterval().compareTo(time) == 0) {
                activities.get(day).remove(activity);
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
