package com.pes.become.backend.persistence;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ControllerActivityDB {

    FirebaseFirestore db;
    public ControllerActivityDB(){
        db = FirebaseFirestore.getInstance();
    }


    /**
     * Pre: La rutina de nom "routineName" ja existeix i l'activitat que es vol crear no
     * se sol·lapa amb cap altre de la mateixa rutina.
     * Param: "routineName" és el nom de la rutina ja existent.
     * Param: "activityName" és el nom de l'activitat que es vol crear.
     * Param: "theme" és el tema de l'activitat.
     * Param: "actDescription" és la descripció del tema.
     * Param: "actDay" és el dia on es vol col·locar l'activitat.
     * Post:
     */
    public void createActivity(String routineName, String activityName, String actTheme,String actDescription, String actDay){
        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("name",activityName);
        dataInput.put("theme",actTheme);
        dataInput.put("description",actDescription);
        dataInput.put("day",actDay);
        db.collection(routineName).document().set(dataInput);

    }



}
