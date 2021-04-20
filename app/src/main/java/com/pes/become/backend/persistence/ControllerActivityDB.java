package com.pes.become.backend.persistence;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;

public class ControllerActivityDB {

    /**
     * Instància de la bd
     */
    private final FirebaseFirestore db;

    /**
     * Creadora per defecte
     */
    public ControllerActivityDB() {
        db = FirebaseFirestore.getInstance();
    }


    /***************CONSULTORES***************/



    /**
     * Obtenir les activitats d'una rutina i un dia indicats
     * @param userId identificador de l'usuari
     * @param idRoutine l'identificador de la rutina
     * @param day dia a consultar
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivitiesByDay(String userId, String idRoutine, String day, Method method, Object object) {
        db.collection("users").document(userId).collection("routines").document(idRoutine).
                collection("activities").whereEqualTo("day", day).addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w("LISTENER FAILED", "Listen failed.", e);
                return;
            }


            ArrayList<ArrayList<String>> activitiesResult = new ArrayList<>();
            for (QueryDocumentSnapshot document : value) {
                ArrayList<String> activity = new ArrayList<>();
                activity.add(document.getId());
                activity.add(document.get("name").toString());
                activity.add(document.get("description").toString());
                activity.add(document.get("theme").toString());
                activity.add(document.get("day").toString());
                activity.add(document.get("beginTime").toString());
                activity.add(document.get("finishTime").toString());
                activitiesResult.add(activity);
            }
            Object[] params = new Object[1];
            params[0] = activitiesResult;
            try {
                method.invoke(object, params);
            } catch (IllegalAccessException e1) {
                System.out.println("Acces invàlid");
            } catch (InvocationTargetException e2) {
                System.out.println("Target no vàlid");
            }
        });
    }

    /***************MODIFICADORES***************/

    /**
     * Crear una activitat en una rutina existent
     * @param userId identificador de l'usuari
     * @param idRoutine es l'identificador de la rutina ja existent
     * @param activityName es el nom de l'activitat que es vol crear
     * @param actTheme es el tema de l'activitat
     * @param actDescription es la descripcio del tema
     * @param actDay es el dia on es vol col·locar l'activitat
     * @param beginTime es l'hora d'inici de l'activitat
     * @param finishTime es l'hora d'acabament de l'activitat
     * @return el valor del id de l'activitat creada
     */
    public String createActivity( String userId, String idRoutine, String activityName, String actTheme,String actDescription, String actDay, String beginTime, String finishTime) {
        CollectionReference refToActivities = db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities");
        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("name",activityName);
        dataInput.put("theme",actTheme);
        dataInput.put("description",actDescription);
        dataInput.put("beginTime",beginTime);
        dataInput.put("finishTime",finishTime);
        dataInput.put("day",actDay);

        DocumentReference refToNewActivity = refToActivities.document();
        String ID = refToNewActivity.getId();

        refToNewActivity.set(dataInput);

        return ID;
    }

    /**
     * Eliminar una activitat existent d'una rutina existent
     * @param userId identificador de l'usuari
     * @param idRoutine es l'identificador de la rutina ja existent
     * @param idActivity es l'identificador de l'activitat
     */
    public void deleteActivity(String userId, String idRoutine, String idActivity) {
        DocumentReference docRefToActivity;
        docRefToActivity = db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities").document(idActivity);

        docRefToActivity.delete();
    }

    /**
     * Actualitzar una activitat existent d'una rutina existent
     * @param userId identificador de l'usuari
     * @param idRoutine es l'identificador de la rutina ja existent
     * @param actName es el nom de l'activitat que es vol modificar
     * @param description es la nova descripció que es vol afegir a l'activitat
     * @param theme es el tema que es vol afegir a l'activitat
     * @param day es el dia de  l'activitat que es vol modificar
     * @param iniT es l'hora d'inici de l'activitat
     * @param endT es l'hora d'acabament de l'activitat
     * @param idActivity és l'identificador de l'activitat
     */
    public void updateActivity(String userId, String idRoutine, String actName, String description, String theme, String day, String iniT, String endT,  String idActivity) {
        DocumentReference docRefToActivity = db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities").document(idActivity);

        docRefToActivity.update("name", actName);
        docRefToActivity.update("description", description);
        docRefToActivity.update("theme", theme);
        docRefToActivity.update("day", day);
        docRefToActivity.update("beginTime", iniT);
        docRefToActivity.update("finishTime", endT);
    }

}
