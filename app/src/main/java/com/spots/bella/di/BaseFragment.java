package com.spots.bella.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.spots.bella.PreferenceManager;

import javax.inject.Inject;

public class BaseFragment extends Fragment {

    @Inject
    public Context context;

    @Inject
    public PreferenceManager pM;

    @Inject
    public Gson gson;


    @Inject
    public FirebaseAuth mFirebaseAuth;

    @Inject
    public DatabaseReference database;

    @Override
    public void onAttach(Context context) {
        ((DaggerApplication) context.getApplicationContext()).getAppComponent().inject(this);
        super.onAttach(context);
    }
}
