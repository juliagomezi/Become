package com.pes.become.backend.persistence;

import android.app.Activity;
import android.net.Uri;

import java.lang.reflect.Method;

public class ControllerPersistence {

    ControllerRoutineDB CR;
    ControllerActivityDB CA;
    ControllerUserDB CU;

    /**
     * Creadora per defecte de la classe ControllerPersistence
     */
    public ControllerPersistence() {
        CA = new ControllerActivityDB();
        CR = new ControllerRoutineDB();
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
        return CA.createActivity(userId, idRoutine, activityName,actTheme,actDescription, actDay, beginTime, finishTime);
    }

    /**
     * Esborrar una activitat d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine és el nom i l'identificador de la rutina
     * @param idActivity és l'identificador de l'activitat
     */
    public void deleteActivity(String userId, String idRoutine, String idActivity) {
        CA.deleteActivity(userId, idRoutine, idActivity);
    }

    /**
     * Actualitzar una activitat existent en una rutina existent
     * @param userId identificador de l'usuari
     * @param idRoutine és l'identificador de la rutina ja existent
     * @param actName és el nom de l'activitat que es vol modificar
     * @param description és la nova descripció que es vol afegir a l'activitat
     * @param theme és el tema que es vol afegir a l'activitat
     * @param day és el dia de  l'activitat que es vol modificar
     * @param iniT és l'hora d'inici de l'activitat
     * @param endT és l'hora d'acabament de l'activitat
     * @param idActivity és l'identificador de l'activitat
     */
    public void updateActivity(String userId, String idRoutine, String actName, String description, String theme,  String iniT, String endT, String day,String idActivity) {
        CA.updateActivity(userId, idRoutine, actName, description, theme, day, iniT, endT, idActivity);
    }

    /**
     * Funció per obtenir el nom de la rutina amb routineId
     * @param userId Id de l'usuari
     * @param routineId Id de l'usuari
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getUserRoutine(String userId, String routineId, Method method, Object object) {
        CR.getUserRoutine(userId, routineId, method, object);
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
        return CR.createRoutine(userId, routineName);
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
     * Metode per obtenir la informació de l'usuari
     * @param userID identificador de l'usuari
     * @param method metode a cridar quan s'obtingui la resposta
     * @param object classe que conte el metode
     */
    public void getInfoUser(String userID, Method method, Object object){
        CU = ControllerUserDB.getInstance();
        CU.getInfoUser(userID, method, object);
    }

    /**
     * Metode per obtenir la rutina seleccionada de l'usuari
     * @param userID identificador de l'usuari
     * @param method metode a cridar quan s'obtingui la resposta
     * @param object classe que conte el metode
     */
    public void getSelectedRoutine(String userID, Method method, Object object){
        CU = ControllerUserDB.getInstance();
        CU.getSelectedRoutine(userID, method, object);
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
    public void deleteUser(Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.deleteUser(method, object);
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
     * Reautenticació de l'usuari
     * @param mail correu
     * @param password contrasenya
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void reauthenticate(String mail, String password, Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.reauthenticate(mail, password, method, object);
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
     * Metode per carregar un usuari
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loadUser(Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.loadUser(method, object);
    }
}