package com.spots.bella;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Ehab Salah on 6/19/2018.
 */
@Singleton
public class PreferenceManager {
    public static final String USER_FIRST_NAME = "user_first_name";
    public static final String USER_LAST_NAME = "user_type";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_TYPE = "user_type";
    public static final String USER_CITY = "user_city";
    public static final String USER_MAX_ORDERS = "user_max_orders";
    private Context context;
    private SharedPreferences sharedPreferences;

    @Inject
    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_main_prefrence), Context.MODE_PRIVATE);
    }

    public void writeSharedPrefrenceBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean readSharedPrefrenceBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void clearPrefrences() {
        sharedPreferences.edit().clear().apply();
    }

    public void writeSharedPrefrenceString(String key, String value) {
         sharedPreferences.edit().putString(key, value).apply();
    }
    public String readeString(String key) {
        return sharedPreferences.getString(key,null);
    }
}
