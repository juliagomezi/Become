package com.pes.become.backend.persistence;

import java.lang.reflect.Method;

public class ControllerPersistence{

    ControllerRoutineDB CR;
    ControllerActivityDB CA;



    /**
     * Crear el controlador d'activitats de la BD
     */
    private void createCtrlActivity() {
        if (CA == null) {
            CA = new ControllerActivityDB();
        }
    }

    /**
     * Crear el controlador de rutines de la BD
     */
    private void createCtrlRoutine() {
        if (CR == null) {
            CR = new ControllerRoutineDB();
        }
    }




    /**FUNCIONS RELACIONADES AMB ACTIVITAT*********************************************************/
    /***************CONSULTORES***************/

    /**
     * Obtenir les activitats d'una rutina i un dia indicats
     * @param idRoutine identificador de la rutina
     * @param day dia a consultar
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivitiesByDay(String idRoutine, String day, Method method, Object object) {
        createCtrlActivity();
        CA.getActivitiesByDay(idRoutine,day,method,object);
    }


    /***************MODIFICADORES***************/


    /**
     * Afegir una nova activitat a una certa rutina de la base de dades
     * @param idRoutine és l'identificador de la rutina on es vol afegir l'activitat
     * @param activityName és el nom de l'activitat que es vol afegir
     * @param actTheme és el tema de l'activitat
     * @param actDescription és la descripció de l'activitat
     * @param actDay és el dia on es vol posar l'actiivtat
     * @param beginTime és l'hora d'inici de l'activitat
     * @param finishTime és l'hora de finalització de l'activitat
     * @return el valor del id de l'activitat creada
     */
    public String createActivity(String idRoutine, String activityName, String actTheme,String actDescription, String actDay, String beginTime, String finishTime) {
        createCtrlActivity();
        return CA.createActivity(idRoutine, activityName,actTheme,actDescription, actDay, beginTime, finishTime);
    }

    /**
     * Esborrar una activitat d'una rutina
     * @param idRoutine és el nom i l'identificador de la rutina
     * @param idActivity és l'identificador de l'activitat
     */
    public void deleteActivity(String idRoutine, String idActivity) {
        createCtrlActivity();
        CA.deleteActivity(idRoutine, idActivity);
    }

    /**
     * Actualitzar una activitat existent en una rutina existent
     * @param idRoutine és l'identificador de la rutina ja existent
     * @param actName és el nom de l'activitat que es vol modificar
     * @param description és la nova descripció que es vol afegir a l'activitat
     * @param theme és el tema que es vol afegir a l'activitat
     * @param day és el dia de  l'activitat que es vol modificar
     * @param iniT és l'hora d'inici de l'activitat
     * @param endT és l'hora d'acabament de l'activitat
     * @param idActivity és l'identificador de l'activitat
     */
    public void updateActivity(String idRoutine, String actName, String description, String theme,  String iniT, String endT, String day,String idActivity) {
        createCtrlActivity();
        CA.updateActivity(idRoutine, actName, description, theme, day, iniT, endT, idActivity);
    }





    /**FUNCIONS RELACIONADES AMB RUTINA************************************************************/

    /***************MODIFICADORES***************/

    /**
     * Canvia el nom d'una rutina.
     * @param idRoutine l'identificador de la rutina.
     * @param newName el nom que se li vol posar a la rutina.
     */
    public void changeRoutineName(String idRoutine, String newName){
        createCtrlRoutine();
        CR.changeName(idRoutine, newName);
    }

    /**
     * Crear una nova rutina
     * @param routineName és el nom que es vol que tingui la rutina
     */
    public String createRoutine(String routineName) {
        createCtrlRoutine();
        return CR.createRoutine(routineName);
    }


    /**
     * Esborra la rutina indicada i les seves activitats
     * @param idRoutine és l'identificador de la rutina que es vol esborrar
     */

    public void deleteRoutine(String idRoutine){
        createCtrlRoutine();
        CR.deleteRoutine(idRoutine);
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