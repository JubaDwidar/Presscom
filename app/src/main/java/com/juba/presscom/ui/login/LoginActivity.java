package com.juba.presscom.ui.login;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.juba.presscom.R;
import com.juba.presscom.ui.main.MainActivity;
import com.juba.presscom.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements LoginContractor.LoginView {

    private EditText email, Password;
    private Button loginBtn, createAccountBtn;
   // private LoginViewModel loginViewModel;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

        loginBtn.setOnClickListener(view -> loginOfUser());

        createAccountBtn.setOnClickListener(view -> sendUserForRegistration());
    }

    private void sendUserForRegistration()

    {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);

    }

    private void initialize() {

      //  loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginPresenter=new LoginPresenter(this);
        loginBtn = findViewById(R.id.login_btn);
        createAccountBtn = findViewById(R.id.create_button);
        email = findViewById(R.id.login_name);
        Password = findViewById(R.id.login_password);

    }


    private void loginOfUser() {
        String email = this.email.getText().toString();
        String password = Password.getText().toString();

        if ( !email.isEmpty() ||  !password.isEmpty()) {
            loginPresenter.SignInUser(email, password);
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(this, "login ed successfully", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Check your Email and Password", Toast.LENGTH_LONG).show();

        }

    }


}
