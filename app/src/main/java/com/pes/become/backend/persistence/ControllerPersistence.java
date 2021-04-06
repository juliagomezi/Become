package com.pes.become.backend.persistence;

import java.lang.reflect.Method;

public class ControllerPersistence{

    ControllerRoutineDB CR;
    ControllerActivityDB CA;

    /**
     * Crear el controlador de rutines de la BD
     */
    public void createCtrlRoutine() {
        if (CR == null) {
            CR = new ControllerRoutineDB();
        }
    }

    /**
     * Crear el controlador d'activitats de la BD
     */
    public void createCtrlActivity() {
        if (CA == null) {
            CA = new ControllerActivityDB();
        }
    }

    /**
     * Crear una nova rutina
     * @param routineName és el nom que es vol que tingui la rutina
     */
    public void createRoutine(String routineName) {
        createCtrlRoutine();
        CR.createRoutine(routineName);
    }

    /**
     * Afegir una nova activitat a una certa rutina de la base de dades
     * @param routineName és el nom i identificador de la rutina on es vol afegir l'activitat
     * @param activityName és el nom de l'activitat que es vol afegir
     * @param actTheme és el tema de l'activitat
     * @param actDescription és la descripció de l'activitat
     * @param actDay és el dia on es vol posar l'actiivtat
     * @param beginTime és l'hora d'inici de l'activitat
     * @param finishTime és l'hora de finalització de l'activitat
     * @return el valor del id de l'activitat creada
     */
    public String createActivity(String routineName, String activityName, String actTheme,String actDescription, String actDay,
                                 String beginTime, String finishTime) {
        createCtrlActivity();
        return CA.createActivity(routineName, activityName,actTheme,actDescription, actDay, beginTime, finishTime);
    }

    /**
     * Esborrar una activitat d'una rutina
     * @param routineName és el nom i l'identificador de la rutina
     * @param idActivity és l'identificador de l'activitat
     */
    public void deleteActivity(String routineName, String idActivity) {
        createCtrlActivity();
        CA.deleteActivity( routineName, idActivity);
    }

    /**
     * Actualitzar una activitat existent en una rutina existent
     * @param routineName és el nom de la rutina ja existent
     * @param actName és el nom de l'activitat que es vol modificar
     * @param description és la nova descripció que es vol afegir a l'activitat
     * @param theme és el tema que es vol afegir a l'activitat
     * @param day és el dia de  l'activitat que es vol modificar
     * @param iniT és l'hora d'inici de l'activitat
     * @param endT és l'hora d'acabament de l'activitat
     * @param idActivity és l'identificador de l'activitat
     */
    public void updateActivity(String routineName, String actName,
                               String description, String theme,  String iniT, String endT, String day,String idActivity) {
        createCtrlActivity();
        CA.updateActivity(routineName, actName, description,
                theme, day, iniT, endT, idActivity);
    }

    /**
     * Obtenir les activitats d'una rutina i un dia indicats
     * @param routineName nom de la rutina
     * @param day dia a consultar
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivitiesByDay(String routineName, String day, Method method, Object object) {
        createCtrlActivity();
        CA.getActivitiesByDay(routineName,day,method,object);
    }

    /*
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
    //FI TUTORIAL*/
}