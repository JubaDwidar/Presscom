package com.juba.presscom.ui.main;

import com.google.firebase.firestore.DocumentSnapshot;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface MainContractor {

    Completable goToAddActivity();

    Flowable<DocumentSnapshot> getUserInfo();

    Completable mainUserImage(String image, String name);

    void signOut();

    interface MainView{}
}
