package com.pes.become.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

public class Signup extends AppCompatActivity {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private EditText emailText, userText, passwordText, passwordConfirm;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailText = findViewById(R.id.emailText);
        userText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        passwordConfirm = findViewById(R.id.passwordconfirmText);
        signUp = findViewById(R.id.signupButton);

        signUp.setOnClickListener(v -> {
            signUpUser();
        });
    }

    private void signUpUser() {
        String email = emailText.getText().toString();
        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty()) emailText.setError(getString(R.string.notNull));
        else if (user.isEmpty()) userText.setError(getString(R.string.notNull));
        else if (password.isEmpty()) userText.setError(getString(R.string.notNull));
        else if (!password.equals(passwordConfirm.getText().toString())) {
            passwordText.setError(getString(R.string.passwords));
            passwordConfirm.setError(getString(R.string.passwords));
        }
        else {
             /*try {
                DA.signUp(email, user, password);
                startActivity(new Intent(Signup.this, MainActivity.class));
                finish();
            } catch (UserExists e) {
                    emailText.setError(getString(R.string.emailExists));
            }*/
        }
    }
}