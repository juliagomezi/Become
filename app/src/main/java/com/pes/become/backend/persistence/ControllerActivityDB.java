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

    /**
     * Obtenir les activitats d'una rutina
     * @param userId identificador de l'usuari
     * @param idRoutine l'identificador de la rutina
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getActivities(String userId, String idRoutine, Method method, Object object) {
        db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities").get().addOnCompleteListener(task -> {
            HashMap<String, ArrayList<ArrayList<String>>> routineActivities = new HashMap<>();
            routineActivities.put("Monday",  new ArrayList<>());
            routineActivities.put("Tuesday",  new ArrayList<>());
            routineActivities.put("Wednesday",  new ArrayList<>());
            routineActivities.put("Thursday",  new ArrayList<>());
            routineActivities.put("Friday",  new ArrayList<>());
            routineActivities.put("Saturday",  new ArrayList<>());
            routineActivities.put("Sunday",  new ArrayList<>());

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
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
                    switch (activityDay) {
                        case "Monday":
                            routineActivities.get("Monday").add(activity);
                            break;
                        case "Tuesday":
                            routineActivities.get("Tuesday").add(activity);
                            break;
                        case "Wednesday":
                            routineActivities.get("Wednesday").add(activity);
                            break;
                        case "Thursday":
                            routineActivities.get("Thursday").add(activity);
                            break;
                        case "Friday":
                            routineActivities.get("Friday").add(activity);
                            break;
                        case "Saturday":
                            routineActivities.get("Saturday").add(activity);
                            break;
                        case "Sunday":
                            routineActivities.get("Sunday").add(activity);
                            break;
                    }
                }
                try {
                    method.invoke(object, routineActivities);
                } catch (IllegalAccessException ignore) {
                } catch (InvocationTargetException ignore) { }
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
     */
    public void updateActivity(String userId, String idRoutine, String actName, String description, String theme, String day, String iniT, String endT, String idActivity) {
        DocumentReference docRefToActivity = db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities").document(idActivity);
        if(actName != null) docRefToActivity.update("name", actName);
        if(description != null) docRefToActivity.update("description", description);
        if(theme != null) docRefToActivity.update("theme", theme);
        if(day != null) docRefToActivity.update("day", day);
        if(iniT != null) docRefToActivity.update("beginTime", iniT);
        if(endT != null) docRefToActivity.update("finishTime", endT);

    }

    /**
     * @param userId identificador de l'usuari
     * @param idRoutine es l'identificador de la rutina ja existent
     * @param lastDayDone és l'últim dia que l'usuari ha marcat la rutina com a feta en format de data yyyy-mm-dd (la classe StringDateConverter serveix per convertir-la)
     * @param idActivity és l'identificador de l'activitat
     */
    public void markActivityAsDone(String userId, String idRoutine, String lastDayDone, String idActivity)
    {
        DocumentReference docRefToActivity = db.collection("users").document(userId).collection("routines").document(idRoutine).collection("activities").document(idActivity);
        if(lastDayDone != null || lastDayDone != "null")
        {
            docRefToActivity.update("lastDayDone", lastDayDone).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ControllerCalendarDB cal = new ControllerCalendarDB();
                    cal.incrementDay(userId, lastDayDone, 1);
                }
            });
        }
        else
        {
            docRefToActivity.get().addOnCompleteListener((task) -> {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    String realLastDayDone = document.get("lastDayDone").toString();
                    docRefToActivity.update("lastDayDone", "null").addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            ControllerCalendarDB cal = new ControllerCalendarDB();
                            cal.incrementDay(userId, realLastDayDone, -1);
                        }
                    });
                }
            });
        }
    }

}
