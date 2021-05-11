package com.pes.become.backend.persistence;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;

public class ControllerTrophiesDB {
    /**
     * Instància de la bd
     */
    final FirebaseFirestore db;

    /**
     * Creadora per defecte.
     */
    public ControllerTrophiesDB() {
        db = FirebaseFirestore.getInstance();
    }

    public void getTrophies(String userId, Method method, Object object)
    {

    }
    public void setFirstRoutine(String userId, boolean accomplished)
    {

    }
    public void setMonthStreak(String userId, boolean accomplished)
    {

    }
    public void setSecondRoutine(String userId, boolean accomplished)
    {

    }
    public void setWeekStreak(String userId, boolean accomplished)
    {

    }
}
