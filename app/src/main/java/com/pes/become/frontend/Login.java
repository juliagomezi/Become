package com.pes.become.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {


    private final DomainAdapter DA = DomainAdapter.getInstance();
    private EditText userText, passwordText;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    private ProgressBar loading;

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
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        loading = findViewById(R.id.loading);

        login.setOnClickListener(v-> loginUser());

        googleLogin.setOnClickListener(v -> googleLoginUser());

        signUp.setOnClickListener(v -> startActivity(new Intent(Login.this, Signup.class)));

        forgotPassword.setOnClickListener(v -> sendPassResetEmail());
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void loginUser() {
        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (user.isEmpty()) userText.setError(getString(R.string.notNull));
        else if(!isEmailValid(user)) userText.setError(getString(R.string.notAValidEmail));
        else if (password.isEmpty()) passwordText.setError(getString(R.string.notNull));
        else {
            loading.setVisibility(View.VISIBLE);
            DA.loginUser(user, password, this);
        }
    }

    private void googleLoginUser() {
        loading.setVisibility(View.VISIBLE);
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
    public void loginCallbackFailed() {
        userText.setError(getString(R.string.loginfailed));
        passwordText.setError(getString(R.string.loginfailed));
        loading.setVisibility(View.INVISIBLE);
    }

    /**
     * Funcio de callback per loguejar l'usuari amb exit
     */
    public void loginCallback() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * Metode per recuperar la contrasenya
     */
    private void sendPassResetEmail() {
        String user = userText.getText().toString();
        if (user.isEmpty()) userText.setError(getString(R.string.notNull));
        else if(!isEmailValid(user)) userText.setError(getString(R.string.notAValidEmail));
        else {
            loading.setVisibility(View.VISIBLE);
            DA.sendPassResetEmail(user,this);
        }
    }

    /**
     * Metode per rebre la resposta de la base de dades del mail de recuperacio de la contrasenya
     * @param success resultat de l'operacio
     */
    public void passResetCallback(boolean success) {
        loading.setVisibility(View.INVISIBLE);
        if(success) {
            userText.setText("");
            Toast.makeText(this, getString(R.string.emailSent), Toast.LENGTH_SHORT).show();
        }
        else userText.setError(getString(R.string.emailNotRegistered));
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}