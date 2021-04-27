package com.pes.become.backend.persistence;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;
import java.util.Map;

public class ControllerStatisticsDB {

    private final FirebaseFirestore db;

    /**
     * Creadora per defecte de la classe ControllerStatisticsDB
     */
    public ControllerStatisticsDB() { db = FirebaseFirestore.getInstance(); }

    /********************************CONSULTORES DE STATISTICS*************************************/



    /********************************MODIFICADORES DE STATISTICS***********************************/

    public void updateDedicatedTime(String userId, String idRoutine, String idActivity, String newBeginTime, String newFinishTime, String newDay, String newTheme, Method method, Object object){
        DocumentReference docRefToActivity = db.collection("users").document(userId).
                collection("routines").document(idRoutine).
                collection("activities").document(idActivity);

        DocumentReference docRefToStatistics = db.collection("users").document(userId).
                collection("statistics").document(idRoutine);

        double newQuantityHours = timeDifference(newBeginTime, newFinishTime);
        //LES DADES QUE TÉ AQUESTA ACTIVITAT SÓN LES VELLES
        docRefToActivity.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String oldDay = document.get("day").toString();
                        String oldTheme = document.get("theme").toString();
                        double oldQuantityHours = timeDifference(document.get("beginTime").toString(), document.get("finishTime").toString());
                        boolean sameDay = oldDay.equals(newDay);
                        boolean sameTheme = oldTheme.equals(newTheme);

                        //ACONSEGUIR HORES DEL TEMA ABANS DE MODIFICAR
                        //RESTAR oldQuantityHours
                        //MIRAR SI EL TEMA ESTA A ZERO
                        //SUMAR newQuantityHours
                        docRefToStatistics.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> mapOldDay = (Map) document.get("statistics"+oldDay);
                                        //double oldThemeTime = mapOldDay.get(oldTheme);
                                        if (sameDay && sameTheme){

                                        }

                                        else if (sameDay){}
                                        else if (sameTheme){}
                                        else {}
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
    public double timeDifference(String beginTime, String finishTime){
        //return finishTime-beginTime
        double beginHour = Double.parseDouble(beginTime.substring(0,2));
        double beginMinute = Double.parseDouble(beginTime.substring(3));

        double finishHour = Double.parseDouble(finishTime.substring(0,2));
        double finishMinute = Double.parseDouble(finishTime.substring(3));

        double hours = finishHour - beginHour;
        double minutes = (finishMinute - beginMinute)/60.0;

        if (minutes<0.0){
            hours = hours - 1;
            minutes = minutes * -1.0;
        }

        return hours + minutes;
    }


    /*
    public void aux(){
        docRefToStatistics.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Actualitzar el mapa afegint la diferència calculada abans
                    }
                }
            }
        });
    }

    */
}
