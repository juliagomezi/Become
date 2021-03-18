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

    /**
     * Pre: activityName existeix a la base de dades
     * Post: Es crea una nova activitat a la rutina
     * @param routineName a
     * @param activityName a
     * @param actTheme a
     * @param actDescription a
     * @param actDay a
     * @param beginTime a
     * @param finishTime a
     */
    public void createActivity(String routineName, String activityName, String actTheme,String actDescription, String actDay, String beginTime, String finishTime)
    {
        createCtrlActivity();
        CA.createActivity(routineName, activityName,actTheme,actDescription, actDay, beginTime, finishTime);
    }
    public void getActivitiesByDay(String routineName, String day) {
        createCtrlActivity();
        CA.getActivitiesByDay(routineName,day);
    }

}
