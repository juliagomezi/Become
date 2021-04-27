package com.pes.become.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

public class LogoScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private final DomainAdapter DA = DomainAdapter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_screen);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            DA.loadUser(this);
        }
        else {
            startActivity(new Intent(this, Login.class));
            finish();
        }
    }

    public void loginCallback() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void loginCallbackFailed() {
        startActivity(new Intent(this, Login.class));
        finish();
    }


}