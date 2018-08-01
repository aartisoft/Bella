package com.spots.bella.activity.make_post;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.spots.bella.activity.make_post.MakePostInteractor.OnSharePostResponseListener;

public class MakePostPresenterIMP implements MakePostPresenter, OnSharePostResponseListener {
    private static final String TAG = "MakePostPresenterIMP";
    private final StorageReference mStorage;

    private MakePostView mView;
    private final DatabaseReference database;
    private final FirebaseAuth mFirebaseAuth;
    private MakePostInteractor mInteractor;

    public MakePostPresenterIMP(MakePostView mView, DatabaseReference database, FirebaseAuth mFirebaseAuth, StorageReference mStorage) {
        this.mView = mView;
        this.database = database;
        this.mFirebaseAuth = mFirebaseAuth;
        this.mInteractor = new MakePostInteractorIMP();
        this.mStorage = mStorage;
    }

    @Override
    public void onSharePostClicked(String text, Uri imageUri) {
        if (mView != null) {
            mInteractor.sharePost(this,mFirebaseAuth,database,mStorage,text, imageUri);
        }
    }

    @Override
    public void onSharePostClicked(String text) {
        if (mView!=null) {
            mInteractor.sharePost(this,mFirebaseAuth,database,mStorage, text, null);
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
    public void onShareSuccess(String message, int code) {
        if (mView != null) {
            Log.d(TAG, "onShareSuccess: ");
        }
    }
}
