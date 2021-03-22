package com.pes.become.backend.persistence;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pes.become.backend.domain.Time;

//import android.util.Log;

//import androidx.annotation.NonNull;

//import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;

//import org.w3c.dom.Document;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.Method;

public class ControllerActivityDB {

    FirebaseFirestore db;
    /**
     * Pre: Cert.
     * Post: Es crea una nova instància de controlador d'activitats.
     */
    public ControllerActivityDB(){
        db = FirebaseFirestore.getInstance();
    }


    //Consultores
    public void getActivitiesByDay(String routineName, String day, Method method, Object object) throws InterruptedException {
        QueryDocumentSnapshot docs;
        Query consulta = db.collection("routines").document(routineName).collection("activities").whereEqualTo("day", day);
        Task<QuerySnapshot> resultat;
        consulta.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot aux2 = task.getResult();
                    for (QueryDocumentSnapshot document : aux2) {
                        Log.d("ConsultaFirebase", document.getId() + " => " + document.getData());
                        String activitiesResult=document.getId() + " => " + document.getData();
                        Object params[] = new Object[1];
                        params[0] = activitiesResult;
                        try {
                            method.invoke(object,params);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.d("ConsultaFirebase", task.getException().getMessage());
                }
            } });
    }

     /**
     * Brief: es comprova que l'activitat que es vol afegir no se sol·lapi amb altres.
     * Pre: Cert.
     * @param actDay és el dia on es vol col·locar l'activitat.
     * @param beginTime és l'hora d'inici de l'activitat a revisar.
     * @param finishTime és l'hora d'acabament de l'activitat a revisar.
     * @param refToActivities conté la referència al conjunt d'activitats al que es vol revisar les hores
     * Post: si alguna activitat se sol·lapa amb l'activitat que es vol afegir, salta una excepció.
     */
    private void checkOverlappingActivities(String actDay, String beginTime,String finishTime,
                                            CollectionReference refToActivities) throws OverlappingActivitiesException, InvalidTimeException {

        //Passar els string de temps a objectes de la classe Time.
        int hourBeginNew = Integer.parseInt(beginTime.substring(0,2));
        int minuteBeginNew = Integer.parseInt(beginTime.substring(3));
        int hourFinishNew = Integer.parseInt(finishTime.substring(0,2));
        int minuteFinishNew = Integer.parseInt(finishTime.substring(3));

        Time tBeginNew = new Time(hourBeginNew,minuteBeginNew);
        Time tFinishNew = new Time(hourFinishNew,minuteFinishNew);

        Query query = refToActivities.whereEqualTo("day", actDay);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for (DocumentSnapshot document : queryDocumentSnapshots){
                    //Càlcul dels temps d'inici i final de l'activitat que es vol evaluar.
                    String auxBegin = document.get("beginTime").toString();
                    String auxFinish = document.get("finishTime").toString();
                    int hourBeginOld = Integer.parseInt(auxBegin.substring(0,2));
                    int minuteBeginOld = Integer.parseInt(auxBegin.substring(3));
                    int hourFinishOld = Integer.parseInt(auxFinish.substring(0,2));
                    int minuteFinishOld = Integer.parseInt(auxFinish.substring(3));

                    try {
                        Time tBeginOld = new Time(hourBeginOld,minuteBeginOld);
                        Time tFinishOld = new Time(hourFinishOld,minuteFinishOld);
                        if ((tFinishNew.compareTo(tBeginOld)<=0) || tFinishOld.compareTo(tBeginNew)<=0){
                            throw new OverlappingActivitiesException();
                        }
                    }
                    catch (InvalidTimeException | OverlappingActivitiesException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }





    //Modificadores
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
                               String beginTime, String finishTime) throws OverlappingActivitiesException, InvalidTimeException {

        CollectionReference refToActivities = db.collection("routines").document(routineName).collection("activities");

        checkOverlappingActivities(actDay, beginTime, finishTime,refToActivities);

        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("name",activityName);
        dataInput.put("theme",actTheme);
        dataInput.put("description",actDescription);
        dataInput.put("beginTime",beginTime);
        dataInput.put("finishTime",finishTime);
        dataInput.put("day",actDay);
        refToActivities.document().set(dataInput);
    }








}