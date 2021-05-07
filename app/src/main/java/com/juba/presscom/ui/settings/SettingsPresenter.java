package com.juba.presscom.ui.settings;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.juba.presscom.model.Users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsPresenter {

    private SettingsContractor settingsContractor;
    public static final String TAG = "SettingsPresenter";
    private MediatorLiveData<Users> userInfo = new MediatorLiveData();
    private SettingsContractor.SettingsView settingsView;

    public SettingsPresenter(SettingsContractor.SettingsView settingsView) {
        this.settingsView = settingsView;
        settingsContractor = new SettingsContractorImp();

        getUserInfo();

    }


    public void updateUserImage(Bitmap image) {
        settingsContractor.updateUserImage(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: image complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: image failed" + e.getMessage());
                    }
                });
    }

    public void updateUserInfo(String name, String status) {
        settingsContractor.updateUserInfo(name, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: update info complete");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: update info failed" + e.getMessage());

                    }
                });
    }

    private void getUserInfo() {
        settingsContractor.getUserInfo()
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
                        userInfo.setValue(user);
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

    public LiveData<Users> userInfoLiveData() {
        return userInfo;
    }


}
