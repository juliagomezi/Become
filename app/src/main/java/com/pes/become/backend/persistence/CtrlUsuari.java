package com.pes.become.backend.persistence;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CtrlUsuari {

    private FirebaseAuth mAuth;



    public void createAccount(String email, String password, Activity act,Method method, Object object){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Object[] params = new Object[1];
                            params[0] = user;





                            try {
                                method.invoke(object, params);
                            } catch (IllegalAccessException e1) {
                                System.out.println("Acces invàlid");
                            } catch (InvocationTargetException e2) {
                                System.out.println("Target no vàlid");
                            }
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
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


    public void onCreate(){
        mAuth = FirebaseAuth.getInstance();
    }



    public void onStart(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //Retornar si l'usuari ha iniciat sessio o no
        }
    }

}
