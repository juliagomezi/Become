package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
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
    private static final ControllerPersistence controllerPersistence = new ControllerPersistence();
    /**
     * Unica instancia de l'adaptador de la classe Rutina
     */
    private static final RoutineAdapter routineAdapter = RoutineAdapter.getInstance();

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
     * @throws OverlappingActivitiesException es llença si existeix una activitat a la mateixa rutina que se solapa temporalment amb la creada
     */
    public void createActivity(String name, String description, String theme, String iniH, String iniM, String endH, String endM, String iniDayString, String endDayString) throws InvalidTimeIntervalException, InvalidDayIntervalException, OverlappingActivitiesException {
        Day iniDay = Day.valueOf(iniDayString);
        Day endDay = Day.valueOf(endDayString);
        int comparison = iniDay.compareTo(endDay); //negatiu si iniDay<endDay; 0 si iguals; positiu si iniDay>endDay
        if(comparison < 0){ //activitat en dies diferents
            //creacio activitat dia 1
            routineAdapter.addActivity(name, description, Theme.valueOf(theme), Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59, iniDay);
            String beginTime = iniH + ":" + iniM;
            String endTime = "23:59";
            controllerPersistence.createActivity("RutinaDeProva", name, theme, description, iniDayString, beginTime, endTime);
            //creacio activitat dia 2
            routineAdapter.addActivity(name, description, Theme.valueOf(theme), 0, 0, Integer.parseInt(endH), Integer.parseInt(endM), endDay);
            beginTime = "00:00";
            endTime = endH + ":" + endM;
            controllerPersistence.createActivity("RutinaDeProva", name, theme, description, iniDayString, beginTime, endTime);
        }
        else if(comparison == 0) {
            routineAdapter.addActivity(name, description, Theme.valueOf(theme), Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM), iniDay);
            String beginTime = iniH + ":" + endM;
            String endTime = endH + ":" + endM;
            controllerPersistence.createActivity("RutinaDeProva", name, theme, description, iniDayString, beginTime, endTime);
        }
        else throw new InvalidDayIntervalException("Error: el dia de fi és anterior al dia d'inici");
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
     * @param iniDayString nou dia d'inici de l'activitat
     * @param endDayString nou dia de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidDayIntervalException es llença si el dia d'inici es posterior al dia de fi
     */
    public void updateActivity(String name, String description, String theme, String oldIniH, String oldIniM, String oldEndH, String oldEndM, String iniH, String iniM, String endH, String endM, String iniDayString, String endDayString) throws InvalidDayIntervalException, InvalidTimeIntervalException {
        Day iniDay = Day.valueOf(iniDayString);
        Day endDay = Day.valueOf(endDayString);
        int comparison = iniDay.compareTo(endDay); //negatiu si iniDay<endDay; 0 si iguals; positiu si iniDay>endDay
        if(comparison < 0){ //activitat en dies diferents
            routineAdapter.updateActivity(name, description, Integer.parseInt(oldIniH), Integer.parseInt(oldIniM), Integer.parseInt(oldEndH), Integer.parseInt(oldEndM), Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59, iniDay);
            routineAdapter.addActivity(name, description, Theme.valueOf(theme), 0, 0, Integer.parseInt(endH), Integer.parseInt(endM), iniDay);
            //2 crides a la DB
        }
        else if(comparison == 0) {
            routineAdapter.updateActivity(name, description, Integer.parseInt(oldIniH), Integer.parseInt(oldIniM), Integer.parseInt(oldEndH), Integer.parseInt(oldEndM), Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM), iniDay);
            //crida a la DB
        }
        else throw new InvalidDayIntervalException("Error: el dia de fi és anterior al dia d'inici");
    }

    /**
     * Metode per demanar les activitats d'un dia
     * @param dayString dia de les activitats
     * @param method metode que s'invocara quan s'hagin obtingut les activitats
     * @param object instancia on s'executara el metode
     * @throws InterruptedException
     * @throws NoSuchMethodException
     */
    public void getActivitiesByDay(String dayString, Method method, Object object) throws InterruptedException, NoSuchMethodException {
        controllerPersistence.getActivitiesByDay("RutinaDeProva", dayString, method, object);
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
        //controllerPersistence.deleteActivity("RutinaDeProva", beginTime, endTime);
    }
}
