package com.juba.presscom.ui.posts;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.juba.presscom.model.Post;
import com.juba.presscom.model.Users;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PostsPresenter {
    private PostContractor postContractor;
    private PostContractor.PostView postView;

    private static final String TAG = "PostsPresenter";

    private CompositeDisposable disposable = new CompositeDisposable();
    private MediatorLiveData<Users> userInfo = new MediatorLiveData();
    private MediatorLiveData<Post> postInfo = new MediatorLiveData();

    public PostsPresenter(PostContractor.PostView postView) {
        this.postView = postView;
        postContractor = new PostContractorImp();
        getPostInfo();
        getUserInfo();
    }

    public void postNewPost(String desc) {
        postContractor.postNewPost(desc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: has posted successfully");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    public void postPostImage(Bitmap image) {
        postContractor.postPostImage(image)
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


    private void getUserInfo() {
        postContractor.getUserInfo()
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

    public void getPostInfo() {
        postContractor.getPostInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<DocumentSnapshot>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null) {
                            Post post = documentSnapshot.toObject(Post.class);
                            postInfo.setValue(post);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public LiveData<Post> postInfo() {
        return postInfo;
    }


}
