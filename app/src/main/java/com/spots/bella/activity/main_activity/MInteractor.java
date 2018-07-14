package com.spots.bella.activity.main_activity;

public interface MInteractor {
    interface OnDataResponseListener {
        void onFailure(String message, int code);

        void onSuccess(String message, int code);
    }

    void fetchData(OnDataResponseListener ResponseListiner);

}
