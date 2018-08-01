package com.spots.bella.activity.make_post;

import android.net.Uri;

interface MakePostPresenter {
    void onSharePostClicked(String text, Uri imageUri);

    void onSharePostClicked(String text);

    void onDestroy();

}
