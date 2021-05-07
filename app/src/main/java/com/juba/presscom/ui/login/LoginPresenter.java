package com.juba.presscom.ui.login;

import android.util.Log;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter {

    private static final String TAG = "LoginPresenter";
    private LoginContractor loginContractor;
    private LoginContractor.LoginView loginView;

    public LoginPresenter(LoginContractor.LoginView loginView) {
        this.loginView = loginView;
        this.loginContractor = new LoginContractorImp();
    }

    public void SignInUser(String email, String Password) {
        loginContractor.SignInUser(email, Password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: sign in complete ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: sign in error" + e.getMessage());
                    }
                });

    }


}
