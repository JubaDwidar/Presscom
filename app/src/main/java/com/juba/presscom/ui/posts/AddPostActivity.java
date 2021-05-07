package com.juba.presscom.ui.posts;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import id.zelory.compressor.Compressor;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.juba.presscom.R;
import com.juba.presscom.ui.main.MainActivity;
import com.juba.presscom.utils.DataConverter;
import com.vanniktech.rxpermission.RealRxPermission;

import java.io.File;
import java.io.IOException;

public class AddPostActivity extends AppCompatActivity implements PostContractor.PostView {
    private ImageView postImage;
    private Button postBtn;
    private EditText postDesc;
    private Toolbar pToolbar;
    private static final int REQUEST_CODE = 1;
  //  private AddPostViewModel postViewModel;
    private PostsPresenter postsPresenter;
    public static final String TAG = "AddPostActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
     //   postViewModel = new ViewModelProvider(this).get(AddPostViewModel.class);
postsPresenter=new PostsPresenter(this);
        initialize();
        observeImage();

        postImage.setOnClickListener(v ->
                showGallery());

        postBtn.setOnClickListener(view -> {
            putAPost();
            moveToMain();

        });

    }

    public void putAPost() {
        String desc = postDesc.getText().toString();
        postsPresenter.postNewPost(desc);

    }

    private void moveToMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    public void observeImage() {
        postsPresenter.postInfo().observe(this, post -> {

            if (post != null) {
                Log.e(TAG, "observer image: " + post.getDesc());
                Glide.with(AddPostActivity.this)
                        .load(post.getImage())
                        .placeholder(R.drawable.ic_photo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(postImage);

            } else {
                Log.e(TAG, "onChanged: error");
            }
        });
    }


    private void initialize() {

        Glide.get(this);

        try {
            pToolbar = findViewById(R.id.add_post_toolbar);
            setSupportActionBar(pToolbar);
            getSupportActionBar().setTitle("Add New Post");

        } catch (NullPointerException e) {
        }
        postBtn = findViewById(R.id.post_btn);
        postImage = findViewById(R.id.post_image);
        postDesc = findViewById(R.id.post_desc);
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
            postsPresenter.postPostImage(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
