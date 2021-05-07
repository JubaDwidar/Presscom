package com.juba.presscom.ui.posts;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentSnapshot;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface PostContractor {


    interface PostView{}
    Completable postNewPost(String desc);
    Completable postPostImage(Bitmap image);
    Flowable<DocumentSnapshot> getUserInfo();
    Flowable<DocumentSnapshot> getPostInfo();
}
