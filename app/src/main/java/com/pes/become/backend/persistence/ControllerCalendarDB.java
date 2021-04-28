package com.pes.become.backend.persistence;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
     * @param dia dia a crear.
     * @param routineId id de la rutina a la que referenciem
     * @param totalActivites nombre d'activitats totals del dia de la rutina que estem afegint.
     */
    public String addDay(String userId, Date dia, String routineId, int totalActivites) {
        Log.w("Calendar", "Iniciant operacio addDay");
        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("idRoutine", routineId);
        dataInput.put("numActivitiesDone", 0);
        dataInput.put("numTotalActivities", totalActivites);
        //Operacions calendari
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(dia);
        if(calendar.get(calendar.HOUR_OF_DAY) != 0) calendar.set(calendar.HOUR_OF_DAY, 0);
        if(calendar.get(Calendar.MINUTE) != 0) calendar.set(calendar.MINUTE, 0);
        if(calendar.get(Calendar.SECOND) != 0) calendar.set(calendar.SECOND, 0);
        if(calendar.get(Calendar.MILLISECOND) != 0) calendar.set(calendar.MILLISECOND, 0);
        dataInput.put("day", new Timestamp(calendar.getTime()));

        Log.w("Calendar", "A punt de crear un nou dia");
        DocumentReference docRefToDay= db.collection("users").document(userId).collection("calendar").document();
        docRefToDay.set(dataInput);
        return docRefToDay.getId();
    }
}
