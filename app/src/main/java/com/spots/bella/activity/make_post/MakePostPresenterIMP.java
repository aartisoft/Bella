package com.spots.bella.activity.make_post;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

class MakePostPresenterIMP implements MakePostPresenter implements OnSharePostListener {

    private  MakePostView mView;
    private final DatabaseReference database;
    private final FirebaseAuth mFirebaseAuth;
    private MakePostInteractor interactor;
    public MakePostPresenterIMP(MakePostView mView, DatabaseReference database, FirebaseAuth mFirebaseAuth) {
        this.mView = mView;
        this.database = database;
        this.mFirebaseAuth = mFirebaseAuth;
        this.interactor = new MakePostInteractorIMP(this);
    }

    @Override
    public void onSharePost() {
        if (mView!=null) {

        }
    }

    @Override
    public void onDestroy() {
        if (mView!=null) {
            mView=null;
        }
    }
}
