package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import java.util.ArrayList;

/**
 * Classe encarregada de gestionar l'acces a la classe Routine
 */
public class RoutineAdapter {

    /**
     * Unica instancia de la classe
     */
    private static RoutineAdapter instance;

    private Routine routine = new Routine("test"); //sample routine

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
     * @param name nom de l'activitat
     * @param description descripcio de l'activitat
     * @param theme tema de l'activitat
     * @param iniH hora d'inici de l'activitat
     * @param iniM minuts d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minuts de fi de l'activitat
     * @param day dia de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public void addActivity(String name, String description, Theme theme, int iniH, int iniM, int endH, int endM, Day day) throws InvalidTimeIntervalException {
        routine.addActivity(new Activity(name, description, theme, iniH, iniM, endH, endM, day));
    }

    /**
     * Metode per actualitzar els parametres d'una activitat d'una rutina
     * @param oldIniH
     * @param oldIniM
     * @param oldEndH
     * @param oldEndM
     * @param name nou nom de l'activitat
     * @param description nova descripcio de l'activitat
     * @param theme nou tema de l'activitat
     * @param iniH nova hora d'inici de l'activitat
     * @param iniM nous minuts d'inici de l'activitat
     * @param endH nova hora de fi de l'activitat
     * @param endM nous minuts de fi de l'activitat
     * @param day nou dia de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public void updateActivity(int oldIniH, int oldIniM, int oldEndH, int oldEndM, String name, String description, Theme theme, int iniH, int iniM, int endH, int endM, Day day) throws InvalidTimeIntervalException {
        routine.updateActivity(oldIniH, oldIniM, oldEndH, oldEndM, name, description, theme, iniH, iniM, endH, endM, day);
    }

    /**
     * Metode per consultar les activitats d'un dia
     * @param day nom del dia
     */
    public ArrayList<ArrayList<String>> getActivitiesByDay(String day) {
        ArrayList<Activity> activities = routine.getActivitiesByDay(day);
        ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        for(Activity activity : activities) {
            ArrayList<String> resaux = new ArrayList<String>();
            resaux.add(activity.getName());
            resaux.add(activity.getDescription());
            resaux.add(activity.getTheme().toString());
            resaux.add(activity.getDay().toString());
            Integer startHour = activity.getInterval().getStartTime().getHours();
            Integer startMinute = activity.getInterval().getStartTime().getMinutes();
            resaux.add(String.format("%02d", startHour) + ":" + String.format("%02d", startMinute));
            Integer endHour = activity.getInterval().getEndTime().getHours();
            Integer endMinute = activity.getInterval().getEndTime().getMinutes();
            resaux.add(String.format("%02d", endHour) + ":" + String.format("%02d", endMinute));
            res.add(resaux);
        }
        return res;
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
        routine.deleteActivity(iniH, iniM, endH, endM);
    }
}
