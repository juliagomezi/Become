package com.pes.become.backend.persistence;


/*

public class ControllerPersistence {
    ControllerRoutineDB CR;
    ControllerActivityDB CA;

    public ControllerPersistence(){}

    public ControllerRoutineDB getCtrlRoutine(){
        return new ControllerRoutineDB();
    }

    public ControllerActivityDB getCtrlActivity(){
        return new ControllerActivityDB();
    }
}

*/

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
}
