package com.juba.presscom.ui.register;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.juba.presscom.R;
import com.juba.presscom.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity implements RegisterContractor.RegisterView {
    private EditText registerUsername, registerPassword;
    private Button registerBtn, signingBtn;
    private ResgisterPresenter resgisterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
        registerBtn.setOnClickListener(view -> {
            createNewAccount();
            sendUserForLogin();
        });

        signingBtn.setOnClickListener(view -> sendUserForLogin());

    }

    private void createNewAccount() {
        String email = registerUsername.getText().toString();
        String password = registerPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(this, "Check your Email and Password", Toast.LENGTH_LONG).show();


        } else {
            resgisterPresenter.signUpUser(email, password);
            Toast.makeText(this, "registered successfully", Toast.LENGTH_LONG).show();

        }
    }

    private void initialize() {
       resgisterPresenter=new ResgisterPresenter(this);
        registerBtn = findViewById(R.id.signup_btn);
        registerUsername = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        signingBtn = findViewById(R.id.signing_button);

    }

    private void sendUserForLogin()

    {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);

    }

}
