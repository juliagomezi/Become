package com.pes.become.backend.persistence;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pes.become.backend.domain.Achievement;

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
     * Funcio que retorna els trofeus de l'usuari i un bool que indica per cada un si l'ha aconseguit
     * @param userId identificador de l'usuari
     * @param method metode que recull les dades
     * @param object classe que conte el metode
     */
    public void getTrophies(String userId, Method method, Object object) {
        CollectionReference colRefToTrophies = db.collection("users").document(userId).collection("trophies");

        colRefToTrophies.get().addOnCompleteListener(task -> {

            ArrayList<String> trophies = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    trophies.add(document.getId());
                }
                try {
                    method.invoke(object, trophies);
                } catch (IllegalAccessException ignore) {
                } catch (InvocationTargetException ignore) { }
            }
        });
    }

    /**
     * Funcio que crea el trofeus d'un usuari
     * @param userId identificador de l'usuari
     * @param trophyName nom del trofeu
     */
    public void addTrophy(String userId, String trophyName) {
        db.collection("users").document(userId).collection("trophies").document(trophyName).set(new HashMap<>());
    }

}
