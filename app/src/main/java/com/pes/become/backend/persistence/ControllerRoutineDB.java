/*
package com.pes.become.backend.persistence;

import java.util.HashMap;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class ControllerRoutineDB {
    GoogleCredentials credentials;
    //FirebaseOptions options;
    Firestore db;

    public ControllerRoutineDB(){
         credentials = GoogleCredentials.getApplicationDefault();
         //options = new FirebaseOptions.Builder().setCredentials(credentials).setProjectId("become-767d4").build();
         //FirebaseApp.initializeApp(options);
         db = FirestoreClient.getFirestore();
    }

    public void createRoutine(){
        Map<String,String> dataInput = new HashMap<String,String>();
        dataInput.put("nom","RutinaPredefinida");
        db.collection("routines").document("RutinaPredefinida").set(dataInput);
    }
}
*/