package com.pes.become.backend.persistence;

import android.app.Activity;
import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.DeflaterOutputStream;

public class ControllerPersistence {

    ControllerRoutineDB CR;
    ControllerActivityDB CA;
    ControllerUserDB CU;
    ControllerStatisticsDB CS;
    ControllerCalendarDB CD;
    private final FirebaseFirestore db;

    /**
     * Creadora per defecte de la classe ControllerPersistence
     */
    public ControllerPersistence() {
        CA = new ControllerActivityDB();
        CR = new ControllerRoutineDB();
        CS = new ControllerStatisticsDB();
        CD = new ControllerCalendarDB();
        db = FirebaseFirestore.getInstance();
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
     */
    public void deleteActivity(String userId, String idRoutine, String idActivity) {

        DocumentReference docRefToActivity = db.collection("users").document(userId).
                collection("routines").document(idRoutine).
                collection("activities").document(idActivity);

        docRefToActivity.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String day = document.get("day").toString();
                    String theme = document.get("theme").toString();
                    String beginTime = document.get("beginTime").toString();
                    String finishTime = document.get("finishTime").toString();
                    CS.deleteActivityStatistics(userId, idRoutine, day, theme, beginTime, finishTime);
                    CA.deleteActivity(userId, idRoutine, idActivity);

                }
            }
        });
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
     */
    public void updateActivity(String userId, String idRoutine, String idActivity, String actName, String description, String newDay, String newTheme,  String newBeginTime, String newFinishTime) {

        DocumentReference docRefToActivity = db.collection("users").document(userId).
                collection("routines").document(idRoutine).
                collection("activities").document(idActivity);

        docRefToActivity.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String oldDay = document.get("day").toString();
                    String oldTheme = document.get("theme").toString();
                    String oldBeginTime = document.get("beginTime").toString();
                    String oldFinishTime = document.get("finishTime").toString();
                    CS.updateDedicatedTimeActivity(userId, idRoutine, newDay, newTheme, newBeginTime, newFinishTime, oldDay, oldTheme, oldBeginTime, oldFinishTime);
                    CA.updateActivity(userId, idRoutine, actName, description, newTheme, newDay, newBeginTime, newFinishTime, idActivity);

                }
            }
        });
    }

    /** Marca una activitat com a feta i actualitza el calendari.
     * @param userId identificador de l'usuari
     * @param idRoutine es l'identificador de la rutina ja existent
     * @param lastDayDone és l'últim dia que l'usuari ha marcat la rutina com a feta en format de data yyyy-mm-dd (la classe StringDateConverter serveix per convertir-la)
     * @param idActivity és l'identificador de l'activitat
     */
    public void markActivityAsDone(String userId, String idRoutine, String lastDayDone, String idActivity)
    {
        CA.markActivityAsDone(userId, idRoutine, lastDayDone, idActivity);
    }
/*********************************FUNCIONS RELACIONADES AMB RUTINES********************************/
    /******************************MODIFICADORES DE RUTINES***********************/
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
        CS.deleteRoutineStatistics(userId, idRoutine);
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
     * Metode per carregar un usuari
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loadUser(Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.loadUser(method, object);
    }

    /**
     * Canvi de contrasenya de l'usuari actual
     * @param newPassword nova contrasenya
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void changePassword(String oldPassword, String newPassword, Method method, Object object) {
        CU = ControllerUserDB.getInstance();
        CU.changePassword(oldPassword, newPassword, method, object);
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

    /**
     * Metode per canviar el nom de l'usuari
     * @param newName nou nom de l'usuari
     */
    public void changeUsername(String userID, String newName) {
        CU = ControllerUserDB.getInstance();
        CU.changeUsername(userID, newName);
    }

    /**
     * Funcio per aconseguir totes les estadistiques d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param method metode a cridar per retornar les dades
     * @param object classe que conte el metode
     */
    public void getAllStatisticsRoutine(String userId, String idRoutine, Method method, Object object){
        CS.getAllStatisticsRoutine(userId, idRoutine, method, object);
    }

    /**
     * Funcio per aconseguir les estadistiques d'una rutina i d'un tema concret
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param theme tema del que es vol aconseguir les estadistiques
     * @param method metode a cridar per retornar les dades
     * @param object classe que conte el metode
     */
    public void getStatisticsRoutineByTheme(String userId, String idRoutine, String theme, Method method, Object object){
        CS.getStatisticsRoutineByTheme(userId, idRoutine, theme, method, object);
    }
    /***************************FUNCIONS RELACIONADES AMB CALENDARI****************************/
    /*****************************CONSULTORES DE CALENDARI***********************/
    /**
     * Executa el metode method amb un hashmap que representa el day de la base de dades si aquest s'ha pogut consultar, o l'excepció que ha saltat si no.
     * Day tindrà les claus: day, idRoutine, numActivitiesDone, numTotalActivities. Totes son strings
     * @param userId id de l'usuari del calendari
     * @param day dia del calendari
     * @param method metode a executar
     * @param object objecte del metode a executar
     */
    public void getDay(String userId, Date day, Method method, Object object)
    {
        CD.getDay(userId, day, method, object);
    }
    /**
     * Retorna els dies de la base de dades del mes indicat
     * @param userId id de l'usuari del calendari
     * @param month més que volem seleccionar
     * @param method metode a executar
     * @param object objecte del metode a executar
     */
    public void getAvailableDays(String userId, int month, Method method, Object object)
    {
        CD.getAvailableDays(userId, month, method, object);
    }
    /*****************************MODIFICADORES DE CALENDARI***********************/

    /**
     * Crea un dia al calendari nou.
     * @param userId identificador de l'usuari
     * @param day dia a crear.
     * @param routineId id de la rutina a la que referencia
     * @param totalActivites nombre d'activitats totals del dia de la rutina que estem afegint.
     */
    public String addDay(String userId, Date day, String routineId, int totalActivites)
    {
        return CD.addDay(userId, day,  routineId, totalActivites );
    }

    /**
     * Actualitza la informació d'un dia
     * @param userId id de l'usuari del calendari
     * @param day dia del calendari
     * @param activitiesDone nou nombre d'activitats fetes (a -1 no actualitzarà res)
     * @param idRoutine nova id de la rutina a la que referencia
     */
    public void updateDay(String userId, Date day, int activitiesDone, String idRoutine)
    {
        CD.updateDay(userId, day, activitiesDone, idRoutine);
    }

    /**
     * Actualitza la informació d'un dia. ATENCIÓ: Markactivity as done ja llança aquesta funció
     * @param userId id de l'usuari del calendari
     * @param day dia del calendari
     * @param activitiesDoneIncrement incrrement del nombre d'activitats fetes
     */
    public void incrementDay(String userId, String day, int activitiesDoneIncrement)
    {
        CD.incrementDay(userId, day, activitiesDoneIncrement);
    }
}