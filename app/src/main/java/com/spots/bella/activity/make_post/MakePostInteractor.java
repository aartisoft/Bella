package com.spots.bella.activity.make_post;

interface MakePostInteractor {
    interface OnSharePostListener{

        void onFailure(String message, int code);

        void onSuccess(String message, int code);
    }
    void fetchData(OnDataResponseListener ResponseListiner);

}
