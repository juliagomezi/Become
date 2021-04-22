package com.pes.become.backend.persistence;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

/*import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;*/
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pes.become.frontend.MainActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CtrlUsuari {
    private static CtrlUsuari instance;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    /**
     * Creadora per defecte.
     */
    private CtrlUsuari(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static CtrlUsuari getInstance() {
        if (instance == null) {
            instance = new CtrlUsuari();
        }
        return instance;
    }

    /***************CONSULTORES***************/

    /**
     * Funcio per obtenir el nom, el correu i la rutina seleccionada de l'usuari en aquest ordre
     * @param userID l'identificador de l'usuari
     * @param method el metode a cridar
     * @param object l'objecte que conte el metode
     */
    public void getInfoUser(String userID, Method method, Object object) {
        DocumentReference docRefToUser = db.collection("users").document(userID);
        Object[] params = new Object[3];

        docRefToUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        params[0] = document.get("name");
                        params[1] = mAuth.getCurrentUser().getEmail();
                        params[2] = document.get("selectedRoutine");
                        try {
                            method.invoke(object, params);
                        } catch (IllegalAccessException e1) {
                            System.out.println("Acces invàlid");
                        } catch (InvocationTargetException e2) {
                            System.out.println("Target no vàlid");
                        }
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

        docRefToUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        params[0] = document.get("selectedRoutine");
                        try {
                            method.invoke(object, params);
                        } catch (IllegalAccessException e1) {
                            System.out.println("Acces invàlid");
                        } catch (InvocationTargetException e2) {
                            System.out.println("Target no vàlid");
                        }
                    }
                }
            }
        });

    }

    /***************MODIFICADORES***************/

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
                    Object[] params = new Object[1];

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseUser", "User password updated.");
                            params[0] = true;
                        }
                        else {
                            params[0] = false;
                        }

                        try {
                            method.invoke(object, params);
                        } catch (IllegalAccessException e1) {
                            System.out.println("Acces invàlid");
                        } catch (InvocationTargetException e2) {
                            System.out.println("Target no vàlid");
                        }
                    }
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
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Object[] params = new Object[1];
                        //Això té 1 element
                        // un bool que diu si hi ha hagut exit o no
                        if (task.isSuccessful()) {
                            Log.d("FirebaseUser", "User account deleted.");
                            DocumentReference docRefToUser = db.collection("users").document(userID);
                            deleteUserData(docRefToUser);
                            params[0] = true;
                        }
                        else {
                            params[0] = false;
                        }

                        try {
                            method.invoke(object, params);
                        } catch (IllegalAccessException e1) {
                            System.out.println("Acces invàlid");
                        } catch (InvocationTargetException e2) {
                            System.out.println("Target no vàlid");
                        }
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
                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        boolean success;
                        Object[] params = new Object[4];
                        //Això té 4 elements
                        // un bool que diu si hi ha hagut exit o no
                        // el ID de l'usuari
                        //el nom de l'usuari
                        //el id de la seva rutina seleccionada


                        if (task.isSuccessful()) {
                            Log.d("FirebaseUser", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();

                            DocumentReference docRefToUser = db.collection("users").document(userID);
                            docRefToUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    params[0] = true;
                                    params[1] = userID;
                                    params[2] = documentSnapshot.get("name");
                                    params[3] = documentSnapshot.get("selectedRoutine");
                                    try {
                                        method.invoke(object, params);
                                    } catch (IllegalAccessException e1) {
                                        System.out.println("Acces invàlid");
                                    } catch (InvocationTargetException e2) {
                                        System.out.println("Target no vàlid");
                                    }
                                }
                            });
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("FirebaseUser", "signInWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                            //Toast.LENGTH_SHORT).show();
                            params[0] = false;
                            params[1] = params[2] = params[3] = "";
                        }

                    }
                });
    }

    /**
     * Reautenticació de l'usuari
     * @param mail correu
     * @param password contrasenya
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void reauthenticate(String mail, String password, Method method, Object object) {
        FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(mail, password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean success = false;
                        if(task.isSuccessful()) {
                            Log.d("FirebaseUser", "User re-authenticated.");
                            success = true;
                        }
                        else
                        {
                            success = false;
                        }
                        Object[] params = new Object[1];
                        params[0] = success;
                        try {
                            method.invoke(object, params);
                        } catch (IllegalAccessException e1) {
                            System.out.println("Acces invàlid");
                        } catch (InvocationTargetException e2) {
                            System.out.println("Target no vàlid");
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
                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        boolean success;
                        Object[] params = new Object[3];
                        //Això té tres elements:
                        // 1-un bool que diu si ha tingut exit o no
                        // 2-el ID de l'usuari
                        // 3-nom de l'usuari
                        if (task.isSuccessful()) {

                            Log.d("TAG", "createUserWithEmail:success");
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
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            //Log.w("FirebaseUser", "createUserWithEmail:failure", task.getException());
                            params[0] = false;
                            params[1] = params[2] = "";
                        }
                        try {
                            method.invoke(object, params);
                        } catch (IllegalAccessException e1) {
                            System.out.println("Acces invàlid");
                        } catch (InvocationTargetException e2) {
                            System.out.println("Target no vàlid");
                        }
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

    public void signOut() {
        mAuth.getInstance().signOut();
    }

    /***************PRIVADES***************/

    /**
     * Esborra totes les dades de la BD de l'usuari (rutines i activitats)
     * @param docRefToUser es el document de l'usuari que conte totes les altres subcol·leccions
     */
    private void deleteUserData(DocumentReference docRefToUser) {

        docRefToUser.collection("routines").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnap : task.getResult()) {
                                DocumentReference docRefToRoutine = documentSnap.getReference();
                                deleteRoutineData(docRefToRoutine);

                            }
                            docRefToUser.delete();
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Esborra totes les dades d'una rutina i després esborra el document de la rutina.
     * @param docRefToRoutine és el document de l'usuari que conté totes les altres subcol·leccions.
     */
    private void deleteRoutineData(DocumentReference docRefToRoutine) {
        docRefToRoutine.collection("activities").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnap : task.getResult()) {
                                DocumentReference docRefToActivity = documentSnap.getReference();
                                docRefToActivity.delete();
                            }
                            docRefToRoutine.delete();
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
        /*
    //TUTORIAL
    public void tutorial(String routineName, String day)  {

        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = ControllerPersistence.class.getMethod("dothingsWithActivity", parameterTypes);
        this.getActivitiesByDay(routineName,day,method1,this);
    }

    public void dothingsWithActivity(ArrayList<String> message)
    {
        for(String a: message) Log.d("DoThingsWithActivity", a);
    }
    //FI TUTORIAL*/

}
