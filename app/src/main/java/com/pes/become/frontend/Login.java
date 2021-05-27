package com.pes.become.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private EditText userText, passwordText;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    private CallbackManager callbackManager = CallbackManager.Factory.create();

    private ProgressBar loading;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createRequest();

        userText = findViewById(R.id.userText);
        passwordText = findViewById(R.id.passText);
        Button login = findViewById(R.id.logInButton);
        Button googleLogin = findViewById(R.id.googleLoginButton);
        LoginButton facebooklogin = findViewById(R.id.facebookLoginButton);
        TextView signUp = findViewById(R.id.signUp);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        loading = findViewById(R.id.loading);

        login.setOnClickListener(v-> loginUser());

        googleLogin.setOnClickListener(v -> googleLoginUser());

        facebooklogin.setOnClickListener(v-> {
            Log.d("Boto", "click al boto");
            facebookLoginUser();
        });

        signUp.setOnClickListener(v -> startActivity(new Intent(Login.this, Signup.class)));

        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPassword.class));
            finish();
        });
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void loginUser() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);

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
        callbackManager.onActivityResult(requestCode, resultCode, data);

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

    private void facebookLoginUser() {
        Log.d("login", "intent de login");
        LoginManager.getInstance().logInWithReadPermissions(this, Collections.singleton("name"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("successFacebook", "facebook:onSuccess:" + loginResult);
                        DA.loginFacebookUser(loginResult.getAccessToken(), Login.this);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("cancelFacebook", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("errorFacebook", "facebook:onError", error);
                    }
                });
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

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}