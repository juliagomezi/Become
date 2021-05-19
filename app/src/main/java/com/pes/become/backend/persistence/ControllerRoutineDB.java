package com.pes.become.backend.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Document;

public class ControllerRoutineDB {

    /**
     * Unica instancia de la classe
     */
    private static ControllerRoutineDB instance;
    /**
     * Instància de la bd
     */
    final FirebaseFirestore db;

    /**
     * Creadora per defecte.
     */
    private ControllerRoutineDB() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Obtenir la instancia de la classe
     * @return instancia
     */
    public static ControllerRoutineDB getInstance() {
        if(instance == null)
            instance = new ControllerRoutineDB();
        return instance;
    }

    //////////////////////////////MODIFICADORES/////////////////////////////////////////////////////
    /**
     * Canvia el nom d'una rutina.
     * @param userId identificador de l'usuari
     * @param idRoutine l'identificador de la rutina.
     * @param newName el nom que se li vol posar a la rutina.
     * @param shared bool que indica si la rutina es publica o no.
     */
    public void changeName(String userId, String idRoutine, String newName, boolean shared){
        DocumentReference docRefToRoutine = db.collection("users").document(userId).collection("routines").document(idRoutine);
        docRefToRoutine.update("name", newName);
        if (shared){
            db.collection("sharedRoutines").document(userId+"_"+idRoutine).update("name", newName);
        }
    }

    /**
     * Crea una rutina nova.
     * @param userId identificador de l'usuari
     * @param routineName nom de la rutina a crear.
     */
    public String createRoutine(String userId, String routineName) {
        Map<String,Object> dataInput = new HashMap<>();
        dataInput.put("name", routineName);
        dataInput.put("timestamp", FieldValue.serverTimestamp());
        dataInput.put("shared", false);
        DocumentReference docRefToRoutine= db.collection("users").document(userId).collection("routines").document();
        docRefToRoutine.set(dataInput);
        return docRefToRoutine.getId();
    }

