package com.pes.become.backend.persistence;

import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;

public class ControllerPersistence{

    ControllerRoutineDB CR;
    ControllerActivityDB CA;

    /**
     * Pre: cert.
     * Post: si el controlador de rutina no estava creat ja, el crea.
     */
    public void createCtrlRoutine(){
        if (CR==null){
            CR = new ControllerRoutineDB();
        }
    }

    /**
     * Pre: cert.
     * Post: si el controlador de rutina no estava creat ja, el crea.
     */
    public void createCtrlActivity(){
        if (CA==null){
            CA = new ControllerActivityDB();
        }
    }

    /**
     * Pre: cert.
     * Param: no hi ha cap rutina que tingui "routineName" com a nom.
     * Post: si el controlador de rutina no estava creat ja, el crea.
     */
    public void createNewRoutine(String routineName){
        createCtrlRoutine();
        CR.createRoutine(routineName);
    }


    // Creadores Activity
    /**Brief: funció que afegeix una nova activitat a una certa rutina de la base de dades.
     * Pre: la rutina que té com a nom routineName ja existeix
     * Pre: l'interval de temps  de l'activitat no se sol·lapa amb cap altra activitat de
     * la mateixa rutina.
     * @param routineName és el nom de la rutina on es vol afegir l'activitat.
     * @param activityName és el nom de l'activitat que es vol afegir.
     * @param actTheme és el tema de l'activitat.
     * @param actDescription és la descripció de l'activitat.
     * @param actDay és el dia on es vol posar l'actiivtat.
     * @param beginTime és l'hora d'inici de l'activitat.
     * @param finishTime és l'hora de finalització de l'activitat.
     * Post: Es crea una nova activitat a la rutina.
     */
    public void createActivity(String routineName, String activityName, String actTheme,String actDescription,
                               String actDay, String beginTime, String finishTime) throws OverlappingActivitiesException, InvalidTimeException {
        createCtrlActivity();
        CA.createActivity(routineName, activityName,actTheme,actDescription, actDay, beginTime, finishTime);
    }
    //Consultores activity
    public void getActivitiesByDay(String routineName, String day) throws InterruptedException {
        createCtrlActivity();
        CA.getActivitiesByDay(routineName,day);
    }
}