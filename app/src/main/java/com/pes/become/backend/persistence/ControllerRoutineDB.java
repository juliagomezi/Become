
package com.pes.become.backend.persistence;

import java.util.HashMap;
import java.util.Map;
/*
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
*/
import com.google.firebase.firestore.FirebaseFirestore;

public class ControllerRoutineDB {


    FirebaseFirestore db;

    public ControllerRoutineDB(){
        db = FirebaseFirestore.getInstance();
    }
    //Cousultores
    /**
     * Pre: cert.
     * Param: no hi ha cap rutina que tingui "routineName" com a nom.
     * Post:
     */
    public void createRoutine(String routineName){
        Map<String,String> dataInput = new HashMap<>();
        dataInput.put("name",routineName);
        db.collection("routines").document(routineName).set(dataInput);
    }
    //Modificadores
    /**
     * Pre: cert.
     * Param: no hi ha cap rutina que tingui "routineName" com a nom.
     * Post:
     */
    public void deleteRoutine(String routineName){
        db.collection("routines").document(routineName).delete();
    }
}












