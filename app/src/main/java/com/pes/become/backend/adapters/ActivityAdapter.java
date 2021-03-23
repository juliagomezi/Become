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

    private Routine routine = new Routine("rutina xaxi");

    /**
     * Metode per obtenir la instancia de la classe
     * @return instancia
     */
    public static ActivityAdapter getInstance(){
        if(instance == null){
            instance = new ActivityAdapter();
        }
        return instance;
    }

    /**
     * Metode per consultar les activitats d'un dia
     * @param day nom del dia
     */
    public ArrayList<ArrayList<String>> getActivities(String day) {
        ArrayList<Activity> acts = routine.getActivitiesByDay(day);
        ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        for(Activity act : acts) {
            ArrayList<String> resaux = new ArrayList<String>();
            resaux.add(act.getId());
            resaux.add(act.getName());
            resaux.add(act.getDescription());
            resaux.add(act.getTheme().toString());
            resaux.add(act.getStartDay().toString());
            resaux.add(act.getInterval().getStartTime().toString());
            resaux.add(act.getEndDay().toString());
            resaux.add(act.getInterval().getEndTime().toString());
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
     * @param startDay dia d'inici de l'activitat
     * @param endDay dia de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidTimeException es llença si les hores o minuts no tenen format valid
     */
    public void createActivity(String name, String description, String theme, int iniH, int iniM, int endH, int endM, String startDay, String endDay) throws InvalidTimeIntervalException, InvalidTimeException {
        routine.addActivity(new Activity(name, description, Theme.valueOf(theme), iniH, iniM, endH, endM, Day.valueOf(startDay),Day.valueOf(endDay)));
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