    /**
     * Eliminar una rutina existent i la rutina equivalent publica (si estava publicada).
     * @param userId identificador de l'usuari.
     * @param idRoutine identificador de la rutina a eliminar.
     * @param shared bool que indica si la rutina es publica o no.
     */
    public void deleteRoutine(String userId, String idRoutine, boolean shared) {
        if (shared){
            unShareRoutinePriv(userId, idRoutine);
        }

        DocumentReference routineReference = db.collection("users").document(userId).collection("routines").document(idRoutine);
        routineReference.collection("activities").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnap : Objects.requireNonNull(task.getResult())) {
                            DocumentReference docRefToActivity = documentSnap.getReference();
                            docRefToActivity.delete();
                        }
                    }
                    routineReference.delete();
                });
    }

    /**
     * Descarrega una rutina publica NO UTILITZAR ENCARA
     * @param userId identificador de l'usuari que vol descarregar la rutina publica
     * @param idSharedRoutine identificador de la rutina publica
     */
    public void downloadSharedRoutine(String userId, String idSharedRoutine){
        DocumentReference docRefToSharedRoutine = db.collection("sharedRoutines").document(idSharedRoutine);
        docRefToSharedRoutine.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot routineDoc = task.getResult();
                        String sharedRoutineName = routineDoc.get("name").toString();
                        String newId = createRoutine(userId, sharedRoutineName);

                        HashMap <String, Object> dataInput = new HashMap<>();

                        HashMap <String, Double> mapMusic, mapSport, mapSleeping, mapCooking, mapWorking, mapEntertainment, mapPlants, mapOther;
                        mapMusic = mapSport = mapSleeping = mapCooking = mapWorking = mapEntertainment = mapPlants = mapOther = new HashMap<>();

                        String[] differentDays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

                        for (int i = 0; i<7; ++i){
                            double zero = 0.0;
                            mapMusic.put(differentDays[i], zero);
                            mapSport.put(differentDays[i], zero);
                            mapSleeping.put(differentDays[i], zero);
                            mapCooking.put(differentDays[i], zero);
                            mapWorking.put(differentDays[i], zero);
                            mapEntertainment.put(differentDays[i], zero);
                            mapPlants.put(differentDays[i], zero);
                            mapOther.put(differentDays[i], zero);
                        }

                        DocumentReference docRefToNewRoutine = db.collection("users").document(userId).collection("routines").document(newId);

                        docRefToSharedRoutine.collection("activities").get().addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot actDoc : task2.getResult()) {
                                    Map<String, Object> activity = actDoc.getData();
                                    String theme = activity.get("theme").toString();
                                    String day = activity.get("day").toString();
                                    double timeActivity = timeDifference(activity.get("beginTime").toString(), activity.get("finishTime").toString());
                                    String activityId = actDoc.getId();
                                    docRefToNewRoutine.collection("activities").document(activityId).set(activity);


                                    switch (theme) {
                                        case "Music":
                                            mapMusic.put(day, mapMusic.get(day) + timeActivity);
                                            break;
                                        case "Sport":
                                            mapSport.put(day, mapSport.get(day) + timeActivity);
                                            break;
                                        case "Sleeping":
                                            mapSleeping.put(day, mapSleeping.get(day) + timeActivity);
                                            break;
                                        case "Cooking":
                                            mapCooking.put(day, mapCooking.get(day) + timeActivity);
                                            break;
                                        case "Working":
                                            mapWorking.put(day, mapWorking.get(day) + timeActivity);
                                            break;
                                        case "Entertainment":
                                            mapEntertainment.put(day, mapEntertainment.get(day) + timeActivity);
                                            break;
                                        case "Plants":
                                            mapPlants.put(day, mapPlants.get(day) + timeActivity);
                                            break;
                                        case "Other":
                                            mapOther.put(day, mapOther.get(day) + timeActivity);
                                            break;
                                    }

                                }
                                dataInput.put("statisticsMusic", mapMusic);
                                dataInput.put("statisticsSport", mapSport);
                                dataInput.put("statisticsSleeping", mapSleeping);
                                dataInput.put("statisticsCooking", mapCooking);
                                dataInput.put("statisticsWorking", mapWorking);
                                dataInput.put("statisticsEntertainment", mapEntertainment);
                                dataInput.put("statisticsPlants", mapPlants);
                                dataInput.put("statisticsOther", mapOther);
                                db.collection("users").document(userId).collection("statistics").document(newId).set(dataInput);

                            }
                        });
                    }
                });


    }

    /**
     * Funcio que fa que una rutina passi a ser publica.
     * @param userId identificador de l'usuari.
     * @param idRoutine l'identificador de la rutina.
     */
    public void shareRoutine(String userId, String idRoutine) {
        DocumentReference routineReference = db.collection("users").document(userId).collection("routines").document(idRoutine);
        DocumentReference sharedRoutineReference = db.collection("sharedRoutines").document(userId+"_"+idRoutine);
        routineReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                routineReference.update("shared", true);
                DocumentSnapshot docRoutine = task.getResult();
                Map<String,Object> routineData = docRoutine.getData();
                routineData.remove("shared");
                routineData.put("avgPoints",0.0);
                routineData.put("numRates", 0);
                routineData.put("ownerId",userId);
                sharedRoutineReference.set(routineData, SetOptions.merge());

                routineReference.collection("activities").get()
                        .addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnap : Objects.requireNonNull(task2.getResult())) {
                                    Map<String,Object> activitat = documentSnap.getData();
                                    String idActivity = documentSnap.getId();
                                    sharedRoutineReference.collection("activities").document(idActivity).set(activitat);
                                }
                            }
                        });
            }
        });
    }

    /**
     * Funcio que fa que l'atribut shared de la rutina "local" sigui false i esborra la rutina publica.
     * @param userId identificador de l'usuari.
     * @param idRoutine l'identificador de la rutina.
     */
    public void unShareRoutine(String userId, String idRoutine) {
        DocumentReference ogRoutineReference = db.collection("users").document(userId).collection("routines").document(idRoutine);
        ogRoutineReference.update("shared", false);
        unShareRoutinePriv(userId, idRoutine);
    }

    /**
     * Funcio que esborra una rutina de la col·leccio de rutines publiques.
     * @param userId identificador de l'usuari.
     * @param idRoutine l'identificador de la rutina.
     */
    private void unShareRoutinePriv(String userId, String idRoutine) {
        DocumentReference routineReference = db.collection("sharedRoutines").document(userId+"_"+idRoutine);
        routineReference.collection("activities").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnap : Objects.requireNonNull(task.getResult())) {
                            DocumentReference docRefToActivity = documentSnap.getReference();
                            docRefToActivity.delete();
                        }
                    }
                    routineReference.delete();
                });
    }

    /**
     * Funcio per a votar una certa rutina publica
     * @param idSharedRoutine identificador de la rutina publica
     * @param average la nova mitjana de puntuacio de la rutina publca
     */
    public void voteRoutine(String idSharedRoutine, double average){
        DocumentReference docRefToSharedRoutine = db.collection("sharedRoutines").document(idSharedRoutine);
        docRefToSharedRoutine.update("avgPoints", average);
        docRefToSharedRoutine.update("numRates", FieldValue.increment(1));
    }


    /**
     * Calcula la diferencia de temps
     * @param beginTime hora d'inici
     * @param finishTime hora de fi
     * @return la diferencia entre hora d'inici i hora de fi
     */
    private double timeDifference(String beginTime, String finishTime) {
        double beginHour = Double.parseDouble(beginTime.substring(0,2));
        double beginMinute = Double.parseDouble(beginTime.substring(3));
        double finishHour = Double.parseDouble(finishTime.substring(0,2));
        double finishMinute = Double.parseDouble(finishTime.substring(3));
        double hours = finishHour - beginHour;
        double minutes = (finishMinute - beginMinute)/60.0;
        if (minutes<0.0){
            hours = hours - 1.0;
            minutes = minutes * -1.0;
        }
        return hours + minutes;
    }

}












