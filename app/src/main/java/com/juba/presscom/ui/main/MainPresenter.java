package com.juba.presscom.ui.main;

import android.util.Log;
import com.google.firebase.firestore.DocumentSnapshot;
import com.juba.presscom.model.Users;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {
    private MainContractor mainContractor;
    private MainContractor.MainView mainView;
    public static final String TAG = "MainPresenter";
    private String userImage;
    private String username;


    public MainPresenter(MainContractor.MainView mainView) {
        this.mainView = mainView;
        mainContractor = new MainContractorImp();
        getUserInfo();

    }


    public void goAdd() {
        mainContractor.goToAddActivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: go add complete");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: go add failure" + e.getMessage());
                    }
                });
    }

    public void getUserInfo() {
        mainContractor.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<DocumentSnapshot>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DocumentSnapshot documentSnapshot) {
                        Users user = documentSnapshot.toObject(Users.class);
                        try {
                            userImage = user.getImage();
                            username = user.getName();
                        } catch (NullPointerException ignored) {
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: user failed" + e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void mainUserImage() {
        mainContractor.mainUserImage(userImage, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: main userAImage completed");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    public void logOut() {
        mainContractor.signOut();
    }
}
