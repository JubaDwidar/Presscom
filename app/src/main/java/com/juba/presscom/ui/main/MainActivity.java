package com.juba.presscom.ui.main;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.juba.presscom.R;
import com.juba.presscom.ui.main.Home.HomeFragment;
import com.juba.presscom.ui.posts.AddPostActivity;
import com.juba.presscom.ui.settings.SettingsActivity;
import com.juba.presscom.ui.login.LoginActivity;

import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements MainContractor.MainView {
    public static final String TAG = "MainViewModel";

    private Toolbar mainToolBar;
    private HomeFragment homeFragment;
    private ExtendedFloatingActionButton floatingActionButton;
    private FirebaseAuth auth;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainPresenter = new MainPresenter(this);
        auth = FirebaseAuth.getInstance();
        floatingActionButton = findViewById(R.id.float_btn);
        try {
            mainToolBar = findViewById(R.id.main_toolbar);
            mainToolBar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(mainToolBar);
            getSupportActionBar().setTitle("PressCom");

        } catch (NullPointerException e) {
        }

        homeFragment = new HomeFragment();
        FragmentTransaction(homeFragment);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: fab clicked");
                mainPresenter.goAdd();
                mainPresenter.getUserInfo();
                mainPresenter.mainUserImage();
                moveToPost();

            }
        });
    }

    private void moveToPost() {
        Log.d(TAG, "onClick: move to post");

        Intent intent = new Intent(this, AddPostActivity.class);
        startActivity(intent);
    }


    private void sendToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.acount_setup) {
            sendToSetup();
        } else if (id == R.id.logout_setup) {

            mainPresenter.logOut();
            sendToLogin();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendToSetup() {
        Intent setupIntent = new Intent(this, SettingsActivity.class);
        startActivity(setupIntent);
    }


    public void FragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null) {
            moveToLogin();
        }
    }

    private void moveToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
