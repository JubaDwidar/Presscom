package com.juba.presscom.ui.register;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

import static com.juba.presscom.utils.Constants.user_node;

public class RegisterContractorImp implements RegisterContractor {

    public static final String TAG = "RegisterContractorImp";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String uId;

    public RegisterContractorImp() {
    }

    @Override
    public Completable signUpUser(String email, String password) {

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        try {
                            uId = mAuth.getCurrentUser().getUid();
                        } catch (NullPointerException e) {
                        }
                        HashMap<String, String> map = new HashMap<>();
                        map.put("email", email);
                        map.put("password", password);
                        map.put("image", "");
                        map.put("name", "");
                        map.put("status", "");
                        map.put("id", uId);

                        firebaseFirestore.collection(user_node).document(uId).set(map)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "problem in save user" + e.getMessage());
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e(TAG, "onSuccess: saved successfully");
                            }
                        });
                    }
                });
            }
        });
    }
}
