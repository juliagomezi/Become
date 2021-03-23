package com.pes.become.backend.persistence;

import android.util.Log;

import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;
import java.lang.reflect.Method;

public class ControllerPersistence{

    ControllerRoutineDB controllerRoutine;
    ControllerActivityDB controllerActivity;

    /**Brief: funció que crea el controlador de rutina.
     * Pre: cert.
     * Post: si el controlador de rutina no estava creat ja, el crea.
     */
    public void createCtrlRoutine() {
        if (controllerRoutine == null) controllerRoutine = new ControllerRoutineDB();
    }

    /**Brief: funció que crea el controlador d'activitat.
     * Pre: cert.
     * Post: si el controlador de d'activitat no estava creat ja, el crea.
     */
    public void createCtrlActivity() {
        if (controllerActivity == null) controllerActivity = new ControllerActivityDB();
    }

    /**Brief: funció que afegeix una nova rutina a la base de dades.
     * Pre: ninguna
     * @param name és el nom de la rutina
     * Post: Es crea una nova rutina.
     */
    public void createRoutine(String name) {
        createCtrlRoutine();
        controllerRoutine.createRoutine(name);
    }

    /**Brief: funció que afegeix una nova activitat a una certa rutina de la base de dades.
     * Pre: la rutina que té com a nom routineName ja existeix
     * Pre: l'interval de temps  de l'activitat no se sol·lapa amb cap altra activitat de
     * la mateixa rutina.
     * @param routineName és el nom de la rutina on es vol afegir l'activitat.
     * @param activityName és el nom de l'activitat que es vol afegir.
     * @param activityTheme és el tema de l'activitat.
     * @param activityDescription és la descripció de l'activitat.
     * @param activityDay és el dia on es vol posar l'actiivtat.
     * @param activityStartTime és l'hora d'inici de l'activitat.
     * @param activityEndTime és l'hora de finalització de l'activitat.
     * Post: Es crea una nova activitat a la rutina.
     */
    public void createActivity(String routineName, String activityName, String activityTheme,String activityDescription, String activityDay, String activityStartTime, String activityEndTime) throws OverlappingActivitiesException, InvalidTimeException {
        createCtrlActivity();
        controllerActivity.createActivity(routineName, activityName, activityTheme, activityDescription, activityDay, activityStartTime, activityEndTime);
    }

    /**Brief: funció que obté les activitats d'una certa rutina d'un dia concret de la base de dades.
     * Pre: la rutina que té com a nom routineName ja existeix
     * Pre: el dia és vàlid
     * @param routineName és el nom de la rutina que es vol consultar.
     * @param routineDay és el dia que es vol consultar.
     * Post: Retorna un String[][] amb la informació de totes les activitats de la rutina en el dia.
     */
    public String[][] getActivitiesByDay(String routineName, String routineDay) throws InterruptedException, NoSuchMethodException {
        createCtrlActivity();

        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = String.class;
        Method method1 = ControllerPersistence.class.getMethod("dothingsWithActivity", parameterTypes);
        //controllerActivity.getActivitiesByDay(routineName, routineDay);
        return null;
    }

    public void dothingsWithActivity(String message) {
        Log.d("DoThingsWithActivity", message);
    }
}