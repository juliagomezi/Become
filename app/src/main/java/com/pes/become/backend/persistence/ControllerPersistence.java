package com.pes.become.backend.persistence;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
    public void createActivity(String routineName, String activityName, String actTheme,String actDescription, String actDay,
                               String beginTime, String finishTime)  {
        createCtrlActivity();
        CA.createActivity(routineName, activityName,actTheme,actDescription, actDay, beginTime, finishTime);
    }

    public void deleteActivity(String routineName, String beginTime, String finishTime) {
        createCtrlActivity();
        CA.deleteActivity( routineName,  beginTime, finishTime);
    }

    public void updateActivity(String routineName, String actName, String description, String theme, String oldIniTime,  String oldEndTime, String iniT, String endT, String day){
        createCtrlActivity();
        CA.updateActivity(routineName, actName, description, theme, oldIniTime, oldEndTime, iniT, endT, day);
    }

    //Consultores activity
    public void getActivitiesByDay(String routineName, String day,Method method, Object object) throws InterruptedException, NoSuchMethodException {
        createCtrlActivity();
        CA.getActivitiesByDay(routineName,day,method,object);
    }

    //TUTORIAL
    public void tutorial(String routineName, String day) throws InterruptedException, NoSuchMethodException {

        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = ControllerPersistence.class.getMethod("dothingsWithActivity", parameterTypes);
        this.getActivitiesByDay(routineName,day,method1,this);
    }

    public void dothingsWithActivity(ArrayList<String> message)
    {
        for(String a: message) Log.d("DoThingsWithActivity", a);
    }
    //FI TUTORIAL
}