package com.pes.become.backend.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Crear una rutina nova.
     * @param routineName nom de la rutina a crear.
     */
    public String createRoutine(String routineName) {
        Map<String,String> dataInput = new HashMap<>();
        dataInput.put("name", routineName);
        DocumentReference docRefToRoutine= db.collection("routines").document();
        docRefToRoutine.set(dataInput);
        return docRefToRoutine.getId();
    }

    /**
     * Eliminar una rutina existent.
     * @param idRoutine identificador de la rutina a eliminar.
     */
    public void deleteRoutine(String idRoutine) {
        DocumentReference DocRefToRoutine = db.collection("routines").document(idRoutine);
        CollectionReference colRefToActivities = DocRefToRoutine.collection("activities");

        deleteActivities(colRefToActivities,10);

        DocRefToRoutine.delete();
    }

    /**
     *
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

}












