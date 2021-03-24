package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe que defineix una rutina
 */
public class Routine {
    /**
     * Nom de la rutina
     */
    private String name;
    /**
     * Mapa amb totes les activitats ordenades temporalment. La clau es la id de l'activitat, i el valor l'activitat
     */
    private ArrayList<Activity> activities;

    /**
     * Creadora de la rutina
     * @param name nom de la rutina
     */
    public Routine(String name){
        this.name = name;
        this.activities = new ArrayList<Activity>();
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
    public void addActivity(Activity activity) {
        activities.add(activity);
        Collections.sort(activities);
    }

    /**
     * Getter de totes les activitats d'un dia
     * @return les activitats
     */
    public ArrayList<Activity> getActivitiesByDay(String day){
        ArrayList<Activity> res = new ArrayList<Activity>();
        for(Activity act : activities) {
            if(act.getDay().equals(Day.valueOf(day))) {
                res.add(act);
            }
        }
        Collections.sort(res);
        return res;
    }

    /**
     * Metode per actualitzar els parametres d'una activitat de la rutina
     * @param name nou nom de l'activitat
     * @param description nova descripcio de l'activitat
     * @param theme
     * @param iniH nova hora d'inici de l'activitat
     * @param iniM nous minuts d'inici de l'activitat
     * @param endH nova hora de fi de l'activitat
     * @param endM nous minuts de fi de l'activitat
     * @param day nou dia de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public void updateActivity(String name, String description, Theme theme, int iniH, int iniM, int endH, int endM, Day day) throws InvalidTimeIntervalException {
        TimeInterval t = new TimeInterval(iniH, iniM, endH, endM);
        for (Activity act : activities) {
            TimeInterval taux = act.getInterval();
            if (taux.compareTo(t) == 0) {
                act.update(name, description, theme, iniH, iniM, endH, endM, day);
                Collections.sort(activities);
                break;
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
        for (Activity act : activities) {
            TimeInterval taux = act.getInterval();
            if (taux.compareTo(t) == 0) {
                activities.remove(act);
                Collections.sort(activities);
                break;
            }
        }
    }
}
