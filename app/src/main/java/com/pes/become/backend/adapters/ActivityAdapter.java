package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import java.util.ArrayList;

/**
 * Classe encarregada de gestionar l'acces a la classe Routine
 */
public class ActivityAdapter {

    /**
     * Unica instancia de la classe
     */
    private static ActivityAdapter instance;

    private static Routine routine;

    /**
     * Metode per obtenir la instancia de la classe
     * @return instancia
     */
    public static ActivityAdapter getInstance() {
        if(instance == null) {
            instance = new ActivityAdapter();
            routine = new Routine("rutina xaxi");
            try {
                routine.addActivity(new Activity("test", "this is a test", Theme.Cooking, 8, 0, 9, 0, Day.Monday));
                routine.addActivity(new Activity("test", "this is a test", Theme.Music, 10, 30, 11, 0, Day.Monday));
                routine.addActivity(new Activity("test", "this is a test", Theme.Studying, 12, 0, 14, 0, Day.Monday));
                routine.addActivity(new Activity("test", "this is a test", Theme.Sport, 17, 0, 18, 0, Day.Monday));
                routine.addActivity(new Activity("test2", "this is a test2", Theme.Entertainment, 18, 0, 19, 0, Day.Monday));
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return instance;
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
            resaux.add(activity.getId());
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
     * Metode per crear una activitat
     * @param name nom de l'activitat
     * @param description descripcio de l'activitat
     * @param theme tema de l'activitat
     * @param iniH hora d'inici de l'activitat
     * @param iniM minut d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minut de fi de l'activitat
     * @param day dia d'inici de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidTimeException es llença si les hores o minuts no tenen format valid
     */
    public void createActivity(String name, String description, String theme, int iniH, int iniM, int endH, int endM, String day) throws InvalidTimeIntervalException, InvalidTimeException {
        routine.addActivity(new Activity(name, description, Theme.valueOf(theme), iniH, iniM, endH, endM, Day.valueOf(day)));
    }

    /**
     * Metode per actualitzar una activitat
     * @param id id de l'activitat
     * @param name nom de l'activitat
     * @param description descripcio de l'activitat
     * @param iniH hora d'inici de l'activitat
     * @param iniM minut d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minut de fi de l'activitat
     * @param day dia de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidTimeException es llença si les hores o minuts no tenen format valid
     */
    public void updateActivity(String id, String name, String description, int iniH, int iniM, int endH, int endM, String day) throws InvalidTimeIntervalException, InvalidTimeException {
        routine.updateActivity(id, name, description, iniH, iniM, endH, endM, Day.valueOf(day));
    }

    /**
     * Metode per eliminar una activitat
     * @param id id de l'activitat
     */
    public void deleteActivity(String id) {
        routine.deleteActivity(id);
    }

}
