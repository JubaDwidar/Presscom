package com.juba.presscom.ui.settings;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentSnapshot;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface SettingsContractor {
    Completable updateUserImage(Bitmap image);
    Completable updateUserInfo(String name, String status);
    Flowable<DocumentSnapshot> getUserInfo();

    interface SettingsView{


    }
}
