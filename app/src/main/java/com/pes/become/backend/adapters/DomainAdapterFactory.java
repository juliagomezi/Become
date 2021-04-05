package com.pes.become.backend.adapters;

import android.util.Log;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.domain.Time;
import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;
import com.pes.become.backend.persistence.ControllerPersistence;
import com.pes.become.frontend.RoutineEdit;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Classe que gestiona la comunicacio entre la capa de presentacio i la capa de domini, i la creacio dels adaptadors de cada classe de domini
 */
public class DomainAdapterFactory {
    /**
     * Unica instancia de la classe
     */
    private static DomainAdapterFactory instance;
    /**
     * Unica instancia del controlador de persistencia
     */
    private static final ControllerPersistence controllerPersistence = new ControllerPersistence();
    /**
     * Unica instancia de l'adaptador de la classe Rutina
     */
    private static final RoutineAdapter routineAdapter = RoutineAdapter.getInstance();

    private static RoutineEdit routineEdit;

    /**
     * Metode per obtenir la instancia de la classe
     * @return instancia
     */
    public static DomainAdapterFactory getInstance(){
        if(instance == null){
            instance = new DomainAdapterFactory();
        }
        return instance;
    }

    //Aqui necessitarem, mes endavant, el correu de l'usuari
    /**
     * Metode per crear una rutina
     * @param name nom de la rutina
     */
    public void createRoutine(String name){
        routineAdapter.createRoutine(name);
        //controllerPersistence.createNewRoutine(name);
    }

    /**
     * Metode per crear una activitat
     * @param name nom de l'activitat
     * @param description descripcio de l'activitat
     * @param theme tema de l'activitat
     * @param startDayString dia d'inici de l'activitat
     * @param endDayString dia de fi de l'activitat
     * @param iniH hora d'inici de l'activitat
     * @param iniM minut d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minut de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidDayIntervalException es llença si el dia de fi es anterior al dia d'inici
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void createActivity(String name, String description, String theme, String startDayString, String endDayString, String iniH, String iniM, String endH, String endM) throws InvalidTimeIntervalException, InvalidDayIntervalException, OverlappingActivitiesException {
        Day startDay = Day.values()[Integer.parseInt(startDayString)];
        Day endDay = Day.values()[Integer.parseInt(endDayString)];
        int comparison = startDay.compareTo(endDay);
        if (comparison < 0) {
            // activitat dia 1
            Activity newActDay1 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval (Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59), startDay);
            Activity newActDay2 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval (0, 0, Integer.parseInt(endH), Integer.parseInt(endM)), endDay);
            if(!routineAdapter.checkOverlappings(newActDay1) && !routineAdapter.checkOverlappings(newActDay2)) {
                String beginTime = iniH + ":" + iniM;
                String endTime = "23:59";
                String id = controllerPersistence.createActivity("RutinaDeProva", name, Theme.values()[Integer.parseInt(theme)].toString(), description, startDay.toString(), beginTime, endTime);
                newActDay1.setId(id);
                routineAdapter.createActivity(newActDay1);
                // activitat dia 2
                beginTime = "00:00";
                endTime = endH + ":" + endM;
                id = controllerPersistence.createActivity("RutinaDeProva", name, Theme.values()[Integer.parseInt(theme)].toString(), description, startDay.toString(), beginTime, endTime);
                newActDay2.setId(id);
                routineAdapter.createActivity(newActDay2);
            } else throw new OverlappingActivitiesException();
        }
        else if (comparison == 0) {
            Activity a = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval (Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM)), startDay);
            if(!routineAdapter.checkOverlappings(a)) {
                String beginTime = iniH + ":" + iniM;
                String endTime = endH + ":" + endM;
                String id = controllerPersistence.createActivity("RutinaDeProva", name, Theme.values()[Integer.parseInt(theme)].toString(), description, startDay.toString(), beginTime, endTime);
                a.setId(id);
                routineAdapter.createActivity(a);
            } else throw new OverlappingActivitiesException();
        }
        else throw new InvalidDayIntervalException();
    }

    /**
     * Metode per actualitzar els parametres d'una activitat d'una rutina
     * @param name nou nom de l'activitat
     * @param description nova descripcio de l'activitat
     * @param theme nou tema de l'activitat
     * @param startDayString nou dia d'inici de l'activitat
     * @param endDayString nou dia de fi de l'activitat
     * @param iniH nova hora d'inici de l'activitat
     * @param iniM nous minuts d'inici de l'activitat
     * @param endH nova hora de fi de l'activitat
     * @param endM nous minuts de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidDayIntervalException es llença si el dia d'inici es posterior al dia de fi
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void updateActivity(String id, String name, String description, String theme, String startDayString, String endDayString, String iniH, String iniM, String endH, String endM) throws InvalidDayIntervalException, InvalidTimeIntervalException, OverlappingActivitiesException {
        Day startDay = Day.values()[Integer.parseInt(startDayString)];
        Day endDay = Day.values()[Integer.parseInt(endDayString)];
        int comparison = startDay.compareTo(endDay);
        if (comparison < 0) {
            // activitat dia 1
            Activity updatedActivity1 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval(Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59), startDay);
            updatedActivity1.setId(id);
            routineAdapter.updateActivity(updatedActivity1);
            String beginTime = iniH + ":" + iniM;
            String endTime = "23:59";
            controllerPersistence.updateActivity("RutinaDeProva", name, description, Theme.values()[Integer.parseInt(theme)].toString(), beginTime, endTime, startDay.toString(), id);
            // activitat dia 2
            createActivity(name, description, theme, endDayString, endDayString, "0","0", endH, endM);
        }
        else if (comparison == 0) {
            Activity updatedActivity = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval(Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM)), startDay);
            updatedActivity.setId(id);
            routineAdapter.updateActivity(updatedActivity);
            String beginTime = iniH + ":" + iniM;
            String endTime = endH + ":" + endM;
            controllerPersistence.updateActivity("RutinaDeProva", name, description, Theme.values()[Integer.parseInt(theme)].toString(), beginTime, endTime, startDay.toString(), id);
        }
        else throw new InvalidDayIntervalException();
    }

    /**
     * Metode per demanar les activitats d'un dia
     * @param dayString dia de les activitats
     * @param re instància de RoutineEdit
     * @throws NoSuchMethodException el mètode no existeix
     */
    public void getActivitiesByDay(String dayString, RoutineEdit re) throws NoSuchMethodException {
        Log.d("getActivitiesByDay","getActivitiesByDay");
        routineEdit = re;
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapterFactory.class.getMethod("setActivitiesByDay", parameterTypes);
        controllerPersistence.getActivitiesByDay("RutinaDeProva", dayString, method1, DomainAdapterFactory.getInstance());
    }

