package com.pes.become.backend.persistence;

import android.app.Activity;
import android.net.Uri;

import java.lang.reflect.Method;

public class ControllerPersistence {

    ControllerRoutineDB CR;
    ControllerActivityDB CA;
    ControllerUserDB CU;
    ControllerStatisticsDB CS;

    /**
     * Creadora per defecte de la classe ControllerPersistence
     */
    public ControllerPersistence() {
        CA = new ControllerActivityDB();
        CR = new ControllerRoutineDB();
        CS = new ControllerStatisticsDB();
    }

    /**
     * Obtenir les activitats d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivitiesRoutine(String userId, String idRoutine, Method method, Object object) {
        CA.getActivities(userId, idRoutine, method, object);
    }

    /**
     * Obtenir les activitats d'una rutina i un dia indicats
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param day dia a consultar
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivitiesByDay(String userId, String idRoutine, String day, Method method, Object object) {
        CA.getActivitiesByDay(userId, idRoutine,day,method,object);
    }

    /**
     * Afegir una nova activitat a una certa rutina de la base de dades
     * @param userId identificador de l'usuari
     * @param idRoutine és l'identificador de la rutina on es vol afegir l'activitat
     * @param activityName és el nom de l'activitat que es vol afegir
     * @param actTheme és el tema de l'activitat
     * @param actDescription és la descripció de l'activitat
     * @param actDay és el dia on es vol posar l'actiivtat
     * @param beginTime és l'hora d'inici de l'activitat
     * @param finishTime és l'hora de finalització de l'activitat
     * @return el valor del id de l'activitat creada
     */
    public String createActivity(String userId, String idRoutine, String activityName, String actTheme,String actDescription, String actDay, String beginTime, String finishTime) {
        CS.addActivityToStatistics(userId, idRoutine, actTheme, actDay, beginTime, finishTime);
        return CA.createActivity(userId, idRoutine, activityName,actTheme,actDescription, actDay, beginTime, finishTime, "null");
    }

    /**
     * Esborrar una activitat d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param idActivity identificador de l'activitat
     * @param day dia de l'activitat que es vol borrar
     * @param theme tema de l'activitat que es vol borrar
     * @param beginTime hora d'inici de l'activitat que es vol borrar
     * @param finishTime hora final de l'activitat que es vol borrar
     */
    public void deleteActivity(String userId, String idRoutine, String idActivity, String day, String theme, String beginTime, String finishTime) {
        CS.deleteActivityStatistics(userId, idRoutine, day, theme, beginTime, finishTime);
        CA.deleteActivity(userId, idRoutine, idActivity);
    }

    /**
     * Actualitzar una activitat existent en una rutina existent
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param idActivity identificador de l'activitat
     * @param actName nou nom de l'activitat
     * @param description nova descripcio de l'activitat
     * @param newDay nou dia de l'activitat
     * @param newTheme nou tema de l'activitat
     * @param newBeginTime nova hora d'inici de l'activitat
     * @param newFinishTime nova hora final de l'activitat
     * @param oldDay vell dia de l'activitat
     * @param oldTheme vell tema de l'activitat
     * @param oldBeginTime vella hora d'inici de l'activitat
     * @param oldFinishTime vella hora final de l'activitat
     */
    public void updateActivity(String userId, String idRoutine, String idActivity, String actName, String description, String newDay, String newTheme,  String newBeginTime, String newFinishTime,
                               String oldDay, String oldTheme,  String oldBeginTime, String oldFinishTime, String lastDayDone) {

        CS.updateDedicatedTimeActivity(userId, idRoutine, newDay, newTheme, newBeginTime, newFinishTime, oldDay, oldTheme, oldBeginTime, oldFinishTime);
        CA.updateActivity(userId, idRoutine, actName, description, newTheme, newDay, newBeginTime, newFinishTime, idActivity, lastDayDone);
    }

    /**
     * Canvia el nom d'una rutina.
     * @param userId identificador de l'usuari
     * @param idRoutine l'identificador de la rutina.
     * @param newName el nom que se li vol posar a la rutina.
     */
    public void changeRoutineName(String userId, String idRoutine, String newName) {
        CR.changeName(userId, idRoutine, newName);
    }

    /**
     * Crear una nova rutina
     * @param userId identificador de l'usuari
     * @param routineName és el nom que es vol que tingui la rutina
     */
    public String createRoutine(String userId, String routineName) {
        String idRoutine = CR.createRoutine(userId, routineName);
        CS.createRoutineStatistics(userId,idRoutine);
        return idRoutine;
    }

    /**
     * Esborra la rutina indicada i les seves activitats
     * @param userId identificador de l'usuari
     * @param idRoutine és l'identificador de la rutina que es vol esborrar
     */
    public void deleteRoutine(String userId, String idRoutine) {
        CR.deleteRoutine(userId, idRoutine);
    }

    /**
     * Metode per obtenir el provider de l'usuari
     * @return el provider de l'usuari
     */
    public String getUserProvider() {
        CU = ControllerUserDB.getInstance();
        return CU.getUserProvider();
    }

    /**
     * Canvi de contrasenya de l'usuari actual
     * @param newPassword nova contrasenya
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void changePassword(String newPassword, Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.changePassword(newPassword, method, object);
    }

    /**
     * Metode per penjar una foto de perfil des de la galeria de l'usuari
     * @param userId id de l'usuari que penja la foto
     * @param imageUri uri de la imatge a penjar
     */
    public void updateProfilePic(String userId, Uri imageUri) {
        CU = ControllerUserDB.getInstance();
        CU.updateProfilePic(userId, imageUri);
    }

    /**
     * Esborrar l'usuari actual i les seves rutines i activitats
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void deleteUser(String password, Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.deleteUser(password, method, object);
    }

    /**
     * Metode per carregar un usuari
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loadUser(Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.loadUser(method, object);
    }

    /**
     * Inici de sessió d'usuari
     * @param mail correu
     * @param password contrasenya
     * @param act Activity d'Android necessaria per la execucio del firebase
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loginUser(String mail, String password, Activity act, Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.loginUser(mail, password, act, method, object);
    }

    /**
     * Inici de sessió d'usuari amb google
     * @param idToken token d'inici de sessió de Google
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loginUserGoogle(String idToken, Method method, Object object ){
        CU = ControllerUserDB.getInstance();
        CU.loginUserGoogle(idToken, method, object);
    }

    /**
     * Registre d'un nou usuari
     * @param mail correu de l'usuari que es vol crear
     * @param password contrasenya de l'usuari que es vol crear
     * @param name nom de l'usuari que es vol crear
     * @param act Activity d'Android necessaria per la execucio del firebase
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void registerUser(String mail, String password, String name, Activity act, Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.registerUser(mail, password, name, act, method, object);
    }

    /**
     * Canvia la rutina seleccionada d'un usuari
     * @param userID identificador de l'usuari
     * @param routineID identificador de la nova rutina seleccionada
     */
    public void setSelectedRoutine(String userID, String routineID) {
        CU = ControllerUserDB.getInstance();
        CU.setSelectedRoutine(userID, routineID);
    }

    /**
     * Metode per tancar la sessió
     */
    public void signOut() {
        CU = ControllerUserDB.getInstance();
        CU.signOut();
    }

    /**
     * Metode per recuperar la contrasenya d'un usuari
     * @param mail mail del compte a recuperar
     */
    public void sendPassResetEmail(String mail, Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.sendPassResetEmail(mail, method, object);
    }

}