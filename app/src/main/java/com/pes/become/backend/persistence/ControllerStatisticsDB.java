package com.pes.become.backend.persistence;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ControllerStatisticsDB {

    private final FirebaseFirestore db;
    private final String[] differentThemes;
    private final String[] differentDays;
    private final int numberOfThemes;


    /**
     * Creadora per defecte de la classe ControllerStatisticsDB
     */
    public ControllerStatisticsDB() {
        db = FirebaseFirestore.getInstance();
        differentThemes = new String[]{"Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other"};
        differentDays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        numberOfThemes = 8;
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

        Object[] params = new Object[1];

        HashMap <String, HashMap<String, Double>> mapThemes = new HashMap<>();

        docRefToRoutineStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    for (int i = 0; i<numberOfThemes; ++i){

                        mapThemes.put(differentThemes[i],(HashMap) document.get("statistics" + differentThemes[i]));
                    }
                    params[0] = mapThemes;

                    try {
                        method.invoke(object, params);
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

        Object[] params = new Object[1];

        docRefToRoutineStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {


                    params[0] = (HashMap) document.get("statistics" + theme);

                    try {
                        method.invoke(object, params);
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
     * Afegeix les estadistiques d'una nova activitat al conjunt d'estadistiques-----AQUESTA SI QUE FUNCIONAAAAAAAAAAAAAAAAAAAAAAAAA
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param theme tema de l'activitat
     * @param day dia de l'activitat
     * @param beginTime temps d'inici de l'activitat
     * @param finishTime temps d'acabament de l'activitat
     */
    public void addActivityToStatistics(String userId, String idRoutine, String theme, String day, String beginTime, String finishTime) {

        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);


        docRefToRoutineStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    double timeToAdd = timeDifference(beginTime, finishTime);
                    HashMap<String, Double> mapActTheme = (HashMap) document.get("statistics" + theme);
                    mapActTheme.put(day, mapActTheme.get(day) + timeToAdd);
                    docRefToRoutineStatistics.update("statistics" + theme, mapActTheme);

                }
            }
        });
    }

    /**
     * Es crea les estadistiques de la rutina que s'acaba de crear ----------AQUESTA SI QUE FUNCIONAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     */
    public void createRoutineStatistics(String userId, String idRoutine){

        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);

        HashMap <String, Object> dataInput = new HashMap<>();
        HashMap <String, Double> mapStatistics = new HashMap<>();


        for (int i = 0; i<7; ++i){
            mapStatistics.put(differentDays[i], 0.0);
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

        docRefToStatistics.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        double hoursToDelete = timeDifference(beginTime, finishTime);
                        HashMap<String, Double> mapActTheme = (HashMap) document.get("statistics" + theme);
                        mapActTheme.put(day, mapActTheme.get(day) - hoursToDelete);
                        docRefToStatistics.update("statistics" + theme, mapActTheme);
                    }
                }
            }
        });

    }

    /**
     *
     * @param userId
     * @param idRoutine
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

        docRefToStatistics.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
                                //Posar les noves hores al mateix mapa i al tema diferent corresponent
                                mapOldTheme.put(newDay, mapOldTheme.get(newDay) + newActivityHours);
                            }

                            else {
                                //Posar les noves hores al mapa del dia diferent corresponent i al tema corresponent
                                HashMap<String, Double> mapNewTheme = (HashMap) document.get("statistics" + newTheme);
                                mapNewTheme.put(newDay, mapNewTheme.get(newDay) + newActivityHours);
                                 docRefToStatistics.update("statistics" + newTheme, mapNewTheme);
                            }
                        }
                        docRefToStatistics.update("statistics" + oldTheme, mapOldTheme);

                    }
                }
            }
        });
    }

    /**
     *
     * @param beginTime
     * @param finishTime
     * @return
     */
    private double timeDifference(String beginTime, String finishTime) {
        //return finishTime-beginTime
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
