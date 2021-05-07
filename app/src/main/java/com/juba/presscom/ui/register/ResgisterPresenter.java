package com.juba.presscom.ui.register;

import android.util.Log;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ResgisterPresenter  {

    private static final String TAG = "ResgisterPresenter";
    private RegisterContractor registerContractor;
    private RegisterContractor.RegisterView registerView;

    public ResgisterPresenter(RegisterContractor.RegisterView registerView) {
        this.registerView = registerView;
        registerContractor=new RegisterContractorImp();
    }

    public void signUpUser(String email, String password) {
        registerContractor.signUpUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: sign up complete ");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: sign up error" + e.getMessage());

                    }
                });
    }
}
