package com.pes.become.backend.persistence;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
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
import com.pes.become.backend.exceptions.OverlappingActivitiesException;

//import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    /**
     * Brief: es comprova que l'activitat que es vol afegir no se sol·lapi amb altres.
     * Pre: Cert.
     * @param routineName és la rutina on es vol col·locar la activitat
     * @param actDay és el dia on es vol col·locar l'activitat.
     * @param beginTime és l'hora d'inici de l'activitat a revisar.
     * @param finishTime és l'hora d'acabament de l'activitat a revisar.
     * Post: si alguna activitat se sol·lapa amb l'activitat que es vol afegir, salta una excepció.
     */
    private void checkOverlappingActivities(String routineName, String actDay, String beginTime,
                                            String finishTime, DocumentReference docRef,
                                            Method method, Object object) throws OverlappingActivitiesException {

        //Passar els string de temps a objectes de la classe Time.
        int hourBeginNew = Integer.parseInt(beginTime.substring(0,2));
        int minuteBeginNew = Integer.parseInt(beginTime.substring(3));
        int hourFinishNew = Integer.parseInt(finishTime.substring(0,2));
        int minuteFinishNew = Integer.parseInt(finishTime.substring(3));

        Time tBeginNew = new Time(hourBeginNew,minuteBeginNew);
        Time tFinishNew = new Time(hourFinishNew,minuteFinishNew);
        Query query = db.collection("routines").document(routineName).collection("activities").whereEqualTo("day", actDay);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean exception = false;
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
                            exception = true;
                            throw new OverlappingActivitiesException();
                        }

                    }
                    catch (OverlappingActivitiesException e) {
                        exception = true;
                        e.printStackTrace();
                    }
                }
                if(!exception) {
                    try {
                        //String newActDay, String newFinishTime, String newBeginTime, DocumentReference docRef
                        Object params[] = new Object[4];
                        params[0] = actDay;
                        params[1] = finishTime;
                        params[2] = beginTime;
                        params[3] = docRef;
                        method.invoke(object, params);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }


    public void getActivitiesByDay(String routineName, String day, Method method, Object object) throws InterruptedException {
        QueryDocumentSnapshot docs;
        Query consulta = db.collection("routines").document(routineName).collection("activities").whereEqualTo("day", day);
        Task<QuerySnapshot> resultat;
        consulta.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot aux2 = task.getResult();
                    ArrayList<String> activitiesResult = new ArrayList<>();
                    for (QueryDocumentSnapshot document : aux2) {
                        Log.d("ConsultaFirebase", document.getId() + " => " + document.getData());
                        activitiesResult.add(""+document.getData());
                    }
                    Object params[] = new Object[1];
                    params[0] = activitiesResult;
                    try {
                        method.invoke(object,params);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("ConsultaFirebase", task.getException().getMessage());
                }
            } });
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
                               String beginTime, String finishTime)  {

        CollectionReference refToActivities = db.collection("routines").document(routineName).collection("activities");

        //checkOverlappingActivities(actDay, beginTime, finishTime,refToActivities);

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


        consulta.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots){
                    DocumentReference docRefToActivity = document.getReference();
                    docRefToActivity.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("SUCCESS", "Tasca esborrada amb exit");
                        }
                    });
                }
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

        consulta.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
            }
        });



    }




    /**
     * Pre: La rutina de nom "routineName" ja existeix.
     * Pre: L'activitat que es vol modificar ja existeix.
     * @param routineName és el nom de la rutina ja existent.
     * @param activityName és el nom de l'activitat que es vol modificar.
     * @param actDay és el dia de  l'activitat que es vol modificar.
     * @param beginTime és l'hora d'inici de l'activitat.
     * @param finishTime és l'hora d'acabament de l'activitat.
     * @param newActDay és el nou dia
     * @param newBeginTime és el nou temps d'inici
     * @param newFinishTime és el nou temps final
     * Post: S'esborra l'activitat indicada.
     */


    public void modifyActivityTime(String routineName, String activityName, String actDay,
                                          String beginTime, String finishTime, String newActDay, String newBeginTime, String newFinishTime) {

        CollectionReference collRefToActivities = db.collection("routines").document(routineName).collection("activities");

        Query consulta = collRefToActivities.whereEqualTo("name",activityName).whereEqualTo("day",actDay).whereEqualTo("beginTime",beginTime).whereEqualTo("finishTime",finishTime);

        consulta.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot document : queryDocumentSnapshots){
                    DocumentReference docRef = document.getReference();
                    try {
                        Class[] parameterTypes = new Class[4];
                        parameterTypes[0] = String.class;
                        parameterTypes[1] = String.class;
                        parameterTypes[2] = String.class;
                        parameterTypes[3] = DocumentReference.class;
                        Method method1 = ControllerActivityDB.class.getMethod("modifyActivityTimeAux", parameterTypes);
                        //checkOverlappingActivities(routineName, newActDay, newBeginTime,newFinishTime,docRef,method1,new ControllerActivityDB());
                        modifyActivityTimeAux(newActDay,newFinishTime, newBeginTime, docRef); //Quan es descomenti la linia de sobre cal comentar aixo i descomentar els catch de sota
                    }/* catch (OverlappingActivitiesException e) {
                        e.printStackTrace();
                    } catch (InvalidTimeException e) {
                        e.printStackTrace();
                    }*/ catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }


                }

            }
        });

    }
    public void modifyActivityTimeAux(String newActDay, String newFinishTime, String newBeginTime, DocumentReference docRef)
    {
        docRef.update("day", newActDay);
        docRef.update("finishTime", newFinishTime);
        docRef.update("beginTime", newBeginTime);
        Log.d("SUCCESS", "Temps modificat amb exit");
    }

}