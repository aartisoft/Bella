package com.spots.bella.activity.main_activity;

import android.content.Context;
import android.content.Intent;

import com.spots.bella.activity.post_activity.PostActivity;

public class MPresenterIMP implements MPresenter, MInteractor.OnDataResponseListener { //TODO: this class links the view and model ((interactor)the business logic )
    private MView mView;
    private MInteractor mInteractor;

    public MPresenterIMP(MView mView) {
        this.mView = mView;
        mInteractor = new MInteractorIMP();

    }


    @Override
    public void onCreate() {
        if (mView != null) {

        }
    }

    @Override
    public void onResume() {
        if (mView != null) {
            mView.showLoading();
            mInteractor.fetchData(this);
        }
    }

    @Override
    public void onDestroy() {
        if (mView != null) {
            mView = null;
        }
    }

    @Override
    public void onRefresh() {
        if (mView != null) {
            mInteractor.fetchData(this);
        }
    }

    @Override
    public void onItemClicked(Context context) {
        context.startActivity(new Intent(context,PostActivity.class));
    }

    //TODO: Handle the response
    @Override
    public void onFailure(String message, int code) {
        if (mView != null) {
            mView.showMessage(message, code);
        }
    }

    @Override
    public void onSuccess(String message, int code) {
        if (mView != null) {
            mView.hideLoading();
            mView.showRecyclerView();
            mView.showMessage(message, code);

        }
    }
}
