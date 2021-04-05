package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;

import java.util.ArrayList;

/**
 * Classe encarregada de gestionar l'acces a la classe Routine
 */
public class RoutineAdapter {

    /**
     * Unica instancia de la classe
     */
    private static RoutineAdapter instance;

    private final Routine routine = new Routine("RutinaDeProva"); //sample routine

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
     * Metode per consultar les activitats d'un dia
     * @param day nom del dia
     */
    public ArrayList<ArrayList<String>> getActivitiesByDay(String day) {
        ArrayList<Activity> activities = routine.getActivitiesByDay(Day.valueOf(day));
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        for(Activity activity : activities) {
            ArrayList<String> resaux = new ArrayList<>();
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
     * Métode per buidar les activitats d'una rutina
     */
    public void clearActivities() {
        routine.clearActivities();
    }

    /**
     * Metode per a afegir una activitat a una rutina
     * @param activity nova activitat
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void createActivity(Activity activity) throws OverlappingActivitiesException {
        routine.createActivity(activity);
    }

    /**
     * Metode per actualitzar els parametres d'una activitat d'una rutina
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void updateActivity(Activity a) throws OverlappingActivitiesException {
        routine.updateActivity(a);
    }

    /**
     * Metode per eliminar una activitat de la rutina
     * @param id identificador de l'activitat
     * @param day dia de l'activitat
     */
    public void deleteActivity(String id, Day day) {
        routine.deleteActivity(id, day);
    }

    /**
     * Funcio que comprova si una activitat donada es solapa temporalment amb alguna del seu mateix dia
     * @param a activitat a comprovar
     * @return true si hi ha solapament, false altrament
     */
    public boolean checkOverlappings(Activity a) {
        return routine.checkOverlappings(a);
    }

}
