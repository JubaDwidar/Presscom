package com.juba.presscom.ui.settings;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.juba.presscom.R;
import com.juba.presscom.ui.main.MainActivity;
import com.juba.presscom.utils.DataConverter;
import com.vanniktech.rxpermission.RealRxPermission;

import java.io.File;
import java.io.IOException;

import androidx.lifecycle.ViewModelProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity implements SettingsContractor.SettingsView {
    private CircleImageView settingsImage;
    private Button setupBtn;
    private EditText settingsName, settingsStatus;
    private Toolbar settingsToolbar;
    private SettingsPresenter settingsPresenter;

    private static final int REQUEST_CODE = 1;
    private static final String TAG = "SettingsViewModel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        try {

            settingsToolbar = findViewById(R.id.settings_toolbar);
            setSupportActionBar(settingsToolbar);
            getSupportActionBar().setTitle("Settings");
        } catch (NullPointerException e) {
        }


        initialize();
        observeInfoData();

        settingsImage.setOnClickListener(v -> showGallery());
        setupBtn.setOnClickListener(view -> {
            updateUserInfo();
        });


    }

    private void initialize() {
        Glide.get(this);
        settingsPresenter = new SettingsPresenter(this);
        setupBtn = findViewById(R.id.settings_button);
        settingsImage = findViewById(R.id.settings_image);
        settingsName = findViewById(R.id.settings_name);
        settingsStatus = findViewById(R.id.settings_status);
    }

    private void sendToMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    public void updateUserInfo() {
        String name = settingsName.getText().toString();
        String status = settingsStatus.getText().toString();

        if (!name.isEmpty() || !status.isEmpty()) {
            settingsPresenter.updateUserInfo(name, status);
            sendToMainActivity();

        } else {
            Toast.makeText(this, "Check your Name and Status", Toast.LENGTH_LONG).show();
        }
    }

    private void observeInfoData() {
        settingsPresenter.userInfoLiveData().observe(this, users -> {
            if (users != null) {
                settingsName.setText(users.getName());
                settingsStatus.setText(users.getStatus());

                Glide.with(SettingsActivity.this)
                        .load(users.getImage())
                        .placeholder(R.drawable.profile_image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(settingsImage);


            }
        });
    }


    private void showGallery() {
        //Storage permission
        RealRxPermission.getInstance(getApplicationContext())
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe();
        //Open gallery Intent
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    convertToByte(selectedImage);
                }
            }
        }
    }

    private void convertToByte(Uri selectedImage) {
        File imageFile = new File(DataConverter.getRealPathFromURI(selectedImage, this));
        try {
            Bitmap bitmap = new Compressor(getApplication()).compressToBitmap(imageFile);

            Log.e(TAG, "convertToByte: " + bitmap.toString());
            settingsPresenter.updateUserImage(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}