    /**
     * Metode per rebre la resposta de la DB amb les activitats d'una rutina
     * @param acts activitats de la rutina
     * @throws InvalidTimeIntervalException l'interval de temps es incorrecte
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void setActivitiesByDay(ArrayList<ArrayList<String>> acts) throws InvalidTimeIntervalException, OverlappingActivitiesException {
        routineAdapter.clearActivities();
        for(ArrayList<String> act : acts) {
            String[] s = act.get(5).split(":");
            String[] s2 = act.get(6).split(":");
            int iniH = Integer.parseInt(s[0]);
            int iniM = Integer.parseInt(s[1]);
            int endH = Integer.parseInt(s2[0]);
            int endM = Integer.parseInt(s2[1]);
            Activity activity = new Activity(act.get(1), act.get(2),Theme.valueOf(act.get(3)), new TimeInterval(iniH, iniM, endH, endM), Day.valueOf(act.get(4)));
            activity.setId(act.get(0));
            routineAdapter.createActivity(activity);
        }
        routineEdit.getActivitiesCallback(routineAdapter.getActivitiesByDay("Monday"));
    }

    /**
     * Metode per eliminar una activitat
     * @param id identificador de l'activitat
     * @param day dia de l'activitat
     */
    public void deleteActivity(String id, String day) {
        routineAdapter.deleteActivity(id, Day.valueOf(day));
        controllerPersistence.deleteActivity("RutinaDeProva", id);
    }

    /**
     * Funcio per saber la posicio del tema al spinner
     * @param element nom del tema
     * @return posicio del tema al spinner
     */
    public int getPositionTheme(String element) {
        for (int i =0; i<Theme.values().length; ++i) {
            if (element.equals(Theme.values()[i].toString())) return i;
        }
        return 0;
    }

    /**
     * Funcio per saber la posició del dia al spinner
     * @param element nom del dia
     * @return posició del dia al spinner
     */
    public int getPositionDay(String element) {
        for (int i =0; i<Day.values().length; ++i) {
            if (element.equals(Day.values()[i].toString())) return i;
        }
        return 0;
    }
}
