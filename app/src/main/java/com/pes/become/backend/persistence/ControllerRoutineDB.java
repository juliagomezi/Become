package com.pes.become.backend.persistence;

import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ControllerRoutineDB {


    final FirebaseFirestore db;

    /**
     * Creadora per defecte.
     */
    public ControllerRoutineDB() {
        db = FirebaseFirestore.getInstance();
    }




    /***************CONSULTORES***************/
    public void getRoutine(String userId, String nameRoutine)
    {
        db.collection("users").document(userId).collection("routines").whereEqualTo("name", nameRoutine).addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w("LISTENER FAILED", "getRoutine failed.", e);
                return;
            }
        });
    }

    /**
     * Funció per obtenir els noms i els ids de totes les rutines d'un usuari
     * @param userId Id de l'usuari
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getUserRoutines(String userId, Method method, Object object)
    {
        db.collection("users").document(userId).collection("routines").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<ArrayList<String>> routinesResult = new ArrayList<>();
                if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot  document : task.getResult()) {
                            ArrayList<String> routine = new ArrayList<>();
                            routine.add(document.getId());
                            routine.add(document.get("name").toString());
                            routinesResult.add(routine);
                        }
                }
                Object[] params = new Object[1];
                params[0] = routinesResult;
                try {
                    method.invoke(object, params);
                } catch (IllegalAccessException e1) {
                    System.out.println("Acces invàlid");
                } catch (InvocationTargetException e2) {
                    System.out.println("Target no vàlid");
                }
            }

        });
    }

    /***************MODIFICADORES***************/

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
        Map<String,String> dataInput = new HashMap<>();
        dataInput.put("name", routineName);
        DocumentReference docRefToRoutine= db.collection("users").document(userId).collection("routines").document();
        docRefToRoutine.set(dataInput);
        return docRefToRoutine.getId();
    }

    /**
     * Esborra les activitats de la rutina.
     * @param collection és la referència a la col·lecció que es vol borrar.
     * @param batchSize és el límit de documents que pot agafar.
     */
    private void deleteActivities(CollectionReference collection, int batchSize) {
        try {
            // retrieve a small batch of documents to avoid out-of-memory errors
            Task<QuerySnapshot> future = collection.limit(batchSize).get();
            int deleted = 0;
            // future.get() blocks on document retrieval
            List<DocumentSnapshot> documents = future.getResult().getDocuments();
            for (DocumentSnapshot document : documents) {
                document.getReference().delete();
                ++deleted;
            }
            if (deleted >= batchSize) {
                // retrieve and delete another batch
                deleteActivities(collection, batchSize);
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }

    /**
     * Eliminar una rutina existent.
     * @param userId identificador de l'usuari
     * @param idRoutine identificador de la rutina a eliminar.
     */
    public void deleteRoutine( String userId, String idRoutine) {
        DocumentReference routineReference = db.collection("users").document(userId).collection("routines").document(idRoutine);
        routineReference.collection("activities").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnap : task.getResult()) {
                                DocumentReference docRefToActivity = documentSnap.getReference();
                                docRefToActivity.delete();
                            }
                            routineReference.delete();
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



}












