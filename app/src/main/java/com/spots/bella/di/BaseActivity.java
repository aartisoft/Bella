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
import com.spots.bella.R;
import com.spots.bella.activity.main_activity.MainActivity;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.spots.bella.constants.Common.setTranslucentStatusBar;

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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatusBar(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/clan_ot_book_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
    }
}
