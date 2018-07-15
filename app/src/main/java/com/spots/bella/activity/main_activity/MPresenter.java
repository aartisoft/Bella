package com.spots.bella.activity.main_activity;

import android.content.Context;

public interface MPresenter {
    void onCreate();
    void onResume();
    void onDestroy();

    void onRefresh();

    void onItemClicked(Context context);
}
