package com.pes.become.backend.persistence;

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


    /********************************MODIFICADORES DE STATISTICS***********************************/

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
     * Es crea les estadistiques de la rutina que s'acaba de crear
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     */
    public void createRoutineStatistics(String userId, String idRoutine){

        DocumentReference docRefToRoutineStatistics = db.collection("users").document(userId).collection("statistics").document(idRoutine);
        String[] differentThemes = { "Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other" };
        String[] differentDays = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        HashMap <String, Object> dataInput = new HashMap<>();
        HashMap <String, Object> mapStatistics = new HashMap<>();


        for (int i = 0; i<8; ++i){
            mapStatistics.put(differentThemes[i],0.0);
        }

        for (int j = 0; j<7; ++j){
            dataInput.put("statistics"+differentDays[j],mapStatistics);
        }

        docRefToRoutineStatistics.set(dataInput);
    }
    
    
    
    /**
     * Funcio per actualitzar les estadistiques d'una rutina quan s'ha modificat una de les seves
     * activitats
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina
     * @param idActivity identificador de l'activitat
     * @param newTheme nou tema de l'activitat
     * @param newDay nou dia de l'activitat
     * @param newBeginTime nova hora d'inici de l'activitat
     * @param newFinishTime nova hora de finalització de l'activitat
     * @param method metode a cridar
     * @param object classe que conte el metode
     */
    public void updateDedicatedTimeActivity(String userId, String idRoutine, String idActivity, String newTheme, String newDay, String newBeginTime, String newFinishTime, Method method, Object object) {
        DocumentReference docRefToActivity = db.collection("users").document(userId).
                collection("routines").document(idRoutine).
                collection("activities").document(idActivity);

        DocumentReference docRefToStatistics = db.collection("users").document(userId).
                collection("statistics").document(idRoutine);

        //Nova duracio de l'activitat
        double newActivityHours = timeDifference(newBeginTime, newFinishTime);

        //LES DADES QUE TE AQUESTA ACTIVITAT SON LES VELLES
        docRefToActivity.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String oldDay = document.get("day").toString();
                        String oldTheme = document.get("theme").toString();
                        boolean sameDay = oldDay.equals(newDay);
                        boolean sameTheme = oldTheme.equals(newTheme);

                        //Duracio en hores de l'activitat abans d'actualitzar-la.
                        double oldActivityHours = timeDifference(document.get("beginTime").toString(), document.get("finishTime").toString());

                        docRefToStatistics.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()) {

                                    DocumentSnapshot document = task.getResult();

                                    if (document.exists()) {

                                        HashMap<String, Double> mapOldDay = (HashMap) document.get("statistics" + oldDay);
                                        //Potser s'ha de fer mapa <string, object> i convertir a double

                                        if (sameDay && sameTheme){
                                            double difference = newActivityHours - oldActivityHours;
                                            mapOldDay.put(oldTheme, mapOldDay.get(oldTheme) + difference);
                                        }

                                        else{

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
                }
            }
        });
    }

    /*************************************FUNCIONS PRIVADES****************************************/
    public double timeDifference(String beginTime, String finishTime) {
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
