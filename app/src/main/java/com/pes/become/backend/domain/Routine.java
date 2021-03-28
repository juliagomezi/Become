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
     * Mapa amb totes les activitats ordenades temporalment
     */
    private ArrayList<Activity> activities;

    /**
     * Creadora de la rutina
     * @param name nom de la rutina
     */
    public Routine(String name){
        this.name = name;
        this.activities = new ArrayList<>();
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
        activities = new ArrayList<>();
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
     * @param day dia a consultar
     * @return les activitats de la rutina al dia indicat
     */
    public ArrayList<Activity> getActivitiesByDay(Day day){
        ArrayList<Activity> res = new ArrayList<>();
        for(Activity act : activities) {
            if(act.getDay().equals(day)) {
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
