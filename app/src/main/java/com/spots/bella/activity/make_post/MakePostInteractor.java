package com.spots.bella.activity.make_post;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

interface MakePostInteractor {
    interface OnSharePostResponseListener {

        void onShareFailure(String message, int code);
        void onShareFailure(String message, String error_msg);

        void onShareSuccess(String message, int code);
    }

    void sharePost(OnSharePostResponseListener responseListener, String text, Uri imageUri);

}
