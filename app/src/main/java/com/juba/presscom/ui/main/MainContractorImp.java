package com.juba.presscom.ui.main;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;

import static com.juba.presscom.utils.Constants.post_node;
import static com.juba.presscom.utils.Constants.user_node;

public class MainContractorImp implements MainContractor {


    private static final String TAG = "MainContractorImp";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public MainContractorImp() {
    }

    public String getUserId() {

        return auth.getCurrentUser().getUid();
    }

    public String getDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date);
        return currentDate;
    }

    public String UserDate() {
        return getUserId() + getDate();
    }


    @Override
    public Completable goToAddActivity() {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                HashMap<String, String> goAddActivity = new HashMap<>();
                goAddActivity.put("id", getUserId());
                goAddActivity.put("desc", "");
                goAddActivity.put("name", "");
                goAddActivity.put("image", "");
                goAddActivity.put("uImage", "");


                Log.e(TAG, "userDate in post " + UserDate());
                firestore.collection(post_node).document(UserDate()).set(goAddActivity).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure:go add fail" + e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

            }
        });
    }

    @Override
    public Flowable<DocumentSnapshot> getUserInfo() {
        return Flowable.create(emitter -> {
            final DocumentReference reference = firestore.collection(user_node).document(getUserId());
            final ListenerRegistration registration = reference.addSnapshotListener((documentSnapshot, e) -> {

                if (documentSnapshot != null) {
                    emitter.onNext(documentSnapshot);
                }
            });

            emitter.setCancellable(() -> registration.remove());
        }, BackpressureStrategy.BUFFER);

    }

    @Override
    public Completable mainUserImage(String uImage, String name) {

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                DocumentReference reference = firestore.collection(post_node).document(UserDate());
                DocumentReference documentReference = firestore.collection(post_node).document(UserDate());
                documentReference.update("uImage", uImage).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: add image ac" + e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e(TAG, "onComplete: add image acc completed");
                        reference.update("name", name).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: name faileure" + e.getMessage());
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.e(TAG, "onComplete: name");
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void signOut() {
        auth.signOut();
    }
}
