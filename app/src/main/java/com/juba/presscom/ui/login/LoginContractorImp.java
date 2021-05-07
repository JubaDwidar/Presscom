package com.juba.presscom.ui.login;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Completable;

public class LoginContractorImp implements LoginContractor {

    public LoginContractorImp() {
    }

    public static final String TAG = "LoginContractorImp";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public Completable SignInUser(String email, String Password) {
        return Completable.create(emitter -> mAuth.signInWithEmailAndPassword(email, Password)
                .addOnFailureListener(e -> Log.e(TAG, "sign in error" + e.getMessage())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        Log.e(TAG, "sign in completed");
                    }
                }));
    }
}
