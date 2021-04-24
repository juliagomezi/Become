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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

                    Object[] params = new Object[1];
                    //Això té 1 element
                    // un bool que diu si hi ha hagut exit o no
                    if (task.isSuccessful()) {
                        DocumentReference docRefToUser = db.collection("users").document(userID);
                        deleteUserData(docRefToUser);
                        params[0] = true;
                    }
                    else {
                        params[0] = false;
                    }

                    try {
                        method.invoke(object, params);
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
    public void loginUser(String mail, String password, Activity act,Method method, Object object) {
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(act, task -> {
                    Object[] params = new Object[5];

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

                                            try {
                                                method.invoke(object, params);
                                            } catch (IllegalAccessException ignore) {
                                            } catch (InvocationTargetException ignore) {
                                            }
                                        }).addOnFailureListener(exception -> {
                                            params[4] = null;

                                            if (params[3] == null) params[3]="";
                                            else params[3] = params[3].toString();

                                            try {
                                                method.invoke(object, params);
                                            } catch (IllegalAccessException ignore) {
                                            } catch (InvocationTargetException ignore) {
                                            }
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
        Object[] params = new Object[5];

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

                            try {
                                method.invoke(object, params);
                            } catch (IllegalAccessException ignore) {
                            } catch (InvocationTargetException ignore) {
                            }
                        }).addOnFailureListener(exception -> {
                            params[4] = null;

                            if (params[3] == null) params[3]="";
                            else params[3] = params[3].toString();

                            try {
                                method.invoke(object, params);
                            } catch (IllegalAccessException ignore) {
                            } catch (InvocationTargetException ignore) {
                            }
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
                    Object[] params = new Object[5];

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
                            }

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

                                            try {
                                                method.invoke(object, params);
                                            } catch (IllegalAccessException ignore) {
                                            } catch (InvocationTargetException ignore) {
                                            }
                                        }).addOnFailureListener(exception -> {
                                            params[4] = null;

                                            if (params[3] == null) params[3]="";
                                            else params[3] = params[3].toString();

                                            try {
                                                method.invoke(object, params);
                                            } catch (IllegalAccessException ignore) {
                                            } catch (InvocationTargetException ignore) {
                                            }
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
                        docRefToUser.delete();
                    }
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
                        docRefToRoutine.delete();
                    }
                });
    }

}
