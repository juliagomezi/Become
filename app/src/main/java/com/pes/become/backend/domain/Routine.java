package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

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
     */
    public void addActivity(Activity activity) {
        ArrayList<Activity> actDay = getActivitiesByDay(activity.getDay());
        actDay.add(activity);
        Collections.sort(actDay);
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
     * @param name nou nom de l'activitat
     * @param description nova descripcio de l'activitat
     * @param theme tema de l'activitat
     * @param oldIniH hora inical desactualitzada
     * @param oldIniM minuts inicals descatualitzats
     * @param oldEndH hora final descatualitzada
     * @param oldEndM minuts finals desactualitzats
     * @param iniH nova hora d'inici de l'activitat
     * @param iniM nous minuts d'inici de l'activitat
     * @param endH nova hora de fi de l'activitat
     * @param endM nous minuts de fi de l'activitat
     * @param day nou dia de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public void updateActivity(String name, String description, Theme theme, int oldIniH, int oldIniM, int oldEndH, int oldEndM, int iniH, int iniM, int endH, int endM, Day day) throws InvalidTimeIntervalException {
        TimeInterval t = new TimeInterval(oldIniH, oldIniM, oldEndH, oldEndM);
        for(Map.Entry<Day, ArrayList<Activity>> actDay : activities.entrySet()) {
            for (Activity act : actDay.getValue()) {
                TimeInterval taux = act.getInterval();
                if (taux.compareTo(t) == 0) {
                    activities.get(act.getDay()).remove(act); //Eliminem del dia anterior
                    act.update(name, description, theme, iniH, iniM, endH, endM, day);
                    activities.get(day).add(act); //Posem al dia nou
                    break;
                }
            }
        }
    }

    /**
     * Metode per eliminar una activitat de la rutina
     * @param iniH hora d'inici de l'activitat
     * @param iniM minuts d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minuts de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public void deleteActivity(int iniH, int iniM, int endH, int endM) throws InvalidTimeIntervalException {
        TimeInterval t = new TimeInterval(iniH, iniM, endH, endM);
        for(Map.Entry<Day, ArrayList<Activity>> actDay : activities.entrySet()) {
            for (Activity act : actDay.getValue()) {
                TimeInterval taux = act.getInterval();
                if (taux.compareTo(t) == 0) {
                    activities.remove(act);
                    Collections.sort(actDay.getValue());
                    break;
                }
            }
        }
    }
}
