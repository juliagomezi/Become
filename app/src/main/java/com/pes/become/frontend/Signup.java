package com.pes.become.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private EditText emailText, userText, passwordText, passwordConfirm;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailText = findViewById(R.id.emailText);
        userText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        passwordConfirm = findViewById(R.id.passwordconfirmText);
        Button signUp = findViewById(R.id.signupButton);
        loading = findViewById(R.id.loading);

        signUp.setOnClickListener(v -> signUpUser());
    }

    private void signUpUser() {
        emailText.setError(null);
        userText.setError(null);
        passwordText.setError(null);
        passwordConfirm.setError(null);

        String email = emailText.getText().toString();
        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty()) emailText.setError(getString(R.string.notNull));
        else if(!isEmailValid(email)) emailText.setError(getString(R.string.notAValidEmail));
        else if (user.isEmpty()) userText.setError(getString(R.string.notNull));
        else if (password.length() < 6) passwordText.setError(getString(R.string.shortPassword));
        else if (password.isEmpty()) passwordText.setError(getString(R.string.notNull));
        else if (!password.equals(passwordConfirm.getText().toString())) {
            passwordText.setError(getString(R.string.passwords));
            passwordConfirm.setError(getString(R.string.passwords));
        }
        else {
            loading.setVisibility(View.VISIBLE);
            DA.registerUser(email, password, user, this);
        }
    }

    /**
     * Funcio per registrar un usuari i retorna error
     */
    public void registerCallbackFailed() {
        loading.setVisibility(View.INVISIBLE);
        emailText.setError(getString(R.string.emailExists));
    }

    /**
     * Funcio a executar despres de crear un usuari amb exit
     */
    public void registerCallback(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}