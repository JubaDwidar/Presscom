package com.juba.presscom.ui.settings;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.juba.presscom.utils.DataConverter;

import androidx.annotation.NonNull;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;

import static com.juba.presscom.utils.Constants.image_node;
import static com.juba.presscom.utils.Constants.user_node;

public class SettingsContractorImp implements SettingsContractor {

    public static final String TAG = "SettingsContractorImp";
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public SettingsContractorImp() {
    }

    public String getUserId() {

        return auth.getCurrentUser().getUid();
    }


    @Override
    public Completable updateUserImage(Bitmap image){

        return Completable.create(emitter -> {

            final StorageReference reference = storage.getReference().child(image_node).child(getUserId() + ".jpg");
            final DocumentReference documentReference = fireStore.collection(user_node).document(getUserId());

            reference.putBytes(DataConverter.convertImage2ByteArray(image)).addOnFailureListener(e -> {
                Log.e(TAG, "onFailure: data repo failure");
                emitter.onError(e);
            })
                    .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: there is a failure one");
                            emitter.onError(e);
                        }
                    }).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "onComplete: problem");
                            emitter.onError(task.getException());

                        } else {
                            documentReference.update("image", task.getResult().toString())
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "onFailure: there is a image failure");
                                        emitter.onError(e);
                                    })
                                    .addOnSuccessListener(aVoid -> {
                                        Log.e(TAG, "onComplete:image Completed" + task.getResult().toString());
                                        emitter.onComplete();
                                    });

                        }
                    }));

        });


    }

    @Override
    public Completable updateUserInfo(String name, String status) {

        return Completable.create(emitter -> {
            DocumentReference reference = fireStore.collection(user_node).document(getUserId());
            reference.update("name", name).addOnFailureListener(e -> Log.e(TAG, "onFailure: update name " + e.getMessage())).addOnSuccessListener(aVoid -> {
                Log.e(TAG, "onSuccess: update name completed");
                reference.update("status", status).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure:  update status" + e.getMessage());
                    }
                }).addOnCompleteListener(task -> Log.e(TAG, "onComplete: status update completed"));
            });

        });
    }
    @Override
    public Flowable<DocumentSnapshot> getUserInfo() {
        return Flowable.create(emitter -> {
            final DocumentReference reference = fireStore.collection(user_node).document(getUserId());
            final ListenerRegistration registration = reference.addSnapshotListener((documentSnapshot, e) -> {

                if (documentSnapshot != null) {
                    emitter.onNext(documentSnapshot);
                }
            });

            emitter.setCancellable(() -> registration.remove());
        }, BackpressureStrategy.BUFFER);

    }
}

