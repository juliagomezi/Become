package com.pes.become.backend.persistence;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pes.become.backend.adapters.DomainAdapter;
import com.pes.become.frontend.MainActivity;

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
     * Funcio per obtenir el nom, el correu i la rutina seleccionada de l'usuari en aquest ordre
     * @param userID l'identificador de l'usuari
     * @param method el metode a cridar
     * @param object l'objecte que conte el metode
     */
    public void getInfoUser(String userID, Method method, Object object) {
        DocumentReference docRefToUser = db.collection("users").document(userID);
        Object[] params = new Object[3];

        docRefToUser.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    params[0] = document.get("name");
                    params[1] = mAuth.getCurrentUser().getEmail();
                    params[2] = document.get("selectedRoutine");
                    try {
                        method.invoke(object, params);
                    } catch (IllegalAccessException ignore) {
                    } catch (InvocationTargetException ignore) {
                    }
                }
            }
        });
    }

    /**
     * Metode utilizat per obtenir la rutina seleccionada de l'usuari
     * @param userID l'identificador de l'usuari
     * @param method el metode a cridar
     * @param object l'objecte que conte el metode
     */
    public void getSelectedRoutine(String userID, Method method, Object object) {

        DocumentReference docRefToUser = db.collection("users").document(userID);
        Object[] params = new Object[1];

        docRefToUser.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    params[0] = document.get("selectedRoutine");
                    try {
                        method.invoke(object, params);
                    } catch (IllegalAccessException ignore) {
                    } catch (InvocationTargetException ignore) {
                    }
                }
            }
        });

    }

    /**
     * Canvi de contrasenya de l'usuari actual
     * @param newPassword nova contrasenya
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void changePassword(String newPassword, Method method, Object object) {
        FirebaseUser user = mAuth.getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    boolean success;

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            success = true;
                        }

                        try {
                            method.invoke(object, success);
                        } catch (IllegalAccessException ignore) {
                        } catch (InvocationTargetException ignore) {
                        }
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
     * Esborrar l'usuari actual i les seves rutines i activitats
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void deleteUser(Method method, Object object) {
        //reauthenticate(mail, oldPassword);
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        user.delete()
                .addOnCompleteListener(task -> {
                    boolean success = false;
                    if (task.isSuccessful()) {
                        DocumentReference docRefToUser = db.collection("users").document(userID);
                        deleteUserData(docRefToUser);
                        success = true;
                    }
                    try {
                        method.invoke(object, success);
                    } catch (IllegalAccessException ignore) {
                    } catch (InvocationTargetException ignore) {
                    }
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
                    Object[] params = new Object[6];

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();

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
                                                try {
                                                    method.invoke(object, params);
                                                } catch (IllegalAccessException ignore) {
                                                } catch (InvocationTargetException ignore) {
                                                }
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
                                                try {
                                                    method.invoke(object, params);
                                                } catch (IllegalAccessException ignore) {
                                                } catch (InvocationTargetException ignore) {
                                                }
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
        Object[] params = new Object[6];

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
                            docRefToUser.collection("routines").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot  document : task.getResult()) {
                                        ArrayList<String> routinesResult = new ArrayList<>();
                                        routinesResult.add(document.getId());
                                        routinesResult.add(document.get("name").toString());
                                        routineIds.add(routinesResult);
                                    }
                                }
                                params[5] = routineIds;
                                try {
                                    method.invoke(object, params);
                                } catch (IllegalAccessException ignore) {
                                } catch (InvocationTargetException ignore) {
                                }
                            });
                        }).addOnFailureListener(exception -> {
                            params[4] = null;

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
                                try {
                                    method.invoke(object, params);
                                } catch (IllegalAccessException ignore) {
                                } catch (InvocationTargetException ignore) {
                                }
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
                    Object[] params = new Object[6];

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();

                        DocumentReference docRefToUser = db.collection("users").document(userID);
                        docRefToUser.get().addOnSuccessListener(documentSnapshot -> {

                            if(!documentSnapshot.exists()) {
                                DocumentReference docRefToUser1 =  db.collection("users").document(userID);
                                HashMap<String, Object> mapa = new HashMap<>();
                                mapa.put("Username", user.getDisplayName());
                                mapa.put("selectedRoutine", "");
                                docRefToUser1.set(mapa);
                                updateProfilePic(userID, user.getPhotoUrl());
                            }

                            params[0] = true;
                            params[1] = userID;
                            params[2] = documentSnapshot.get("Username").toString();
                            params[3] = documentSnapshot.get("selectedRoutine");

                            //Codi que descarrega la foto de perfil de l'usuari de Google i la fica de perfil
                            /*
                            //Descarreguem la imatge
                            DownloadManager.Request request = new DownloadManager.Request(user.getPhotoUrl());
                            request.setTitle(userID);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, userID+".jpg");
                            DownloadManager manager = (DownloadManager) MainActivity.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);

                            //Quan acabem de descarregar:
                            BroadcastReceiver onComplete = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    String action = intent.getAction();
                                    if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                                        //Anem a la carpeta descarregues
                                        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                        File[] files = directory.listFiles();
                                        for (int i = 0; i < files.length; i++)
                                        {
                                            if(files[i].getName().equals(userID+".jpg")){ //quan trobem l'arxiu amb la id de l'usuari com a nom
                                                Bitmap bm = BitmapFactory.decodeFile(files[i].getAbsolutePath()); //el convertim a bitmap
                                                DomainAdapter.getInstance().updateProfilePic(bm); //el passem a domini
                                                files[i].delete(); //i l'eliminem
                                            }
                                        }
                                    }
                                }
                            };

                            //Crec que aixo s'ha de fer pero ni idea
                            MainActivity.getInstance().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                            //Fi del codi que carrega la imatge de Google*/

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
                                                try {
                                                    method.invoke(object, params);
                                                } catch (IllegalAccessException ignore) {
                                                } catch (InvocationTargetException ignore) {
                                                }
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
                                                try {
                                                    method.invoke(object, params);
                                                } catch (IllegalAccessException ignore) {
                                                } catch (InvocationTargetException ignore) {
                                                }
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
     * Reautenticació de l'usuari
     * @param mail correu de l'usuari
     * @param password contrasenya de l'usuari
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void reauthenticate(String mail, String password, Method method, Object object) {
        FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(mail, password);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    boolean success = false;
                    if(task.isSuccessful()) {
                        success = true;
                    }
                    try {
                        method.invoke(object, success);
                    } catch (IllegalAccessException ignore) {
                    } catch (InvocationTargetException ignore) {
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
     * Metode per fer sign out de l'usuari actual
     */
    public void signOut() {
        mAuth.getInstance().signOut();
    }


    /**
     * Esborra totes les dades de la BD de l'usuari (rutines i activitats)
     * @param docRefToUser es el document de l'usuari que conte totes les altres subcol·leccions
     */
    private void deleteUserData(DocumentReference docRefToUser) {

        docRefToUser.collection("routines").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnap : task.getResult()) {
                            DocumentReference docRefToRoutine = documentSnap.getReference();
                            deleteRoutineData(docRefToRoutine);

                        }
                    }
                    docRefToUser.delete();
                });
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

}
