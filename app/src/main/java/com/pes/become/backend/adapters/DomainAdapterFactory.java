package com.pes.become.backend.adapters;

import android.util.Log;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Theme;
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
     * @param iniH hora d'inici de l'activitat
     * @param iniM minut d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minut de fi de l'activitat
     * @param iniDayString dia d'inici de l'activitat
     * @param endDayString dia de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidDayIntervalException es llença si el dia de fi es anterior al dia d'inici
     */
    public void createActivity(String name, String description, String theme, String iniH, String iniM, String endH, String endM, String iniDayString, String endDayString) throws InvalidTimeIntervalException, InvalidDayIntervalException {
        Day iniDay = Day.values()[Integer.parseInt(iniDayString)];
        Day endDay = Day.values()[Integer.parseInt(endDayString)];
        int comparison = iniDay.compareTo(endDay); //negatiu si iniDay<endDay; 0 si iguals; positiu si iniDay>endDay
        if(comparison < 0){ //activitat en dies diferents
            //creacio activitat dia 1
            routineAdapter.addActivity(name, description, Theme.values()[Integer.parseInt(theme)], Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59, iniDay);
            String beginTime = iniH + ":" + iniM;
            String endTime = "23:59";
            controllerPersistence.createActivity("RutinaDeProva", name, Theme.values()[Integer.parseInt(theme)].toString(), description, iniDay.toString(), beginTime, endTime);
            //creacio activitat dia 2
            routineAdapter.addActivity(name, description, Theme.values()[Integer.parseInt(theme)], 0, 0, Integer.parseInt(endH), Integer.parseInt(endM), endDay);
            beginTime = "00:00";
            endTime = endH + ":" + endM;
            controllerPersistence.createActivity("RutinaDeProva", name, Theme.values()[Integer.parseInt(theme)].toString(), description, iniDay.toString(), beginTime, endTime);
        }
        else if(comparison == 0) {
            routineAdapter.addActivity(name, description, Theme.values()[Integer.parseInt(theme)], Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM), iniDay);
            String beginTime = iniH + ":" + iniM;
            String endTime = endH + ":" + endM;
            controllerPersistence.createActivity("RutinaDeProva", name, Theme.values()[Integer.parseInt(theme)].toString(), description, iniDay.toString(), beginTime, endTime);
        }
        else throw new InvalidDayIntervalException("Error: el dia de fi és anterior al dia d'inici");
    }

    /**
     * Metode per actualitzar els parametres d'una activitat d'una rutina
     * @param oldIniH hora inicial desactualitzada
     * @param oldIniM minuts inicials descatualitzats
     * @param oldEndH hora final descatualitzada
     * @param oldEndM minuts finals decastualitzats
     * @param name nou nom de l'activitat
     * @param description nova descripcio de l'activitat
     * @param theme nou tema de l'activitat
     * @param iniH nova hora d'inici de l'activitat
     * @param iniM nous minuts d'inici de l'activitat
     * @param endH nova hora de fi de l'activitat
     * @param endM nous minuts de fi de l'activitat
     * @param iniDayString nou dia d'inici de l'activitat
     * @param endDayString nou dia de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidDayIntervalException es llença si el dia d'inici es posterior al dia de fi
     */
    public void updateActivity(String name, String description, String theme, String oldIniH, String oldIniM, String oldEndH, String oldEndM, String iniH, String iniM, String endH, String endM, String iniDayString, String endDayString) throws InvalidDayIntervalException, InvalidTimeIntervalException {
        Day iniDay = Day.values()[Integer.parseInt(iniDayString)];
        Day endDay = Day.values()[Integer.parseInt(endDayString)];
        int comparison = iniDay.compareTo(endDay); //negatiu si iniDay<endDay; 0 si iguals; positiu si iniDay>endDay
        String oldBeginTime = oldIniH + ":" + oldIniM;
        String oldEndTime = oldEndH + ":" + oldEndM;
        if(comparison < 0){ //activitat en dies diferents
            routineAdapter.updateActivity(name, description, Theme.values()[Integer.parseInt(theme)], Integer.parseInt(oldIniH), Integer.parseInt(oldIniM), Integer.parseInt(oldEndH), Integer.parseInt(oldEndM), Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59, iniDay);
            String beginTime = iniH + ":" + iniM;
            String endTime = "23:59";
            controllerPersistence.updateActivity("RutinaDeProva", name, description, Theme.values()[Integer.parseInt(theme)].toString(), oldBeginTime, oldEndTime, beginTime, endTime, iniDay.toString());

            routineAdapter.addActivity(name, description, Theme.values()[Integer.parseInt(theme)], 0, 0, Integer.parseInt(endH), Integer.parseInt(endM), iniDay);
            beginTime = "00:00";
            endTime = endH + ":" + endM;
            controllerPersistence.createActivity("RutinaDeProva", name, Theme.values()[Integer.parseInt(theme)].toString(), description, endDay.toString(), beginTime, endTime);
        }
        else if(comparison == 0) {
            routineAdapter.updateActivity(name, description, Theme.values()[Integer.parseInt(theme)], Integer.parseInt(oldIniH), Integer.parseInt(oldIniM), Integer.parseInt(oldEndH), Integer.parseInt(oldEndM), Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM), iniDay);
            String beginTime = iniH + ":" + iniM;
            String endTime = endH + ":" + endM;
            controllerPersistence.updateActivity("RutinaDeProva", name, description, Theme.values()[Integer.parseInt(theme)].toString(), oldBeginTime, oldEndTime, beginTime, endTime, iniDay.toString());
        }
        else throw new InvalidDayIntervalException("Error: el dia de fi és anterior al dia d'inici");
    }

    /**
     * Metode per demanar les activitats d'un dia
     * @param dayString dia de les activitats
     * @param re instància de RoutineEdit
     * @throws NoSuchMethodException el mètode no existeix
     */
    public void getActivitiesFromDB(String dayString, RoutineEdit re) throws NoSuchMethodException {
        routineEdit = re;
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapterFactory.class.getMethod("setActivitiesFromDB", parameterTypes);
        controllerPersistence.getActivitiesByDay("RutinaDeProva", dayString, method1, DomainAdapterFactory.getInstance());
    }

    /**
     * Metode per rebre la resposta de la DB amb les activitats d'una rutina
     * @param acts activitats de la rutina
     * @throws InvalidTimeIntervalException l'interval de temps es incorrecte
     */
    public void setActivitiesFromDB(ArrayList<ArrayList<String>> acts) throws InvalidTimeIntervalException {
        routineAdapter.clearActivities();
        for(ArrayList<String> act : acts) {
            String[] s = act.get(4).split(":");
            String[] s2 = act.get(5).split(":");
            int iniH = Integer.parseInt(s[0]);
            int iniM = Integer.parseInt(s[1]);
            int endH = Integer.parseInt(s2[0]);
            int endM = Integer.parseInt(s2[1]);
            routineAdapter.addActivity(act.get(0), act.get(1),Theme.valueOf(act.get(2)), iniH, iniM, endH, endM, Day.valueOf(act.get(3)));
        }
        routineEdit.getActivitiesCallback(routineAdapter.getActivitiesByDay("Monday"));
    }

    /**
     * Metode per eliminar una activitat
     * @param iniH hora d'inici de l'activitat
     * @param iniM minuts d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minuts de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public void deleteActivity(String iniH, String iniM, String endH, String endM) throws InvalidTimeIntervalException {
        routineAdapter.deleteActivity(Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM));
        String beginTime = iniH + ":" + endM;
        String endTime = endH + ":" + endM;
        controllerPersistence.deleteActivity("RutinaDeProva", beginTime, endTime);
        routineAdapter.getActivitiesByDay("Monday");
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
