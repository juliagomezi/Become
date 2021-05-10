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


    /**
     * Obtenir les activitats d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine l'identificador de la rutina
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivities(String userId, String idRoutine, Method method, Object object, boolean login) {
        Log.d("DB", "DB");
        db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities").addSnapshotListener((value, e) -> {
            if (e != null) {
                return;
            }
            HashMap <String, ArrayList<ArrayList<String>> > routineActivities = new HashMap<>();
            ArrayList<ArrayList<String>> activitiesMonday = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> activitiesTuesday = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> activitiesWednesday = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> activitiesThursday = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> activitiesFriday = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> activitiesSaturday = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> activitiesSunday = new ArrayList<ArrayList<String>>();


            for (QueryDocumentSnapshot document : value) {
                ArrayList<String> activity = new ArrayList<>();
                activity.add(document.getId());
                activity.add(document.get("name").toString());
                activity.add(document.get("description").toString());
                activity.add(document.get("theme").toString());
                String activityDay = document.get("day").toString();
                activity.add(activityDay);
                activity.add(document.get("beginTime").toString());
                activity.add(document.get("finishTime").toString());
                //activity.add(document.get("lastDayDone").toString());

                //ArrayList<ArrayList<String>> aux = routineActivities.get(activityDay);
                //aux.add(activity);
                //routineActivities.put(activityDay,aux);

                if (activityDay.equals("Monday")) { activitiesMonday.add(activity); }
                else if (activityDay.equals("Tuesday")) { activitiesTuesday.add(activity); }
                else if (activityDay.equals("Wednesday")) { activitiesWednesday.add(activity); }
                else if (activityDay.equals("Thursday")) { activitiesThursday.add(activity); }
                else if (activityDay.equals("Friday")) { activitiesFriday.add(activity); }
                else if (activityDay.equals("Saturday")) { activitiesSaturday.add(activity); }
                else { activitiesSunday.add(activity); }


            }
            routineActivities.put("Monday",activitiesMonday);
            routineActivities.put("Tuesday",activitiesTuesday);
            routineActivities.put("Wednesday",activitiesWednesday);
            routineActivities.put("Thursday",activitiesThursday);
            routineActivities.put("Friday",activitiesFriday);
            routineActivities.put("Saturday",activitiesSaturday);
            routineActivities.put("Sunday",activitiesSunday);

            Object[] params = new Object[2];
            params[0] = routineActivities;
            params[1] = login;
            try {
                method.invoke(object, params);
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        });
    }

    /**
     * Obtenir les activitats d'una rutina i un dia indicats
     * @param userId identificador de l'usuari
     * @param idRoutine l'identificador de la rutina
     * @param day dia a consultar
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivitiesByDay(String userId, String idRoutine, String day, Method method, Object object) {
        db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities").whereEqualTo("day", day).addSnapshotListener((value, e) -> {
            if (e != null) {
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
            try {
                method.invoke(object, activitiesResult);
            } catch (IllegalAccessException ignore) {
            } catch (InvocationTargetException ignore) {
            }
        });
    }

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
