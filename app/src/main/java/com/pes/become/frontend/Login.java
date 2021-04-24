package com.pes.become.frontend;

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
import com.google.android.gms.tasks.Task;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

public class Login extends AppCompatActivity {


    private final DomainAdapter DA = DomainAdapter.getInstance();
    private EditText userText, passwordText;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createRequest();

        userText = findViewById(R.id.userText);
        passwordText = findViewById(R.id.passText);
        Button login = findViewById(R.id.logInButton);
        Button googleLogin = findViewById(R.id.googleLoginButton);
        TextView signUp = findViewById(R.id.signUp);

        login.setOnClickListener(v-> loginUser());

        googleLogin.setOnClickListener(v -> googleLoginUser());

        signUp.setOnClickListener(v -> startActivity(new Intent(Login.this, Signup.class)));


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
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                DA.loginGoogleUser(account.getIdToken(), this);

            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

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