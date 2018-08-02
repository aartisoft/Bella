package com.spots.bella.activity.make_post;

import android.content.Context;
import android.net.Uri;

interface MakePostInteractor {
    interface OnSharePostResponseListener {

        void onShareFailure(String message, int code);
        void onShareFailure(String message, String error_msg);

        void onShareSuccess(String message);
    }

    void sharePost( OnSharePostResponseListener responseListener, String text, Uri imageUri, int image_count);

}
