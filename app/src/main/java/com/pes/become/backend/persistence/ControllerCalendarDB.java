package com.pes.become.backend.persistence;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ControllerCalendarDB {

    /**
     * Instància de la bd
     */
    final FirebaseFirestore db;

    /**
     * Creadora per defecte.
     */
    public ControllerCalendarDB() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Crea un dia al calendari nou.
     * @param userId identificador de l'usuari
     * @param day dia a crear.
     * @param routineId id de la rutina a la que referencia
     * @param totalActivites nombre d'activitats totals del dia de la rutina que estem afegint.
     */
    public String addDay(String userId, Date day, String routineId, int totalActivites) {
        Log.w("Calendar", "Iniciant operacio addDay");
        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("idRoutine", routineId);
        dataInput.put("numActivitiesDone", 0);
        dataInput.put("numTotalActivities", totalActivites);
        dataInput.put("day", getStringDay(day));
        dataInput.put("month", getStringMonth(day));
        dataInput.put("year", getStringYear(day));

        Log.w("Calendar", "A punt de crear un nou dia");
        DocumentReference docRefToDay= db.collection("users").document(userId).collection("calendar").document(StringDateConverter.dateToString(day));
        docRefToDay.set(dataInput);
        return docRefToDay.getId();
    }

    /**
     * Actualitza la informació d'un dia
     * @param userId id de l'usuari del calendari
     * @param day dia del calendari
     * @param activitiesDone nou nombre d'activitats fetes (a -1 no actualitzarà res)
     * @param idRoutine nova id de la rutina a la que referencia
     */
    public void updateDay(String userId, Date day, int activitiesDone, String idRoutine){
        DocumentReference docRefToRoutine = db.collection("users").document(userId).collection("calendar").document(StringDateConverter.dateToString(day));
        if(activitiesDone >= 0)docRefToRoutine.update("numActivitiesDone", activitiesDone);
        if(idRoutine != null && !idRoutine.equals(""))docRefToRoutine.update("idRoutine", idRoutine);
    }
    /**
     * Actualitza la informació d'un dia
     * @param userId id de l'usuari del calendari
     * @param day dia del calendari
     * @param activitiesDoneIncrement incrrement del nombre d'activitats fetes
     */
    public void incrementDay(String userId, String day, int activitiesDoneIncrement){
        Map<String, Object> data = new HashMap<>();
        data.put("numActivitiesDone", FieldValue.increment(activitiesDoneIncrement));
        DocumentReference docRefToRoutine = db.collection("users").document(userId).collection("calendar").document(day);
        //Aixo esta posat aixi perque volem actualitzar els camps concrets del document (merge) o crear-lo si no existeix (set)
        docRefToRoutine.set( data, SetOptions.merge());
    }
    /**
     * Executa el metode method amb un hashmap que representa el day de la base de dades si aquest s'ha pogut consultar, o l'excepció que ha saltat si no.
     * Day tindrà les claus: day, idRoutine, numActivitiesDone, numTotalActivities. Totes son strings
     * @param userId id de l'usuari del calendari
     * @param day dia del calendari
     * @param method metode a executar
     * @param object objecte del metode a executar
     */
    public void getDay(String userId, Date day, Method method, Object object){

        db.collection("users").document(userId).collection("calendar").document(StringDateConverter.dateToString(day))
            .get().addOnCompleteListener(task -> {
            Object[] params = new Object[2];
                params[0] = task.isSuccessful();
                if (task.isSuccessful()) {

                     DocumentSnapshot document = task.getResult();
                     if(document.exists()) {
                         HashMap<String, String> hashAux = new HashMap<String, String>();
                         hashAux.put("day", document.get("day").toString());
                         hashAux.put("month", document.get("month").toString());
                         hashAux.put("year", document.get("year").toString());
                         hashAux.put("idRoutine", document.get("idRoutine").toString());
                         hashAux.put("numActivitiesDone", document.get("numActivitiesDone").toString());
                         hashAux.put("numTotalActivities", document.get("numTotalActivities").toString());
                         params[1] = hashAux;
                     }
                     else
                     {
                         params[0] = false;
                         params[1] = new Exception("Tried to get a day that didn't exist");
                     }
                }else params[1] = task.getException();
                try {
                    method.invoke(object, params);
                } catch (IllegalAccessException ignore) {
                } catch (InvocationTargetException ignore) {
                }
        });
    }

    /**
     * Retorna els dies de la base de dades del mes indicat
     * @param userId id de l'usuari del calendari
     * @param month més que volem seleccionar
     * @param method metode a executar
     * @param object objecte del metode a executar
     */
    public void getAvailableDays(String userId, int month, Method method, Object object)
    {
        db.collection("users").document(userId).collection("calendar").whereEqualTo("month", String.format("%02d", month))
                .get().addOnCompleteListener(task -> {
            Object[] params = new Object[2];
            params[0] = task.isSuccessful();
            if (task.isSuccessful()) {
                ArrayList<HashMap<String,String>> list = new  ArrayList<>();
                QuerySnapshot documents = task.getResult();
                for(DocumentSnapshot document: documents) {
                    HashMap<String, String> hashAux = new HashMap<>();
                    hashAux.put("day", document.get("day").toString());
                    hashAux.put("month", document.get("month").toString());
                    hashAux.put("year", document.get("year").toString());
                    hashAux.put("idRoutine", document.get("idRoutine").toString());
                    hashAux.put("numActivitiesDone", document.get("numActivitiesDone").toString());
                    hashAux.put("numTotalActivities", document.get("numTotalActivities").toString());
                    list.add(hashAux);
                }
                params[1] = list;

            }else params[1] = task.getException();
            try {
                method.invoke(object, params);
            } catch (IllegalAccessException ignore) {
            } catch (InvocationTargetException ignore) {
            }
        });
    }


    /**
     * Retorna la data en format per el dia de la base de dades
     * @param date data
     * @return
     */
    private String getStringDay(Date date)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd");
        return dateFormat.format(date);
    }
    /**
     * Retorna la data en format pel mes de la base de dades
     * @param date data
     * @return
     */
    private String getStringMonth(Date date)
    {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        return dateFormat.format(date);
    }
    /**
     * Retorna la data en format per l'any de la base de dades
     * @param date data
     * @return
     */
    private String getStringYear(Date date)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        return dateFormat.format(date);
    }
}
