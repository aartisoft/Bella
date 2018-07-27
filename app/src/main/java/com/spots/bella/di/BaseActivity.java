package com.spots.bella.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.spots.bella.PreferenceManager;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {
    @Inject
    public Context context;

    @Inject
    public PreferenceManager pM;

    @Inject
    public Gson gson;

    @Inject
    public DatabaseReference database;
    @Inject
    public FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
    }
}
