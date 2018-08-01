package com.spots.bella.activity.make_post;

interface MakePostView {
    void showPregress();
    void hidePregress();
    void showImageView();
    void hideIamgeView();

    void showMessage(String message, int code);
    void showMessage(String message, String msg);
}
