package com.spots.bella.activity.main_activity;

public interface MView {
    void showMessage(String message, int code);

    void showLoading();

    void hideLoading();

    void showRecyclerView();
}
