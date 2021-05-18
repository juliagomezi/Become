package com.pes.become.backend.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

public class ControllerRoutineDB {

    /**
     * Unica instancia de la classe
     */
    private static ControllerRoutineDB instance;
    /**
     * Instància de la bd
     */
    final FirebaseFirestore db;

    /**
     * Creadora per defecte.
     */
    private ControllerRoutineDB() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Obtenir la instancia de la classe
     * @return instancia
     */
    public static ControllerRoutineDB getInstance() {
        if(instance == null)
            instance = new ControllerRoutineDB();
        return instance;
    }

    /**
     * Canvia el nom d'una rutina.
     * @param userId identificador de l'usuari
     * @param idRoutine l'identificador de la rutina.
     * @param newName el nom que se li vol posar a la rutina.
     */
    public void changeName(String userId, String idRoutine, String newName){
        DocumentReference docRefToRoutine = db.collection("users").document(userId).collection("routines").document(idRoutine);
        docRefToRoutine.update("name", newName);
    }

    /**
     * Crea una rutina nova.
     * @param userId identificador de l'usuari
     * @param routineName nom de la rutina a crear.
     */
    public String createRoutine(String userId, String routineName) {
        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("name", routineName);
        dataInput.put("timestamp", FieldValue.serverTimestamp());
        dataInput.put("shared", false);
        DocumentReference docRefToRoutine= db.collection("users").document(userId).collection("routines").document();
        docRefToRoutine.set(dataInput);
        return docRefToRoutine.getId();
    }

    /**
     * Eliminar una rutina existent.
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina a eliminar.
     */
    public void deleteRoutine( String userId, String idRoutine) {
        unShareRoutinePriv(userId, idRoutine);
        DocumentReference routineReference = db.collection("users").document(userId).collection("routines").document(idRoutine);
        routineReference.collection("activities").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnap : Objects.requireNonNull(task.getResult())) {
                            DocumentReference docRefToActivity = documentSnap.getReference();
                            docRefToActivity.delete();
                        }
                    }
                    routineReference.delete();
                });
    }

    public void shareRoutine(String userId, String idRoutine)
    {
        DocumentReference routineReference = db.collection("users").document(userId).collection("routines").document(idRoutine);
        DocumentReference sharedRoutineReference = db.collection("sharedRoutines").document(userId+"_"+idRoutine);
        routineReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                Map<String,Object> rutina = new HashMap<>();
                rutina = doc.getData();
                rutina.remove("shared");
                routineReference.update("shared", true);
                rutina.put("totalPoints",0.0);
                rutina.put("numRates", 0);
                sharedRoutineReference.set(rutina, SetOptions.merge());
                routineReference.collection("activities").get()
                        .addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnap : Objects.requireNonNull(task2.getResult())) {
                                    Map<String,Object> activitat = new HashMap<>();
                                    activitat = documentSnap.getData();
                                    String aux = documentSnap.getId();
                                    sharedRoutineReference.collection("activities").document(aux).set(activitat);
                                }
                            }
                        });
            }
        });
    }
    public void unShareRoutine(String userId, String idRoutine)
    {
        DocumentReference ogRoutineReference = db.collection("users").document(userId).collection("routines").document(idRoutine);
        ogRoutineReference.update("shared", false);
        unShareRoutinePriv(userId, idRoutine);
    }

    private void unShareRoutinePriv(String userId, String idRoutine)
    {
        DocumentReference routineReference = db.collection("sharedRoutines").document(userId+"_"+idRoutine);
        routineReference.collection("activities").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnap : Objects.requireNonNull(task.getResult())) {
                            DocumentReference docRefToActivity = documentSnap.getReference();
                            docRefToActivity.delete();
                        }
                    }
                    routineReference.delete();
                });
    }
}












