package com.pes.become.backend.persistence;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

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
    public void createActivity(String routineName, String activityName, String actTheme,String actDescription, String actDay, String beginTime, String finishTime){
        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("name",activityName);
        dataInput.put("theme",actTheme);
        dataInput.put("description",actDescription);
        dataInput.put("beginTime",beginTime);
        dataInput.put("finishTime",finishTime);
        dataInput.put("day",actDay);
        db.collection("routines").document(routineName).collection("activities").document().set(dataInput);
    }
    public void getActivitiesByDay(String routineName, String day)
    {
        QueryDocumentSnapshot docs;
        Query consulta = db.collection("routines").document(routineName).collection("activities")
                .whereEqualTo("day", day);
        Task<QuerySnapshot> resultat =consulta.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot aux2 = task.getResult();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("ConsultaFirebase", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("ConsultaFirebase", task.getException().getMessage());
                        }
                    } });
    }
}
