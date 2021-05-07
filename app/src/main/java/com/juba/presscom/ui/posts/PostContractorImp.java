package com.juba.presscom.ui.posts;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.juba.presscom.utils.DataConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;

import static com.juba.presscom.utils.Constants.image_post_node;
import static com.juba.presscom.utils.Constants.post_node;
import static com.juba.presscom.utils.Constants.user_node;

public class PostContractorImp implements PostContractor {

    private static final String TAG = "PostContractorImp";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public PostContractorImp() {
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

    public String getDateForImage() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDate = sdf.format(date);
        return currentDate;
    }

    public String UserDateForImage() {
        return getUserId() + getDateForImage();
    }


    public String UserDate() {
        return getUserId() + getDate();
    }

    @Override
    public Completable postNewPost(String desc) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                HashMap<String, String> postMap = new HashMap<>();
                postMap.put("id", getUserId());
                postMap.put("desc", desc);
                DocumentReference reference = fireStore.collection(post_node).document(UserDate());
                reference.update("desc", desc).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: desc failure");
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
    public Completable postPostImage(Bitmap image) {

        return Completable.create(emitter -> {

            final StorageReference reference = storage.getReference().child(image_post_node).child(UserDateForImage() + ".jpg");
            final DocumentReference documentReference = fireStore.collection(post_node).document(UserDate());

            reference.putBytes(DataConverter.convertImage2ByteArray(image)).addOnFailureListener(e -> {
                Log.e(TAG, "onFailure:post data repo failure");
                emitter.onError(e);
            })
                    .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: post there is a failure one");
                            emitter.onError(e);
                        }
                    }).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "onComplete: problem");
                            emitter.onError(task.getException());

                        } else {
                            Log.e(TAG, "userDate in pic " + UserDate());

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


    @Override
    public Flowable<DocumentSnapshot> getPostInfo() {
        return Flowable.create(emitter -> {
            DocumentReference reference = fireStore.collection(post_node).document(UserDate());
            final ListenerRegistration registration = reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        emitter.onNext(documentSnapshot);
                    } else {
                        Log.e(TAG, "onEvent: getPostInfo error");
                    }
                }
            });

            emitter.setCancellable(() -> registration.remove());
        }, BackpressureStrategy.BUFFER);
    }
}
