package com.pes.become.backend.persistence;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    /*****************************************CONSULTORES******************************************/

    /**
     * Obtenir les activitats d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine l'identificador de la rutina
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivities(String userId, String idRoutine, Method method, Object object) {
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
                activity.add(document.get("lastDayDone").toString());

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


            try {
                method.invoke(object, routineActivities);
            } catch (IllegalAccessException ignore) {
            } catch (InvocationTargetException ignore) {
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
                activity.add(document.get("lastDayDone").toString());
                activitiesResult.add(activity);
            }
            try {
                method.invoke(object, activitiesResult);
            } catch (IllegalAccessException ignore) {
            } catch (InvocationTargetException ignore) {
            }
        });
    }

    /*****************************************MODIFICADORES****************************************/

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
     * @param lastDayDone és l'últim dia que l'usuari ha marcat la rutina com a feta en format de data Standard del firebase
     */
    public String createActivity( String userId, String idRoutine, String activityName, String actTheme,String actDescription, String actDay, String beginTime, String finishTime, String lastDayDone) {
        CollectionReference refToActivities = db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities");
        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("name",activityName);
        dataInput.put("theme",actTheme);
        dataInput.put("description",actDescription);
        dataInput.put("beginTime",beginTime);
        dataInput.put("finishTime",finishTime);
        dataInput.put("day",actDay);
        dataInput.put("lastDayDone", lastDayDone);
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
     * @param lastDayDone és l'últim dia que l'usuari ha marcat la rutina com a feta en format de data Standard del firebase, hauria d'exsistir al calendari de l'usuari de la base de dades un document del dia
     */
    public void updateActivity(String userId, String idRoutine, String actName, String description, String theme, String day, String iniT, String endT,  String lastDayDone, String idActivity) {
        DocumentReference docRefToActivity = db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities").document(idActivity);
        if(actName != null) docRefToActivity.update("name", actName);
        if(description != null) docRefToActivity.update("description", description);
        if(theme != null) docRefToActivity.update("theme", theme);
        if(day != null) docRefToActivity.update("day", day);
        if(iniT != null) docRefToActivity.update("beginTime", iniT);
        if(endT != null) docRefToActivity.update("finishTime", endT);
        if(lastDayDone != null)
        {
            docRefToActivity.update("lastDayDone", lastDayDone).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ControllerCalendarDB cal = new ControllerCalendarDB();
                    cal.incrementDay(userId, lastDayDone, 1);
                }
                else {
                }
            });
        }
    }
//    public void markActivity
}
