package com.pes.become.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.lang.reflect.Method;

public class Login extends AppCompatActivity {


    private final DomainAdapter DA = DomainAdapter.getInstance();
    private EditText userText, passwordText;
    private Button login, googleLogin;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123; //el valor es indiferent
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createRequest();
        mAuth = FirebaseAuth.getInstance();

        userText = findViewById(R.id.userText);
        passwordText = findViewById(R.id.passText);
        login = findViewById(R.id.logInButton);
        googleLogin = findViewById(R.id.googleLoginButton);
        TextView signUp = findViewById(R.id.signUp);

        login.setOnClickListener(v-> {
            loginUser();
        });

        googleLogin.setOnClickListener(v -> {
            googleLoginUser();
        });

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Signup.class));
        });


    }

    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void loginUser() {
        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (user.isEmpty()) userText.setError(getString(R.string.notNull));
        else if (password.isEmpty()) userText.setError(getString(R.string.notNull));

        DA.loginUser(user, password, this);

    }

    private void googleLoginUser() {
        //mostra la pantalla per veure els usuaris que tens amb google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                DA.loginGoogleUser(account.getIdToken(), this);

            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user !=null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }*/

    /**
     * Funcio de callback per loguejar l'usuari sense exit
     */
    public void loginCallbackFailed(){
        Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
    }

    /**
     * Funcio de callback per loguejar l'usuari amb exit
     */
    public void loginCallback() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}