package com.pes.become.backend.persistence;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControllerStatisticsDB {

    private final FirebaseFirestore db;

    /**
     * Creadora per defecte de la classe ControllerStatisticsDB
     */
    public ControllerStatisticsDB() { db = FirebaseFirestore.getInstance(); }

    /********************************CONSULTORES DE STATISTICS*************************************/

    /**
     * Funcio que retorna les estadistiques de tots els dies d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conte el metode
     */
    public void getAllStatisticsRoutine(String userId, String idRoutine, Method method, Object object){

        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        Object[] params = new Object[7];

        docRefToRoutineStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    for (int i = 0; i<7; ++i){
                        params[i] = document.get("statistics" + days[i]);
                    }

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
     * Funcio que retorna les estadistiques d'una rutina en un dia concret
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param day dia de les estadistiques que es volen aconseguir
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conte el metode
     */
    public void getStatisticsRoutineByDay(String userId, String idRoutine, String day, Method method, Object object){

        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);

        Object[] params = new Object[1];

        docRefToRoutineStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {


                    params[0] = document.get("statistics" + day);

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


    /********************************MODIFICADORES DE STATISTICS***********************************/

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
        double timeToAdd = timeDifference(beginTime, finishTime);

        docRefToRoutineStatistics.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    HashMap<String, Double> mapActDay = (HashMap) document.get("statistics" + day);
                    mapActDay.put(theme, mapActDay.get(theme) + timeToAdd);
                    docRefToRoutineStatistics.update("statistics" + day, mapActDay);

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
        String[] differentThemes = { "Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other" };
        String[] differentDays = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        HashMap <String, Object> dataInput = new HashMap<>();
        HashMap <String, Double> mapStatistics = new HashMap<>();


        for (int i = 0; i<8; ++i){
            mapStatistics.put(differentThemes[i], 0.0);
        }

        for (int j = 0; j<7; ++j){
            dataInput.put("statistics" + differentDays[j], mapStatistics);
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


        double hoursToDelete = timeDifference(beginTime, finishTime);

        docRefToStatistics.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        HashMap<String, Double> mapDay = (HashMap) document.get("statistics" + day);
                        Log.d("HORES", String.valueOf(mapDay.get(theme)));

                        mapDay.put(theme, mapDay.get(theme) - hoursToDelete);
                        docRefToStatistics.update("statistics" + day, mapDay);

                    }
                }
            }
        });

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
    public void updateDedicatedTimeActivity(String userId, String idRoutine, String newDay, String newTheme,  String newBeginTime, String newFinishTime, String oldDay, String oldTheme,  String oldBeginTime, String oldFinishTime) {


        DocumentReference docRefToStatistics = db.collection("users").document(userId).
                collection("statistics").document(idRoutine);

        //Nova duracio de l'activitat
        double newActivityHours = timeDifference(newBeginTime, newFinishTime);
        double oldActivityHours = timeDifference(oldBeginTime, oldFinishTime);
        boolean sameDay = oldDay.equals(newDay);
        boolean sameTheme = oldTheme.equals(newTheme);



        docRefToStatistics.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        HashMap<String, Double> mapOldDay = (HashMap) document.get("statistics" + oldDay);

                        if (sameDay && sameTheme){
                            double difference = newActivityHours - oldActivityHours;
                            mapOldDay.put(oldTheme, mapOldDay.get(oldTheme) + difference);
                        }

                        else {
                            //Treure les hores antigues de l'activitat antiga
                            mapOldDay.put(oldTheme, mapOldDay.get(oldTheme) - oldActivityHours);

                            if (sameDay){
                                //Posar les noves hores al mateix mapa i al tema diferent corresponent
                                mapOldDay.put(newTheme, mapOldDay.get(newTheme) + newActivityHours);
                            }

                            else {
                                //Posar les noves hores al mapa del dia diferent corresponent i al tema corresponent
                                HashMap<String, Double> mapNewDay = (HashMap) document.get("statistics" + newDay);
                                mapNewDay.put(newTheme, mapNewDay.get(newTheme) + newActivityHours);
                                 docRefToStatistics.update("statistics" + newDay, mapNewDay);
                            }
                        }
                        docRefToStatistics.update("statistics" + oldDay, mapOldDay);

                    }
                }
            }
        });




    }

    /*************************************FUNCIONS PRIVADES****************************************/
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
