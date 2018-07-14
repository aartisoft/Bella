package com.spots.bella.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.spots.bella.PreferenceManager;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {
    @Inject
    public SharedPreferences prefs;
    @Inject
    public Context context;
    @Inject
    public String string;

    @Inject
    public PreferenceManager pM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
    }
}
