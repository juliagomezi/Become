package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Day;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;
import com.pes.become.backend.persistence.ControllerPersistence;

import java.lang.reflect.Method;

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
    private static ControllerPersistence controllerPersistence = new ControllerPersistence();
    /**
     * Unica instancia de l'adaptador de la classe Rutina
     */
    private static RoutineAdapter routineAdapter;
    /**
     * Unica instancia de l'adaptador de la classe Activitat
     */
    private static ActivityAdapter activityAdapter;

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

    /**
     * Metode per obtenir l'adaptador de Rutina
     */
    private static void getInstanceRoutineAdapter(){
        if(routineAdapter == null){
            routineAdapter = RoutineAdapter.getInstance();
        }
    }

    /**
     * Metode per obtenir l'adaptador de Activitat
     */
    private static void getInstanceActivityAdapter(){
        if(activityAdapter == null){
            activityAdapter = ActivityAdapter.getInstance();
        }
    }

    //Aqui necessitarem, mes endavant, el correu de l'usuari
    /**
     * Metode per crear una rutina
     * @param name nom de la rutina
     */
    public void createRoutine(String name){
        getInstanceRoutineAdapter();
        routineAdapter.createRoutine(name);
        //controllerPersistence.createNewRoutine(name);
    }

    /**
     * Metode per crear una activitat
     * @param routineName nom de la rutina de la que forma part l'activitat
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
     * @throws InvalidTimeException es llença si les hores o minuts no tenen format valid
     * @throws OverlappingActivitiesException es llença si existeix una activitat a la mateixa rutina que se solapa temporalment amb la creada
     */
    /*
    public void createActivity(String routineName, String name, String description, String theme, int iniH, int iniM, int endH, int endM, String iniDayString, String endDayString) throws InvalidTimeIntervalException, InvalidDayIntervalException, InvalidTimeException, OverlappingActivitiesException {
        getInstanceActivityAdapter();
        getInstanceRoutineAdapter();
        Day iniDay = Day.valueOf(iniDayString);
        Day endDay = Day.valueOf(endDayString);
        int comparison = iniDay.compareTo(endDay); //negatiu si iniDay<endDay; 0 si iguals; positiu si iniDay>endDay
        if(comparison < 0){ //activitat en dies diferents
            //creacio activitat dia 1
            //Activity activity1 = activityAdapter.createActivity(name, description, theme, iniH, iniM, 23, 59, iniDay);
            //routineAdapter.addActivity(routineName, activity1);
            String beginTime = iniH + ":" + iniM;
            String endTime = "23:59";
            controllerPersistence.createActivity(routineName, name, theme, description, iniDayString, beginTime, endTime);
            //creacio activitat dia 2
            //Activity activity2 = activityAdapter.createActivity(name, description, theme, 0, 0, endH, endM, endDay);
            //routineAdapter.addActivity(routineName, activity2);
            beginTime = "00:00";
            endTime = endH + ":" + endM;
            controllerPersistence.createActivity(routineName, name, theme, description, iniDayString, beginTime, endTime);
        }
        else if(comparison == 0){
            //Activity activity = activityAdapter.createActivity(name, description, theme, iniH, iniM, endH, endM, iniDay);
            //routineAdapter.addActivity(routineName, activity);
            String beginTime = iniH + ":" + endM;
            String endTime = endH + ":" + endM;
            controllerPersistence.createActivity(routineName, name, theme, description, iniDayString, beginTime, endTime);
        }
        else throw new InvalidDayIntervalException("Error: el dia de fi és anterior al dia d'inici");
    }

    //WIP
    public void updateActivty(String routineName, ActivityKey activityKey, String name, String description, int iniH, int iniM, int endH, int endM, String iniDayString, String endDayString) throws InvalidDayIntervalException {
        getInstanceRoutineAdapter();
        Day iniDay = Day.valueOf(iniDayString);
        Day endDay = Day.valueOf(endDayString);
        int comparison = iniDay.compareTo(endDay); //negatiu si iniDay<endDay; 0 si iguals; positiu si iniDay>endDay
        if(comparison < 0){ //activitat en dies diferents
            //routineAdapter.updateActivity(routineName, activityKey, name, description, iniH, iniM, 23, 59, iniDay);
            //routineAdapter.updateActivity(routineName, activityKey, name, description, 0, 0, endH, endM, iniDay);
        }
        else if(comparison == 0){
            //routineAdapter.updateActivity(routineName, activityKey, name, description, iniH, iniM, endH, endM, iniDay);
        }
        else throw new InvalidDayIntervalException("Error: el dia de fi és anterior al dia d'inici");
    }

    //WIP
    public void getActivitiesByDay(String routineName, String dayString, Method method, Object object) throws InterruptedException, NoSuchMethodException {
        //SortedMap<TimeInterval, InfoActivity> result = new TreeMap<>();
        controllerPersistence.getActivitiesByDay(routineName, dayString, method, object);
    }
    */
}
