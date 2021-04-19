package com.pes.become.backend.persistence;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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


    private CtrlUsuari(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    public static CtrlUsuari getInstance()
    {
        if(instance ==null) {
            instance = new CtrlUsuari();
        }
        return instance;
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
    public void registerUser(String mail, String password,String name, Activity act,Method method, Object object){

        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        boolean success;
                        if (task.isSuccessful()) {

                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();

                            DocumentReference docRefToUser =  db.collection("users").document(userID);
                            HashMap<String, Object> mapa = new HashMap<>();
                            mapa.put("Username", name);

                            docRefToUser.set(mapa);
                            success =true;
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w("FirebaseUser", "createUserWithEmail:failure", task.getException());
                            success=false;
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
     * Inici de sessió d'usuari
     * @param mail correu
     * @param password contrasenya
     * @param act Activity d'Android necessaria per la execucio del firebase
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void loginUser(String mail, String password, Activity act,Method method, Object object){
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        boolean success;
                        if (task.isSuccessful()) {
                            Log.d("FirebaseUser", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            success = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FirebaseUser", "signInWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    //Toast.LENGTH_SHORT).show();
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
     * Esborrar l'usuari actual i les seves rutines i activitats
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void deleteUser(Method method, Object object){
        //reauthenticate(mail, oldPassword);
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean success;
                        if (task.isSuccessful()) {
                            Log.d("FirebaseUser", "User account deleted.");
                            //Cal borrar l'usuari, encaran no implementat
                            success = true;
                        }else{
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
     * Canvi de contrasenya de l'usuari actual
     * @param newPassword nova contrasenya
     * @param method mètode a executar de forma asíncrona un cop acabada la reautentificació (el paràmetre és un boolea que retorna true si la reautentificació ha anat bé o false si no)
     * @param object instancia de la classe del mètode a executar
     */
    public void changePassword(String newPassword, Method method, Object object){
        FirebaseUser user = mAuth.getCurrentUser();

        user.updatePassword(newPassword)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                boolean success = false;
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseUser", "User password updated.");
                        success = true;
                    }
                    else {
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
