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

    /**
     * Unica instancia de la classe
     */
    private static ControllerPersistence instance;
    private final ControllerRoutineDB CR;
    private final ControllerActivityDB CA;
    private final ControllerUserDB CU;
    private final ControllerStatisticsDB CS;
    private final ControllerCalendarDB CD;
    private final ControllerTrophiesDB CT;
    private final FirebaseFirestore db;

    /**
     * Obtenir la instancia de la classe
     * @return instancia
     */
    public static ControllerPersistence getInstance(){
        if(instance == null)
            instance = new ControllerPersistence();
        return instance;
    }

    /**
     * Creadora per defecte de la classe ControllerPersistence
     */
    private ControllerPersistence() {
        CA = ControllerActivityDB.getInstance();
        CR = ControllerRoutineDB.getInstance();
        CU = ControllerUserDB.getInstance();
        CS = ControllerStatisticsDB.getInstance();
        CD = ControllerCalendarDB.getInstance();
        CT = ControllerTrophiesDB.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    ////////////////////////////////FUNCIONS RELACIONADES AMB LA CLASSE ACTIVITAT///////////////////
    ///////////CONSULTORES DE LA CLASSE ACTIVITAT
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

    //////////MODIFICADORES DE LA CLASSE ACTIVITAT
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
     * @param shared bool que indica si la rutina es publica o no.
     * @return el valor del id de l'activitat creada
     */
    public String createActivity(String userId, String idRoutine, String activityName, String actTheme,String actDescription, String actDay, String beginTime, String finishTime, boolean shared) {
        CS.addActivityToStatistics(userId, idRoutine, actTheme, actDay, beginTime, finishTime);
        return CA.createActivity(userId, idRoutine, activityName,actTheme,actDescription, actDay, beginTime, finishTime, "null", shared);
    }

    /**
     * Esborrar una activitat d'una rutina i de la rutina publica equivalent (si existeix)
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param idActivity identificador de l'activitat
     * @param shared bool que indica si la rutina es publica o no.
     */
    public void deleteActivity(String userId, String idRoutine, String idActivity, boolean shared) {

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
                    CA.deleteActivity(userId, idRoutine, idActivity, shared);

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
     * @param shared bool que indica si la rutina es publica o no.
     */
    public void updateActivity(String userId, String idRoutine, String idActivity, String actName, String description, String newDay, String newTheme,  String newBeginTime, String newFinishTime, boolean shared) {

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
                    CA.updateActivity(userId, idRoutine, actName, description, newTheme, newDay, newBeginTime, newFinishTime, idActivity, shared);

                }
            }
        });
    }

    /** Marca una activitat com a feta i actualitza el calendari.
     * @param userId identificador de l'usuari
     * @param idRoutine es l'identificador de la rutina ja existent
     * @param lastDayDone és l'últim dia que l'usuari ha marcat la rutina com a feta en format de data yyyy-mm-dd (la classe StringDateConverter serveix per convertir-la)
     * @param idActivity és l'identificador de l'activitat
     * @param totalActivities nombre total d'activitats del dia per la rutina seleccionada
     */
    public void markActivityAsDone(String userId, String idRoutine, String lastDayDone, String idActivity, int totalActivities)
    {
        CA.markActivityAsDone(userId, idRoutine, lastDayDone, idActivity, totalActivities);
    }
    ////////////////////////////////FUNCIONS RELACIONADES AMB LA CLASSE RUTINA//////////////////////
    /**
     * Canvia el nom d'una rutina.
     * @param userId identificador de l'usuari
     * @param idRoutine l'identificador de la rutina.
     * @param newName el nom que se li vol posar a la rutina.
     * @param shared bool que indica si la rutina es publica o no.
     */
    public void changeRoutineName(String userId, String idRoutine, String newName, boolean shared) {
        CR.changeName(userId, idRoutine, newName, shared);
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
     * @param shared bool que indica si la rutina es publica o no.
     */
    public void deleteRoutine(String userId, String idRoutine, boolean shared) {
        CR.deleteRoutine(userId, idRoutine, shared);
        CS.deleteRoutineStatistics(userId, idRoutine);
    }

    /**
     * Afegeix la rutina compartida a la col·leccio de rutines de l'usuari
     * @param userId identificador de l'usuari que vol descarregar la rutina
     * @param idSharedRoutine identificador de la rutina publica
     */
    public void downloadSharedRoutine(String userId, String idSharedRoutine){
        CR.downloadSharedRoutine(userId, idSharedRoutine);
    }

    /**
     * Funcio que fa que una rutina passi a ser publica.
     * @param userId identificador de l'usuari.
     * @param idRoutine l'identificador de la rutina.
     */
    public void shareRoutine(String userId, String idRoutine) {
        CR.shareRoutine(userId, idRoutine);
    }

    /**
     * Funcio que fa que l'atribut shared de la rutina "local" sigui false i esborra la rutina publica.
     * @param userId identificador de l'usuari.
     * @param idRoutine l'identificador de la rutina.
     */
    public void unShareRoutine(String userId, String idRoutine) {
        CR.unShareRoutine(userId, idRoutine);
    }

    /**
     * Funcio per a votar una certa rutina publica
     * @param userId identificador de l'usuari que vol votar
     * @param idSharedRoutine identificador de la rutina publica
     * @param average la nova mitjana de puntuacio de la rutina publca
     */
    public void voteRoutine(String userId, String idSharedRoutine, double average){
        CR.voteRoutine(userId, idSharedRoutine, average);
    }

    ////////////////////////////////FUNCIONS RELACIONADES AMB LA CLASSE USUARI//////////////////////
    /**
     * Metode per obtenir el provider de l'usuari
     * @return el provider de l'usuari
     */
    public String getUserProvider() {
        return CU.getUserProvider();
    }

    /**
     * Metode per carregar un usuari
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loadUser(Method method, Object object) {
        CU.loadUser(method, object);
    }

    /**
     * Canvi de contrasenya de l'usuari actual
     * @param newPassword nova contrasenya
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void changePassword(String oldPassword, String newPassword, Method method, Object object) {
        CU.changePassword(oldPassword, newPassword, method, object);
    }

    /**
     * Metode per penjar una foto de perfil des de la galeria de l'usuari
     * @param userId id de l'usuari que penja la foto
     * @param imageUri uri de la imatge a penjar
     */
    public void updateProfilePic(String userId, Uri imageUri) {
        CU.updateProfilePic(userId, imageUri);
    }

    /**
     * Esborrar l'usuari actual i les seves rutines i activitats
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void deleteUser(String password, Method method, Object object) {
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
        CU.loginUser(mail, password, act, method, object);
    }

    /**
     * Inici de sessió d'usuari amb google
     * @param idToken token d'inici de sessió de Google
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loginUserGoogle(String idToken, Method method, Object object ){
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
        CU.registerUser(mail, password, name, act, method, object);
    }

    /**
     * Canvia la rutina seleccionada d'un usuari
     * @param userID identificador de l'usuari
     * @param routineID identificador de la nova rutina seleccionada
     */
    public void setSelectedRoutine(String userID, String routineID) {
        CU.setSelectedRoutine(userID, routineID);
    }

    /**
     * Metode per tancar la sessió
     */
    public void signOut() {
        CU.signOut();
    }

    /**
     * Metode per recuperar la contrasenya d'un usuari
     * @param mail mail del compte a recuperar
     */
    public void sendPassResetEmail(String mail, Method method, Object object) {
        CU.sendPassResetEmail(mail, method, object);
    }

    /**
     * Metode per canviar el nom de l'usuari
     * @param newName nou nom de l'usuari
     */
    public void changeUsername(String userID, String newName) {
        CU.changeUsername(userID, newName);
    }

    ///////////////////////////////FUNCIONS RELACIONADES AMB LA CLASSE STATISTICS///////////////////
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
    ///////////////////////////FUNCIONS RELACIONADES AMB LA CLASSE CALENDAR/////////////////////////
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
    public void getAvailableDays(String userId, int month, int year, Method method, Object object)
    {
        CD.getAvailableDays(userId, month, year, method, object);
    }

    /**
     * Retorna els dies de la base de dades
     * @param userId id de l'usuari del calendari
     * @param method metode a executar
     * @param object objecte del metode a executar
     */
    public void getAllDays(String userId, Method method, Object object)
    {
        CD.getAllDays(userId, method, object);
    }

    /**
     * Retorna un enter amb el nombre de dies en ratxa que porta l'usuari
     * @param userId id de l'usuari del calendari
     * @param method metode a executar
     * @param object objecte del metode a executar
     */
    public void getStreak(String userId, Method method, Object object)
    {
        CD.getStreak(userId,method,object);
    }

    /**
     * Crea un dia al calendari nou.
     * @param userId identificador de l'usuari
     * @param day dia a crear.
     * @param routineId id de la rutina a la que referencia
     * @param totalActivities nombre d'activitats totals del dia de la rutina que estem afegint.
     */
    public String addDay(String userId, Date day, String routineId, int totalActivities)
    {
        return CD.addDay(userId, day,  routineId, totalActivities );
    }

    /**
     * Actualitza la informació d'un dia
     * @param userId id de l'usuari del calendari
     * @param day dia del calendari
     * @param activitiesDone nou nombre d'activitats fetes (a -1 no actualitzarà res)
     * @param idRoutine nova id de la rutina a la que referencia
     */
    public void updateDay(String userId, Date day, int activitiesDone, String idRoutine) {
        CD.updateDay(userId, day, activitiesDone, idRoutine);
    }
    //////////////////////////////FUNCIONS RELACIONADES AMB LA CLASSE TROPHIES//////////////////////
    /**
     * Funcio que retorna els trofeus de l'usuari i un bool que indica per cada un si l'ha aconseguit
     * @param userId identificador de l'usuari
     * @param method metode que recull les dades
     * @param object classe que conte el metode
     */
    public void getTrophies(String userId, Method method, Object object) {
        CT.getTrophies(userId, method,object);
    }

    /**
     * Funcio que afegeix un trofeu a l'usuari
     * @param userId nom de l'usuari
     * @param trophyName nom del trofeu
     */
    public void addTrophy(String userId, String trophyName) {
        CT.addTrophy(userId, trophyName);
    }

}