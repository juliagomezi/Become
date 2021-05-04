package com.pes.become.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private TextView mail, sent;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Button done = findViewById(R.id.doneButton);
        done.setOnClickListener(v -> sendPassResetEmail());

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });

        mail = findViewById(R.id.mail);
        loading = findViewById(R.id.loading);
        sent = findViewById(R.id.emailSentText);
    }

    /**
     * Metode per recuperar la contrasenya
     */
    private void sendPassResetEmail() {
        sent.setVisibility(View.GONE);
        String user = mail.getText().toString();
        if (user.isEmpty()) mail.setError(getString(R.string.notNull));
        else if(!isEmailValid(user)) mail.setError(getString(R.string.notAValidEmail));
        else {
            loading.setVisibility(View.VISIBLE);
            DomainAdapter.getInstance().sendPassResetEmail(user,this);
        }
    }

    /**
     * Metode per rebre la resposta de la base de dades del mail de recuperacio de la contrasenya
     * @param success resultat de l'operacio
     */
    public void passResetCallback(boolean success) {
        loading.setVisibility(View.GONE);
        if(success) {
            mail.setText("");
            sent.setVisibility(View.VISIBLE);
        }
        else mail.setError(getString(R.string.emailNotRegistered));
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
