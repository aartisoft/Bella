package com.spots.bella.activity.make_post;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.spots.bella.activity.make_post.MakePostInteractor.OnSharePostResponseListener;

public class MakePostPresenterIMP implements MakePostPresenter, OnSharePostResponseListener {
    private static final String TAG = "MakePostPresenterIMP";
    private final StorageReference mStorage;
    private  Context context;

    private MakePostView mView;
    private final DatabaseReference database;
    private final FirebaseAuth mFirebaseAuth;
    private MakePostInteractor mInteractor;

    public MakePostPresenterIMP(MakePostView mView, DatabaseReference database, FirebaseAuth mFirebaseAuth, StorageReference mStorage) {
        this.mView = mView;
        this.database = database;
        this.mFirebaseAuth = mFirebaseAuth;
        this.mStorage = mStorage;
        this.context = (Context) mView;
        this.mInteractor = new MakePostInteractorIMP(context);

    }

    @Override
    public void onSharePostClicked(String text, String imageUri, int image_count) {
        if (mView != null) {
            mInteractor.sharePost(this, text, imageUri, image_count);
        }
    }

    @Override
    public void onSharePostClicked(String text) {
        if (mView!=null) {
            mInteractor.sharePost(this, text, null, 0);
        }
    }

    @Override
    public void onDestroy() {
        if (mView != null) {
            mView = null;
        }
    }

    @Override
    public void onShareFailure(String message, int code) {
        if (mView != null) {
            Log.d(TAG, "onShareFailure: ");
            mView.showMessage(message,code);
        }
    }

    @Override
    public void onShareFailure(String message, String error_msg) {
        if (mView != null) {
            Log.d(TAG, "onShareFailure: ");
            mView.showMessage(message,error_msg);
        }
    }

    @Override
    public void onShareSuccess(String message) {
        if (mView!=null) {
            mView.closeActivity();
        }
    }

}
