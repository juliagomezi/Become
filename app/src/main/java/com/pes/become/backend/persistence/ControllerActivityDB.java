package com.pes.become.backend.persistence;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;

public class ControllerActivityDB {

    private FirebaseFirestore db;

    /**
     * Pre: Cert.
     * Post: Es crea una nova instància de controlador d'activitats.
     */
    public ControllerActivityDB(){
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Métode per aconseguir les activitats d'una rutina i un dia indicats.
     * @param routineName nom de la rutina
     * @param day dia a consultar
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivitiesByDay(String routineName, String day, Method method, Object object) {
        db.collection("routines").document(routineName).collection("activities").whereEqualTo("day", day)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w("LISTENER FAILED", "Listen failed.", e);
                        return;
                    }

                    ArrayList<ArrayList<String>> activitiesResult = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        ArrayList<String> activity = new ArrayList<>();
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

    /**
     * Pre: La rutina de nom "routineName" ja existeix.
     * @param routineName és el nom de la rutina ja existent.
     * @param activityName és el nom de l'activitat que es vol crear.
     * @param actTheme és el tema de l'activitat.
     * @param actDescription és la descripció del tema.
     * @param actDay és el dia on es vol col·locar l'activitat.
     * @param beginTime és l'hora d'inici de l'activitat.
     * @param finishTime és l'hora d'acabament de l'activitat
     * Post: si l'activitat no se sol·lapa amb cap altre, es crea.
     */
    public void createActivity(String routineName, String activityName, String actTheme,String actDescription, String actDay,
                               String beginTime, String finishTime)  {

        CollectionReference refToActivities = db.collection("routines").document(routineName).collection("activities");

        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("name",activityName);
        dataInput.put("theme",actTheme);
        dataInput.put("description",actDescription);
        dataInput.put("beginTime",beginTime);
        dataInput.put("finishTime",finishTime);
        dataInput.put("day",actDay);
        refToActivities.document().set(dataInput);
    }

    /**
     * Pre: La rutina de nom "routineName" ja existeix.
     * Pre: L'activitat que es vol esborrar ja existeix i
     * està identificada pels paràmetres següents.
     * @param routineName és el nom de la rutina ja existent.
     * @param beginTime és l'hora d'inici de l'activitat.
     * @param finishTime és l'hora d'acabament de l'activitat
     * Post: S'esborra l'activitat indicada.
     */
    public void deleteActivity(String routineName, String beginTime, String finishTime){

        CollectionReference collRefToActivities;
        collRefToActivities = db.collection("routines").document(routineName).collection("activities");

        Query consulta = collRefToActivities.whereEqualTo("beginTime",beginTime).whereEqualTo("finishTime",finishTime);

        consulta.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots){
                DocumentReference docRefToActivity = document.getReference();
                docRefToActivity.delete().addOnSuccessListener(aVoid -> Log.d("SUCCESS", "Tasca esborrada amb exit"));
            }
        });
    }

    /**
     * Pre: La rutina de nom "routineName" ja existeix.
     * Pre: L'activitat que es vol modificar ja existeix.
     * @param routineName és el nom de la rutina ja existent.
     * @param actName és el nom de l'activitat que es vol modificar.
     * @param day és el dia de  l'activitat que es vol modificar.
     * @param iniT és l'hora d'inici de l'activitat.
     * @param endT és l'hora d'acabament de l'activitat.
     * @param description és la nova descripció que es vol afegir a l'activitat.
     * Post: Es modifica la descripció de l'activitat indicada.
     */
    public void updateActivity(String routineName, String actName, String description, String theme, String oldIniTime,  String oldEndTime, String iniT, String endT, String day){


        CollectionReference collRefToActivities = db.collection("routines").document(routineName).collection("activities");


        Query consulta = collRefToActivities.whereEqualTo("beginTime",oldIniTime).whereEqualTo("finishTime",oldEndTime);

        consulta.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots){
                DocumentReference docRef = document.getReference();
                docRef.update("name", actName);
                docRef.update("description", description);
                docRef.update("theme", theme);
                docRef.update("day", day);
                docRef.update("beginTime",iniT);
                docRef.update("finishTime", endT);

                Log.d("SUCCESS", "Descripcio modificada amb exit");
            }
        });
    }
}