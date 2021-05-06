package com.pes.become.backend.persistence;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ControllerUserDB {

    private static ControllerUserDB instance;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    /**
     * Creadora per defecte.
     */
    private ControllerUserDB(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Metode per obtenir una instancia de la classe ControllerUserDB
     * @return una instancia de la classe ControllerUserDB
     */
    public static ControllerUserDB getInstance() {
        if (instance == null) {
            instance = new ControllerUserDB();
        }
        return instance;
    }

    /**
     * Canvi de contrasenya de l'usuari actual
     * @param newPassword nova contrasenya
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void changePassword(String oldPassword, String newPassword, Method method, Object object) {
        FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            try {
                                                method.invoke(object, true);
                                            } catch (IllegalAccessException ignore) {
                                            } catch (InvocationTargetException ignore) {}
                                        } else {
                                            try {
                                                method.invoke(object, false);
                                            } catch (IllegalAccessException ignore) {
                                            } catch (InvocationTargetException ignore) {}
                                        }
                                    }
                                });
                    } else {
                        try {
                            method.invoke(object, false);
                        } catch (IllegalAccessException ignore) {
                        } catch (InvocationTargetException ignore) {}
                    }
                });
    }

    /**
     * Metode per penjar una foto de perfil des de la galeria de l'usuari
     * @param userId nom de l'usuari que penja la foto
     * @param imageUri uri de la imatge a penjar
     */
    public void updateProfilePic(String userId, Uri imageUri) {
        StorageReference imageRef = storageRef.child("images/"+userId);
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                })
                .addOnFailureListener(exception -> {
                });
    }

    /**
     * Inici de sessió d'usuari
     * @param mail correu
     * @param password contrasenya
     * @param act Activity d'Android necessaria per la execucio del firebase
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loginUser(String mail, String password, Activity act, Method method, Object object) {
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(act, task -> {
                    Object[] params = new Object[7];

                    if (task.isSuccessful() && mAuth.getCurrentUser().isEmailVerified()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();

                        DocumentReference docRefToUser = db.collection("users").document(userID);
                        docRefToUser.get().addOnSuccessListener(documentSnapshot -> {
                            params[0] = true;
                            params[1] = userID;
                            params[2] = documentSnapshot.get("Username").toString();
                            params[3] = documentSnapshot.get("selectedRoutine");
                            if (params[3] == null){  params[3]="";}
                            else {params[3] = params[3].toString();}

                            try {
                                File localFile = File.createTempFile("images", "jpeg");
                                StorageReference imageRef = storageRef.child("images/"+userID);
                                imageRef.getFile(localFile)
                                        .addOnSuccessListener(taskSnapshot -> {
                                            params[4] = BitmapFactory.decodeFile(localFile.getAbsolutePath());



                                            ArrayList<ArrayList<String>> routineIds = new ArrayList();
                                            docRefToUser.collection("routines").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()) {
                                                    for (QueryDocumentSnapshot  document : task2.getResult()) {
                                                        ArrayList<String> routinesResult = new ArrayList<>();
                                                        routinesResult.add(document.getId());
                                                        routinesResult.add(document.get("name").toString());
                                                        routineIds.add(routinesResult);
                                                    }
                                                }
                                                params[5] = routineIds;
                                                String sRoutine = params[3].toString();
                                                DocumentReference docRefToRoutineStatistics = docRefToUser.collection("statistics").document(sRoutine);
                                                docRefToRoutineStatistics.get().addOnCompleteListener(task23 -> {
                                                    if (task23.isSuccessful()) {
                                                        DocumentSnapshot document = task23.getResult();
                                                        if (document.exists()) {
                                                            String[] differentThemes = {"Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other"};
                                                            HashMap <String, HashMap<String, Double>> mapThemes = new HashMap<>();
                                                            for (int i = 0; i<8; ++i){
                                                                mapThemes.put(differentThemes[i],(HashMap) document.get("statistics" + differentThemes[i]));
                                                            }
                                                            params[6] = mapThemes;
                                                            try {
                                                                method.invoke(object, params);
                                                            } catch (IllegalAccessException ignore) {
                                                            } catch (InvocationTargetException ignore) {
                                                            }

                                                        }
                                                        else {params[6]=null;
                                                            try {
                                                                method.invoke(object, params);
                                                            } catch (IllegalAccessException ignore) {
                                                            } catch (InvocationTargetException ignore) {
                                                            }}
                                                    }
                                                    else {params[6]=null;
                                                        try {
                                                            method.invoke(object, params);
                                                        } catch (IllegalAccessException ignore) {
                                                        } catch (InvocationTargetException ignore) {
                                                        }}
                                                });
                                            });
                                        }).addOnFailureListener(exception -> {
                                    params[4] = null;

                                    ArrayList<ArrayList<String>> routineIds = new ArrayList();
                                    docRefToUser.collection("routines").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task3 -> {
                                        if (task3.isSuccessful()) {
                                            for (QueryDocumentSnapshot  document : task3.getResult()) {
                                                ArrayList<String> routinesResult = new ArrayList<>();
                                                routinesResult.add(document.getId());
                                                routinesResult.add(document.get("name").toString());
                                                routineIds.add(routinesResult);
                                            }
                                        }
                                        params[5] = routineIds;
                                        String sRoutine = params[3].toString();
                                        DocumentReference docRefToRoutineStatistics = docRefToUser.collection("statistics").document(sRoutine);
                                        docRefToRoutineStatistics.get().addOnCompleteListener(task23 -> {
                                            if (task23.isSuccessful()) {
                                                DocumentSnapshot document = task23.getResult();
                                                if (document.exists()) {
                                                    String[] differentThemes = {"Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other"};
                                                    HashMap <String, HashMap<String, Double>> mapThemes = new HashMap<>();
                                                    for (int i = 0; i<8; ++i){
                                                        mapThemes.put(differentThemes[i],(HashMap) document.get("statistics" + differentThemes[i]));
                                                    }
                                                    params[6] = mapThemes;
                                                    try {
                                                        method.invoke(object, params);
                                                    } catch (IllegalAccessException ignore) {
                                                    } catch (InvocationTargetException ignore) {
                                                    }

                                                }
                                                else {params[6]=null;
                                                    try {
                                                        method.invoke(object, params);
                                                    } catch (IllegalAccessException ignore) {
                                                    } catch (InvocationTargetException ignore) {
                                                    }}
                                            }
                                            else {params[6]=null;
                                                try {
                                                    method.invoke(object, params);
                                                } catch (IllegalAccessException ignore) {
                                                } catch (InvocationTargetException ignore) {
                                                }}
                                        });

                                    });
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else {
                        params[0] = false;
                        params[1] = params[2] = params[3] = "";
                        try {
                            method.invoke(object, params);
                        } catch (IllegalAccessException ignore) {
                        } catch (InvocationTargetException ignore) {
                        }
                    }

                });
    }


    /**
     * Funcio per carregar un usuari que ja te la sessio inciada
     * @param method metode que es cridara amb el resultat
     * @param object classe que conte el metode
     */
    public void loadUser(Method method, Object object) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        Object[] params = new Object[7];

        DocumentReference docRefToUser = db.collection("users").document(userID);
        docRefToUser.get().addOnSuccessListener(documentSnapshot -> {
            params[0] = true;
            params[1] = userID;
            params[2] = documentSnapshot.get("Username").toString();
            params[3] = documentSnapshot.get("selectedRoutine");

            try {
                File localFile = File.createTempFile("images", "jpeg");
                StorageReference imageRef = storageRef.child("images/"+userID);
                imageRef.getFile(localFile)
                        .addOnSuccessListener(taskSnapshot -> {
                            params[4] = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                            if (params[3] == null) params[3]="";
                            else params[3] = params[3].toString();

                            ArrayList<ArrayList<String>> routineIds = new ArrayList();
                            docRefToUser.collection("routines").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    for (QueryDocumentSnapshot  document : task2.getResult()) {
                                        ArrayList<String> routinesResult = new ArrayList<>();
                                        routinesResult.add(document.getId());
                                        routinesResult.add(document.get("name").toString());
                                        routineIds.add(routinesResult);
                                    }
                                }
                                params[5] = routineIds;
                                String sRoutine = params[3].toString();
                                DocumentReference docRefToRoutineStatistics = docRefToUser.collection("statistics").document(sRoutine);
                                docRefToRoutineStatistics.get().addOnCompleteListener(task23 -> {
                                    if (task23.isSuccessful()) {
                                        DocumentSnapshot document = task23.getResult();
                                        if (document.exists()) {
                                            String[] differentThemes = {"Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other"};
                                            HashMap <String, HashMap<String, Double>> mapThemes = new HashMap<>();
                                            for (int i = 0; i<8; ++i){
                                                mapThemes.put(differentThemes[i],(HashMap) document.get("statistics" + differentThemes[i]));
                                            }
                                            params[6] = mapThemes;
                                            try {
                                                method.invoke(object, params);
                                            } catch (IllegalAccessException ignore) {
                                            } catch (InvocationTargetException ignore) {
                                            }

                                        }
                                        else {params[6]=null;
                                            try {
                                                method.invoke(object, params);
                                            } catch (IllegalAccessException ignore) {
                                            } catch (InvocationTargetException ignore) {
                                            }}
                                    }
                                    else {params[6]=null;
                                        try {
                                            method.invoke(object, params);
                                        } catch (IllegalAccessException ignore) {
                                        } catch (InvocationTargetException ignore) {
                                        }}
                                });
                            });
                        }).addOnFailureListener(exception -> {
                    params[4] = null;

                    if (params[3] == null) params[3]="";
                    else params[3] = params[3].toString();

                    ArrayList<ArrayList<String>> routineIds = new ArrayList();
                    docRefToUser.collection("routines").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task3 -> {
                        if (task3.isSuccessful()) {
                            for (QueryDocumentSnapshot  document : task3.getResult()) {
                                ArrayList<String> routinesResult = new ArrayList<>();
                                routinesResult.add(document.getId());
                                routinesResult.add(document.get("name").toString());
                                routineIds.add(routinesResult);
                            }
                        }
                        params[5] = routineIds;
                        String sRoutine = params[3].toString();
                        DocumentReference docRefToRoutineStatistics = docRefToUser.collection("statistics").document(sRoutine);
                        docRefToRoutineStatistics.get().addOnCompleteListener(task23 -> {
                            if (task23.isSuccessful()) {
                                DocumentSnapshot document = task23.getResult();
                                if (document.exists()) {
                                    String[] differentThemes = {"Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other"};
                                    HashMap <String, HashMap<String, Double>> mapThemes = new HashMap<>();
                                    for (int i = 0; i<8; ++i){
                                        mapThemes.put(differentThemes[i],(HashMap) document.get("statistics" + differentThemes[i]));
                                    }
                                    params[6] = mapThemes;
                                    try {
                                        method.invoke(object, params);
                                    } catch (IllegalAccessException ignore) {
                                    } catch (InvocationTargetException ignore) {
                                    }

                                }
                                else {params[6]=null;
                                    try {
                                        method.invoke(object, params);
                                    } catch (IllegalAccessException ignore) {
                                    } catch (InvocationTargetException ignore) {
                                    }}
                            }
                            else {params[6]=null;
                                try {
                                    method.invoke(object, params);
                                } catch (IllegalAccessException ignore) {
                                } catch (InvocationTargetException ignore) {
                                }}
                        });

                    });
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Inici de sessió d'usuari amb google
     * @param idToken token d'inici de sessió de Google
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loginUserGoogle(String idToken, Method method, Object object)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    Object[] params = new Object[7];

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();

                        DocumentReference docRefToUser = db.collection("users").document(userID);
                        docRefToUser.get().addOnSuccessListener(documentSnapshot -> {

                            if(!documentSnapshot.exists()) {
                                HashMap<String, Object> mapa = new HashMap<>();
                                mapa.put("Username", user.getDisplayName());
                                mapa.put("selectedRoutine", "");
                                docRefToUser.set(mapa);
                            }


                            try {
                                File localFile = File.createTempFile("images", "jpeg");
                                StorageReference imageRef = storageRef.child("images/"+userID);
                                imageRef.getFile(localFile)
                                        .addOnSuccessListener(taskSnapshot -> {
                                            params[0] = true;
                                            params[1] = userID;
                                            params[2] = documentSnapshot.get("Username").toString();
                                            params[3] = documentSnapshot.get("selectedRoutine");
                                            params[4] = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                            if (params[3] == null) params[3]="";
                                            else params[3] = params[3].toString();

                                            ArrayList<ArrayList<String>> routineIds = new ArrayList();
                                            docRefToUser.collection("routines").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()) {
                                                    for (QueryDocumentSnapshot  document : task2.getResult()) {
                                                        ArrayList<String> routinesResult = new ArrayList<>();
                                                        routinesResult.add(document.getId());
                                                        routinesResult.add(document.get("name").toString());
                                                        routineIds.add(routinesResult);
                                                    }
                                                }
                                                params[5] = routineIds;
                                                String sRoutine = params[3].toString();
                                                DocumentReference docRefToRoutineStatistics = docRefToUser.collection("statistics").document(sRoutine);
                                                docRefToRoutineStatistics.get().addOnCompleteListener(task23 -> {
                                                    if (task23.isSuccessful()) {
                                                        DocumentSnapshot document = task23.getResult();
                                                        if (document.exists()) {
                                                            String[] differentThemes = {"Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other"};
                                                            HashMap <String, HashMap<String, Double>> mapThemes = new HashMap<>();
                                                            for (int i = 0; i<8; ++i){
                                                                mapThemes.put(differentThemes[i],(HashMap) document.get("statistics" + differentThemes[i]));
                                                            }
                                                            params[6] = mapThemes;
                                                            try {
                                                                method.invoke(object, params);
                                                            } catch (IllegalAccessException ignore) {
                                                            } catch (InvocationTargetException ignore) {
                                                            }

                                                        }
                                                        else {params[6]=null;
                                                            try {
                                                                method.invoke(object, params);
                                                            } catch (IllegalAccessException ignore) {
                                                            } catch (InvocationTargetException ignore) {
                                                            }}
                                                    }
                                                    else {params[6]=null;
                                                        try {
                                                            method.invoke(object, params);
                                                        } catch (IllegalAccessException ignore) {
                                                        } catch (InvocationTargetException ignore) {
                                                        }}
                                                });
                                            });
                                        }).addOnFailureListener(exception -> {
                                    params[0] = true;
                                    params[1] = userID;
                                    params[2] = documentSnapshot.get("Username").toString();
                                    params[3] = documentSnapshot.get("selectedRoutine");
                                    params[4] = null;

                                    if (params[3] == null) params[3]="";
                                    else params[3] = params[3].toString();

                                    ArrayList<ArrayList<String>> routineIds = new ArrayList();
                                    docRefToUser.collection("routines").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task3 -> {
                                        if (task3.isSuccessful()) {
                                            for (QueryDocumentSnapshot  document : task3.getResult()) {
                                                ArrayList<String> routinesResult = new ArrayList<>();
                                                routinesResult.add(document.getId());
                                                routinesResult.add(document.get("name").toString());
                                                routineIds.add(routinesResult);
                                            }
                                        }
                                        params[5] = routineIds;
                                        String sRoutine = params[3].toString();
                                        DocumentReference docRefToRoutineStatistics = docRefToUser.collection("statistics").document(sRoutine);
                                        docRefToRoutineStatistics.get().addOnCompleteListener(task23 -> {
                                            if (task23.isSuccessful()) {
                                                DocumentSnapshot document = task23.getResult();
                                                if (document.exists()) {
                                                    String[] differentThemes = {"Music", "Sport", "Sleeping", "Cooking", "Working", "Entertainment", "Plants", "Other"};
                                                    HashMap <String, HashMap<String, Double>> mapThemes = new HashMap<>();
                                                    for (int i = 0; i<8; ++i){
                                                        mapThemes.put(differentThemes[i],(HashMap) document.get("statistics" + differentThemes[i]));
                                                    }
                                                    params[6] = mapThemes;
                                                    try {
                                                        method.invoke(object, params);
                                                    } catch (IllegalAccessException ignore) {
                                                    } catch (InvocationTargetException ignore) {
                                                    }

                                                }
                                                else {params[6]=null;
                                                    try {
                                                        method.invoke(object, params);
                                                    } catch (IllegalAccessException ignore) {
                                                    } catch (InvocationTargetException ignore) {
                                                    }}
                                            }
                                            else {params[6]=null;
                                                try {
                                                    method.invoke(object, params);
                                                } catch (IllegalAccessException ignore) {
                                                } catch (InvocationTargetException ignore) {
                                                }}
                                        });
                                    });
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else {
                        params[0] = false;
                        params[1] = params[2] = params[3] = "";
                        try {
                            method.invoke(object, params);
                        } catch (IllegalAccessException ignore) {
                        } catch (InvocationTargetException ignore) {
                        }
                    }
                });
    }

    /**
     * Registre d'un nou usuari
     * @param mail correu de l'usuari que es vol crear
     * @param password contrasenya de l'usuari que es vol crear
     * @param name nom de l'usuari que es vol crear
     * @param act Activity d'Android necessaria per la execucio del firebase
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void registerUser(String mail, String password,String name, Activity act,Method method, Object object) {

        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(act, task -> {
                    Object[] params = new Object[4];

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();

                        DocumentReference docRefToUser =  db.collection("users").document(userID);
                        HashMap<String, Object> mapa = new HashMap<>();
                        mapa.put("Username", name);
                        mapa.put("selectedRoutine", "");
                        docRefToUser.set(mapa);
                        params[0] = true;
                        params[1] = userID;
                        params[2] = name;
                        params[3] = "";

                        user.sendEmailVerification();
                        signOut();
                    }
                    else {
                        params[0] = false;
                        params[1] = params[2] = params[3]= "";
                    }
                    try {
                        method.invoke(object, params);
                    } catch (IllegalAccessException ignore) {
                    } catch (InvocationTargetException ignore) {
                    }
                });
    }

    /**
     * Canvia la rutina seleccionada d'un usuari
     * @param userID identificador de l'usuari
     * @param routineID identificador de la nova rutina seleccionada
     */
    public void setSelectedRoutine(String userID, String routineID) {
        DocumentReference docRefToUser = db.collection("users").document(userID);
        docRefToUser.update("selectedRoutine",routineID);
    }

    /**
     * Canvia el nom d'usuari
     * @param userID identificador de l'usuari
     * @param name nou nom d'usuari
     */
    public void changeUsername(String userID, String name) {
        DocumentReference docRefToUser = db.collection("users").document(userID);
        docRefToUser.update("Username",name);
    }

    /**
     * Metode per fer sign out de l'usuari actual
     */
    public void signOut() {
        mAuth.getInstance().signOut();
    }

    /**
     * Esborrar l'usuari actual i les seves rutines i activitats
     */
    public void deleteUser(String password, Method method, Object object) {
        FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), password);


        String id = user.getUid();
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        user.delete()
                                .addOnCompleteListener(task2 -> {
                                    boolean success = false;
                                    if (task2.isSuccessful()) {
                                        DocumentReference docRefToUser = db.collection("users").document(id);
                                        deleteUserData(docRefToUser);
                                        success = true;
                                    }
                                    try {
                                        method.invoke(object, success);
                                    } catch (IllegalAccessException ignore) {
                                    } catch (InvocationTargetException ignore) {
                                    }
                                });
                    } else {
                        try {
                            method.invoke(object, false);
                        } catch (IllegalAccessException ignore) {
                        } catch (InvocationTargetException ignore) {
                        }
                    }
                });
    }

    /**
     * Metode per obtenir el provider de l'usuari
     * @return el provider de l'usuari
     */
    public String getUserProvider() {
        for(UserInfo prov :  mAuth.getCurrentUser().getProviderData()) {
            if(prov.getProviderId().equals("google.com")) {
                return "google.com";
            }
        }
        return mAuth.getCurrentUser().getProviderId();
    }

    /**************************************FUNCIONS PRIVADES***************************************/

    /**
     * Esborra totes les dades de la BD de l'usuari (rutines i activitats)
     * @param docRefToUser es el document de l'usuari que conte totes les altres subcol·leccions
     */
    private void deleteUserData(DocumentReference docRefToUser) {

        CollectionReference colRefToRoutines = docRefToUser.collection("routines");
        CollectionReference colRefToStatistics = docRefToUser.collection("statistics");

        colRefToRoutines.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnap : task.getResult()) {
                            DocumentReference docRefToRoutine = documentSnap.getReference();
                            deleteRoutineData(docRefToRoutine);

                        }
                    }
                });

        colRefToStatistics.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnap : task.getResult()) {
                            DocumentReference docRefToStatistic = documentSnap.getReference();
                            docRefToStatistic.delete();

                        }
                    }
                });
        docRefToUser.delete();


    }

    /**
     * Esborra totes les dades d'una rutina i després esborra el document de la rutina.
     * @param docRefToRoutine és el document de l'usuari que conté totes les altres subcol·leccions.
     */
    private void deleteRoutineData(DocumentReference docRefToRoutine) {
        docRefToRoutine.collection("activities").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnap : task.getResult()) {
                            DocumentReference docRefToActivity = documentSnap.getReference();
                            docRefToActivity.delete();
                        }
                    }
                    docRefToRoutine.delete();
                });
    }



    /**
     * Metode per recuperar la contrasenya d'un usuari
     * @param mail mail del compte a recuperar
     */
    public void sendPassResetEmail(String mail, Method method, Object object) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(mail)
                .addOnCompleteListener(task -> {
                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                    boolean google = false;
                    if (!isNewUser) {
                        google = task.getResult().getSignInMethods().get(0).equals("google.com");
                        if(!google) FirebaseAuth.getInstance().sendPasswordResetEmail(mail);
                    }
                    boolean sent = true;
                    if(isNewUser || google) sent = false;
                    try {
                        method.invoke(object, sent);
                    } catch (IllegalAccessException ignore) {
                    } catch (InvocationTargetException ignore) {
                    }
                });
    }

}
