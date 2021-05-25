package com.pes.become.backend.persistence;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ControllerStatisticsDB {

    /**
     * Unica instancia de la classe
     */
    private static ControllerStatisticsDB instance;
    private final FirebaseFirestore db;
    private final String[] differentThemes;
    private final String[] differentDays;
    private final int numberOfThemes;

    /**
     * Creadora per defecte de la classe ControllerStatisticsDB
     */
    private ControllerStatisticsDB() {
        db = FirebaseFirestore.getInstance();
        differentThemes = new String[]{"Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other"};
        differentDays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        numberOfThemes = 8;
    }

    /**
     * Obtenir la instancia de la classe
     * @return instancia
     */
    public static ControllerStatisticsDB getInstance() {
        if(instance == null)
            instance = new ControllerStatisticsDB();
        return instance;
    }

    /**
     * Funcio que retorna les estadistiques de tots els temes d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conte el metode
     */
    public void getAllStatisticsRoutine(String userId, String idRoutine, Method method, Object object){
        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);
        HashMap <String, HashMap<String, Double>> mapThemes = new HashMap<>();

        docRefToRoutineStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    for (int i = 0; i<numberOfThemes; ++i){

                        mapThemes.put(differentThemes[i],(HashMap) document.get("statistics" + differentThemes[i]));
                    }

                    try {
                        method.invoke(object, mapThemes);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Funcio que retorna les estadistiques d'una rutina sobre un tema concret
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param theme tema de les estadistiques que es volen aconseguir
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conte el metode
     */
    public void getStatisticsRoutineByTheme(String userId, String idRoutine, String theme, Method method, Object object){
        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);

        docRefToRoutineStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    try {
                        method.invoke(object, document.get("statistics" + theme));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Afegeix les estadistiques d'una nova activitat al conjunt d'estadistiques
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param theme tema de l'activitat
     * @param day dia de l'activitat
     * @param beginTime temps d'inici de l'activitat
     * @param finishTime temps d'acabament de l'activitat
     */
    public void addActivityToStatistics(String userId, String idRoutine, String theme, String day, String beginTime, String finishTime) {
        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);
        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(docRefToRoutineStatistics);
            double timeToAdd = timeDifference(beginTime, finishTime);
            HashMap<String, Double> mapActTheme = (HashMap) snapshot.get("statistics" + theme);
            mapActTheme.put(day, mapActTheme.get(day) + timeToAdd);
            transaction.update(docRefToRoutineStatistics, "statistics" + theme, mapActTheme);
            return null;
        });
    }

    /**
     * Es crea les estadistiques de la rutina que s'acaba de crear
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     */
    public void createRoutineStatistics(String userId, String idRoutine){
        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);
        HashMap <String, Object> dataInput = new HashMap<>();
        HashMap <String, Double> mapStatistics = new HashMap<>();

        for (int i = 0; i<7; ++i){
            double zero = 0.0;
            mapStatistics.put(differentDays[i], zero);
        }
        for (int j = 0; j<numberOfThemes; ++j){
            dataInput.put("statistics" + differentThemes[j], mapStatistics);
        }

        docRefToRoutineStatistics.set(dataInput);
    }

    /**
     * Esborra les estadistiques d'una activitat d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param day dia de l'activitat que es vol borrar
     * @param theme tema de l'activitat que es vol borrar
     * @param beginTime hora d'inici de l'activitat que es vol borrar
     * @param finishTime hora final de l'activitat que es vol borrar
     */
    public void deleteActivityStatistics(String userId, String idRoutine, String day, String theme, String beginTime, String finishTime){
        DocumentReference docRefToStatistics = db.collection("users").
                document(userId).collection("statistics").document(idRoutine);

        docRefToStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    double hoursToDelete = timeDifference(beginTime, finishTime);
                    HashMap<String, Double> mapActTheme = (HashMap) document.get("statistics" + theme);
                    mapActTheme.put(day, mapActTheme.get(day) - hoursToDelete);
                    docRefToStatistics.update("statistics" + theme, mapActTheme);
                }
            }
        });
    }

    /**
     * Metode per esborrar les estadístiques d'una rutina
     * @param userId identificador de l'usuari propietari de la rutina
     * @param idRoutine identificador de la rutina
     */
    public void deleteRoutineStatistics(String userId, String idRoutine){
        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);
        docRefToRoutineStatistics.delete();
    }

    /**
     * Funcio per actualitzar les estadistiques d'una rutina quan s'ha modificat una de les seves activitats
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param newDay nou dia de l'activitat
     * @param newTheme nou tema de l'activitat
     * @param newBeginTime nou hora d'inici de l'activitat
     * @param newFinishTime nou hora d'acabament de l'activitat
     * @param oldDay vell dia de l'activitat
     * @param oldTheme nou tema de l'activitat
     * @param oldBeginTime vell hora d'inici de l'activitat
     * @param oldFinishTime vell hora d'acabament de l'activitat
     */
    public void updateDedicatedTimeActivity(String userId, String idRoutine, String newDay, String newTheme,  String newBeginTime,
                                            String newFinishTime, String oldDay, String oldTheme,  String oldBeginTime, String oldFinishTime) {

        DocumentReference docRefToStatistics = db.collection("users").document(userId).
                collection("statistics").document(idRoutine);

        docRefToStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    double newActivityHours = timeDifference(newBeginTime, newFinishTime);
                    double oldActivityHours = timeDifference(oldBeginTime, oldFinishTime);
                    boolean sameDay = oldDay.equals(newDay);
                    boolean sameTheme = oldTheme.equals(newTheme);

                    HashMap<String, Double> mapOldTheme = (HashMap) document.get("statistics" + oldTheme);
                    if (sameDay && sameTheme){
                        double difference = newActivityHours - oldActivityHours;
                        mapOldTheme.put(oldDay, mapOldTheme.get(oldDay) + difference);
                    }

                    else {

                        mapOldTheme.put(oldDay, mapOldTheme.get(oldDay) - oldActivityHours);

                        if (sameTheme){
                            mapOldTheme.put(newDay, mapOldTheme.get(newDay) + newActivityHours);
                        }

                        else {
                            HashMap<String, Double> mapNewTheme = (HashMap) document.get("statistics" + newTheme);
                            mapNewTheme.put(newDay, mapNewTheme.get(newDay) + newActivityHours);
                             docRefToStatistics.update("statistics" + newTheme, mapNewTheme);
                        }
                    }
                    docRefToStatistics.update("statistics" + oldTheme, mapOldTheme);
                }
            }
        });
    }

    /**
     * Calcula la diferencia de temps
     * @param beginTime hora d'inici
     * @param finishTime hora de fi
     * @return la diferencia entre hora d'inici i hora de fi
     */
    private double timeDifference(String beginTime, String finishTime) {
        double beginHour = Double.parseDouble(beginTime.substring(0,2));
        double beginMinute = Double.parseDouble(beginTime.substring(3));
        double finishHour = Double.parseDouble(finishTime.substring(0,2));
        double finishMinute = Double.parseDouble(finishTime.substring(3));
        double hours = finishHour - beginHour;
        double minutes = (finishMinute - beginMinute)/60.0;
        if (minutes<0.0){
            hours = hours - 1.0;
            minutes = minutes * -1.0;
        }
        return hours + minutes;
    }

}
