package com.spots.bella.activity.main_activity;

import android.os.Handler;

public class MInteractorIMP implements MInteractor {

    public MInteractorIMP() {
    }

    @Override
    public void fetchData(final OnDataResponseListener listener) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess("NOTE: MVP Working Correctly",1);
            }
        }, 3000);
    }
}
