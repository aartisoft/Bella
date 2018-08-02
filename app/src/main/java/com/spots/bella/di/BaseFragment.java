package com.spots.bella.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.spots.bella.PreferenceManager;
import com.spots.bella.constants.FirebaseMethods;

import javax.inject.Inject;

public class BaseFragment extends Fragment {

    @Inject
    public Context context;

    @Inject
    public PreferenceManager pM;

    @Inject
    public Gson mGson;

    @Inject
    public FirebaseAuth mAuth;

    @Inject
    public DatabaseReference mDatabase;

    @Inject
    public StorageReference mStorage;

    @Override
    public void onAttach(Context context) {
        ((DaggerApplication) context.getApplicationContext()).getAppComponent().inject(this);
        super.onAttach(context);
    }
}
