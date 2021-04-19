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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CtrlUsuari {

    private FirebaseAuth mAuth;
    private final FirebaseFirestore db;


    public CtrlUsuari(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Registre d'un nou usuari
     * @param mail correu de l'usuari que es vol crear
     * @param password contrasenya de l'usuari que es vol crear
     * @param name nom de l'usuari que es vol crear
     * @param act
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void registerUser(String mail, String password,String name, Activity act,Method method, Object object){

        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();

                            DocumentReference docRefToUser =  db.collection("users").document(userID);


                            Object[] params = new Object[1];
                            params[0] = user;
                            params[1] = userID;
                            try {
                                method.invoke(object, params);
                            } catch (IllegalAccessException e1) {
                                System.out.println("Acces invàlid");
                            } catch (InvocationTargetException e2) {
                                System.out.println("Target no vàlid");
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FirebaseUser", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    /**
     * Inici de sessió d'usuari
     * @param mail correu
     * @param password contrasenya
     * @param act
     * @param method metode a cridar quan es retornin les dades
     * @param object classe que conté el mètode
     */
    public void getUser(String mail, String password, Activity act,Method method, Object object){
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Object[] params = new Object[1];
                            params[0] = user;
                            params[1] = user.getUid();
                            params[2] = user.getDisplayName();
                            try {
                                method.invoke(object,params);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FirebaseUser", "signInWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    //Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * SUPER PROVISIONAL Metode per a esborrar l'usuari actual
     */
    public void BORRAR_USUARI_PROVISIONAL(){
        FirebaseUser user = mAuth.getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseUser", "User account deleted.");
                        }
                    }
                });
    }

    /**
     * SUPER PROVISIONAL Metode per a canviar el password de l'usuari actual
     * @param oldPassword
     * @param newPassword
     */
    public void changePassword(String oldPassword, String newPassword){
        FirebaseUser user = mAuth.getCurrentUser();


        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseUser", "User password updated.");
                        }
                    }
                });
    }

    /**
     * SUPER PROVISIONAL Reautentificacio per borrar i canviar contrasenya
     * @param mail
     * @param password
     */
    public void reauthenticate(String mail, String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(mail, password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("FirebaseUser", "User re-authenticated.");
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
