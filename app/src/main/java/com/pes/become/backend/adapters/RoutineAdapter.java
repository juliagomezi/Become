package com.pes.become.backend.adapters;

import android.annotation.SuppressLint;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.exceptions.NoSelectedRoutineException;
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

    private Routine routine;

    /**
     * Obtenir la instancia de la classe
     * @return instancia
     */
    public static RoutineAdapter getInstance() {
        if(instance == null) {
            instance = new RoutineAdapter();
        }
        return instance;
    }

    /**
     * Metode per establir la rutina seleccionada
     * @param selectedRoutine rutina activa de l'usuari
     */
    public void setCurrentRoutine(Routine selectedRoutine) {
        routine = selectedRoutine;
    }

    /**
     * Metode per canviar el nom de la rutina
     * @param id identificador de la rutina
     * @param name nou nom de la rutina
     */
    public void changeName(String id, String name){
        if(routine.getId().equals(id))
            routine.setName(name);
    }

    /**
     * Crear una nova rutina
     * @param name nom de la rutina
     * @return instancia de la rutina creada
     */
    public Routine createRoutine(String name) {
        return new Routine(name);
    }

    /**
     * Consultar les activitats d'un dia
     * @param day nom del dia
     * @throws NoSelectedRoutineException si l'usuari no te cap rutina seleccionada
     */
    @SuppressLint("DefaultLocale")
    public ArrayList<ArrayList<String>> getActivitiesByDay(String day) throws NoSelectedRoutineException {
        if(routine == null){
            throw new NoSelectedRoutineException();
        }
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
            resaux.add(String.valueOf(activity.isDoneToday()));
            res.add(resaux);
        }
        return res;
    }

    /**
     * Afegir una activitat a una rutina
     * @param activity nova activitat
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     * @throws NoSelectedRoutineException si l'usuari no te cap rutina seleccionada
     */
    public void createActivity(Activity activity) throws OverlappingActivitiesException, NoSelectedRoutineException {
        if(routine == null) {
            throw new NoSelectedRoutineException();
        }
        routine.createActivity(activity);
    }

    /**
     * Actualitzar els parametres d'una activitat d'una rutina
     * @param activity activitat actualitzada
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     * @throws NoSelectedRoutineException si l'usuari no te cap rutina seleccionada
     */
    public void updateActivity(Activity activity) throws OverlappingActivitiesException, NoSelectedRoutineException {
        if(routine == null){
            throw new NoSelectedRoutineException();
        }
        routine.updateActivity(activity);
    }

    /**
     * Eliminar una activitat de la rutina
     * @param id identificador de l'activitat
     * @param day dia de l'activitat
     * @throws NoSelectedRoutineException si l'usuari no te cap rutina seleccionada
     */
    public void deleteActivity(String id, Day day) throws NoSelectedRoutineException {
        if(routine == null){
            throw new NoSelectedRoutineException();
        }
        routine.deleteActivity(id, day);
    }

    /**
     * Métode per buidar les activitats d'una rutina
     */
    public void clearActivities() {
        routine.clearActivities();
    }

    /**
     * Setter de totes les activitats d'un dia d'una rutina
     * @param acts llista de les activitats
     * @param day dia de les activitats
     */
    public void setActivitiesByDay(ArrayList<Activity> acts, Day day) {
        routine.setActivitiesByDay(acts,day);
    }

    /**
     * Comprovar si una activitat donada es solapa temporalment amb alguna del seu mateix dia
     * @param a activitat a comprovar
     * @return true si hi ha solapament, false altrament
     */
    public boolean checkOverlappings(Activity a) {
        return routine.checkOverlappings(a);
    }

    public void markActivityAsDone(String activityID, boolean isDone) {
        routine.getActivity(activityID).setDoneToday(isDone);
    }
}
