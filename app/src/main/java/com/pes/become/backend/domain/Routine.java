package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.OverlappingActivitiesException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Classe que defineix una rutina
 */
public class Routine {
    /**
     * Identificador de la rutina
     */
    private String id;
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
    public Routine(String name) {
        this.id = null;
        this.name = name;
        activities = new TreeMap<>();
        for(int i=0; i<7; ++i){
            activities.put(Day.values()[i], new ArrayList<>());
        }
    }

    /**
     * Getter de la id
     * @return id de la rutina
     */
    public String getId() {
        return id;
    }

    /**
     * Setter de la id
     * @param id nova id de la rutina
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter del nom
     * @return nom de la rutina
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter del nom de la rutina
     * @param name nom de la rutina
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Métode per buidar les activitats d'una rutina
     * @param day dia de les activitats
     */
    public void clearActivities(Day day) {
        Objects.requireNonNull(activities.get(day)).clear();
    }

    /**
     * Metode que afegeix una activitat a la rutina
     * @param activity activitat a afegir
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     * */
    public void createActivity(Activity activity) throws OverlappingActivitiesException {
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
    public ArrayList<Activity> getActivitiesByDay(Day day) {
        return activities.get(day);
    }

    /**
     * Metode per actualitzar els parametres d'una activitat de la rutina
     * @param updatedActivity activitat actualitzada
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void updateActivity(Activity updatedActivity) throws OverlappingActivitiesException {
        if(!checkOverlappings(updatedActivity)) {
            for(Map.Entry<Day, ArrayList<Activity>> entry : activities.entrySet()) {
                for(Activity a : entry.getValue()) {
                    if(a.getId().equals(updatedActivity.getId())) {
                        entry.getValue().remove(a);
                        break;
                    }
                }
            }
            activities.get(updatedActivity.getDay()).add(updatedActivity);
            Collections.sort(activities.get(updatedActivity.getDay()));
        } else throw new OverlappingActivitiesException();
    }

    /**
     * Metode per eliminar una activitat de la rutina
     * @param id identificador de l'activitat
     * @param day dia de l'activitat
     */
    public void deleteActivity(String id, Day day) {
        ArrayList<Activity> acts = activities.get(day);
        for (Activity activity : acts) {
            if (activity.getId().equals(id)) {
                acts.remove(activity);
                break;
            }
        }
    }

    /**
     * Funcio que comprova si una activitat donada es solapa temporalment amb alguna del seu mateix dia
     * @param a activitat a comprovar
     * @return true si hi ha solapament, false altrament
     */
    public boolean checkOverlappings(Activity a) {
        ArrayList<Activity> acts = activities.get(a.getDay());
        for (Activity activity : acts) {
            if(!activity.getId().equals(a.getId()) && activity.compareTo(a) == 0) return true;
        }
        return false;
    }
}
