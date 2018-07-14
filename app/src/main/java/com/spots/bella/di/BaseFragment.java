package com.spots.bella.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

public class BaseFragment extends Fragment {
    @Inject
    public SharedPreferences prefs;
    @Inject
    public Context context;
    @Inject
    public String string;

    @Override
    public void onAttach(Context context) {
        ((DaggerApplication) context.getApplicationContext()).getAppComponent().inject(this);
        super.onAttach(context);
    }
}
