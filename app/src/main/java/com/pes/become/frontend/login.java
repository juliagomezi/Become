package com.pes.become.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

public class login extends AppCompatActivity {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private EditText userText, passwordText;
    private Button login, googleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
            startActivity(new Intent(login.this, Signup.class));
        });


    }

    private void loginUser() {
        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (user.isEmpty()) userText.setError(getString(R.string.notNull));
        else if (password.isEmpty()) userText.setError(getString(R.string.notNull));

        else {
            /*try {
                DA.loginUser(user, password);
                startActivity(new Intent(login.this, MainActivity.class));
                finish();
            } catch (UserNotExists e) {
                    userText.setError(getString(R.string.userNotMatch));
                    passwordText.setError(getString(R.string.userNotMatch));
            }*/

        }

    }

    private void googleLoginUser() {

    }
}