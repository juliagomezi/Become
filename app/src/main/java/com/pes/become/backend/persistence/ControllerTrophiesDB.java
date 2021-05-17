package com.pes.become.backend.persistence;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ControllerTrophiesDB {

    /**
     * Unica instancia de la classe
     */
    private static ControllerTrophiesDB instance;
    /**
     * Instància de la bd
     */
    private final FirebaseFirestore db;

    /**
     * Creadora per defecte.
     */
    private ControllerTrophiesDB() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Obtenir la instancia de la classe
     * @return instancia
     */
    public static ControllerTrophiesDB getInstance() {
        if(instance == null)
            instance = new ControllerTrophiesDB();
        return instance;
    }

    /**
     * Funcio que crea els trofeus d'un usuari quan aquest es registra
     * @param userId identificador de l'usuari
     */
    public void createTrophies(String userId){
        CollectionReference colRefToTrophies = db.collection("users").document("userId").collection("trophies");
        HashMap<String,Object> mapRoutines = new HashMap<>();

        mapRoutines.put("accomplished", false);
        colRefToTrophies.document("firstRoutine").set(mapRoutines);
        colRefToTrophies.document("weekStreak").set(mapRoutines);
        colRefToTrophies.document("monthStreak").set(mapRoutines);
        mapRoutines.put("routinesCreated", 0);
        colRefToTrophies.document("secondRoutine").set(mapRoutines);

    }

    /**
     * Funcio que retorna els trofeus de l'usuari i un bool que indica per cada un si l'ha aconseguit
     * @param userId identificador de l'usuari
     * @param method metode que recull les dades
     * @param object classe que conte el metode
     */
    public void getTrophies(String userId, Method method, Object object) {
        CollectionReference colRefToTrophies = db.collection("users").document(userId).collection("trophies");

        colRefToTrophies.get().addOnCompleteListener(task -> {

            HashMap<String,HashMap<String,Object>> mapTrophies = new HashMap<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    String typeOfTrophy = document.getId();
                    mapTrophies.put(typeOfTrophy,(HashMap)document.getData());

                }
                try {
                    method.invoke(object, mapTrophies);
                } catch (IllegalAccessException ignore) {
                } catch (InvocationTargetException ignore) { }
            }
        });
    }

    /**
     * Funcio que registra el progres del trofeu de crear una rutina i mira si s'ha assolit.
     * @param userId identificador de l'usuari
     * @param method metode que recull les dades
     * @param object classe que conte el metode
     */
    public void updateFirstRoutine(String userId, Method method, Object object){
        DocumentReference docRefToTrophy = db.collection("users").document(userId).collection("trophies").document("firstRoutine");

        docRefToTrophy.update("accomplished",true);
        try {
            method.invoke(object, "firstRoutine");
        } catch (IllegalAccessException ignore) {
        } catch (InvocationTargetException ignore) { }
    }

    /**
     * Funcio que registra el progres del trofeu de crear dues rutines i mira si s'ha assolit.
     * @param userId identificador de l'usuari
     * @param method metode que recull les dades
     * @param object classe que conte el metode
     */
    public void updateSecondRoutine(String userId, Method method, Object object){
        DocumentReference docRefToTrophy = db.collection("users").document(userId).collection("trophies").document("secondRoutine");

        docRefToTrophy.get().addOnCompleteListener((task) -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String trophyToReturn = "";
                int numberRoutinesCreated = (Integer) document.get("routinesCreated");

                ++numberRoutinesCreated;
                if (numberRoutinesCreated==2){
                    docRefToTrophy.update("accomplished",true);
                    trophyToReturn = "secondRoutine";
                }

                try {
                    method.invoke(object, trophyToReturn);
                } catch (IllegalAccessException ignore) {
                } catch (InvocationTargetException ignore) { }

            }
        });
    }

}
