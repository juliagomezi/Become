package com.pes.become.backend.persistence;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;

public class ControllerRoutineDB {


    final FirebaseFirestore db;

    /**
     * Creadora per defecte
     */
    public ControllerRoutineDB() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Crear una rutina nova
     * @param routineName nom de la rutina a crear
     */
    public void createRoutine(String routineName) {
        Map<String,String> dataInput = new HashMap<>();
        dataInput.put("name", routineName);
        db.collection("routines").document(routineName).set(dataInput);
    }

    /**
     * Eliminar una rutina existent
     * @param routineName nom de la rutina a eliminar
     */
    public void deleteRoutine(String routineName) {
        db.collection("routines").document(routineName).delete();
    }
}












