package com.spots.bella.activity.make_post;

import android.content.Context;
import android.net.Uri;

interface MakePostPresenter {
    void onSharePostClicked(String text);

    void onSharePostClicked(String text, Uri imageUri, int image_count);

    void onDestroy();

}
