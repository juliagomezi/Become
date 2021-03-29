package com.pes.become.backend.persistence;


import java.lang.reflect.Method;

public class ControllerPersistence{

    ControllerRoutineDB CR;
    ControllerActivityDB CA;

    /** Brief: Funció per a crear el controlador de rutines de la BD.
     * Pre: Cert.
     * Post: Si el controlador de rutina no estava creat ja, el crea.
     */
    public void createCtrlRoutine(){
        if (CR==null){
            CR = new ControllerRoutineDB();
        }
    }

    /** Brief: Funció per a crear el controlador d'activitats de la BD.
     * Pre: Cert.
     * Post: Si el controlador de rutina no estava creat ja, el crea.
     */
    public void createCtrlActivity(){
        if (CA==null){
            CA = new ControllerActivityDB();
        }
    }

    /** Brief: Funció que crea una nova rutina.
     * Pre: No hi ha cap rutina que tingui "routineName" com a nom.
     * @param routineName és el nom que es vol que tingui la rutina.
     * Post: Es crea una nova rutina.
     */
    public void createNewRoutine(String routineName){
        createCtrlRoutine();
        CR.createRoutine(routineName);
    }


    // Creadores Activity
    /**Brief: Funció que afegeix una nova activitat a una certa rutina de la base de dades.
     * Pre: La rutina que té com a nom routineName ja existeix.
     * Pre: L'interval de temps  de l'activitat no se sol·lapa amb cap altra activitat de
     * la mateixa rutina.
     * @param routineName és el nom i identificador de la rutina on es vol afegir l'activitat.
     * @param activityName és el nom de l'activitat que es vol afegir.
     * @param actTheme és el tema de l'activitat.
     * @param actDescription és la descripció de l'activitat.
     * @param actDay és el dia on es vol posar l'actiivtat.
     * @param beginTime és l'hora d'inici de l'activitat.
     * @param finishTime és l'hora de finalització de l'activitat.
     * Post: Es crea una nova activitat a la rutina i retorna el seu identificador.
     */
    public String createActivity(String routineName, String activityName, String actTheme,String actDescription, String actDay,
                                 String beginTime, String finishTime)  {
        createCtrlActivity();
        return CA.createActivity(routineName, activityName,actTheme,actDescription, actDay, beginTime, finishTime);
    }

    /** Brief: Funció que esborra una activitat d'una rutina.
     * Pre: Cert.
     * @param routineName és el nom i l'identificador de la rutina.
     * @param idActivity és l'identificador de l'activitat.
     * Post: Esborra l'activitat de la rutina.
     */
    public void deleteActivity(String routineName, String idActivity) {
        createCtrlActivity();
        CA.deleteActivity( routineName, idActivity);
    }

    public void updateActivity(String routineName, String actName,
                               String description, String theme,  String iniT, String endT, String day,String idActivity){
        createCtrlActivity();
        CA.updateActivity(routineName, actName, description,
                theme, day, iniT, endT, idActivity);
    }

    //Consultores activity
    public void getActivitiesByDay(String routineName, String day,Method method, Object object) {
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