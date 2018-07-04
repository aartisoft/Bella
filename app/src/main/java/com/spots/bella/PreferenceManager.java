package com.spots.bella;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

/**
 * Created by Ehab Salah on 6/19/2018.
 */

public class PreferenceManager {
    Context context;
    SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        this.context = context;
        getSharedPrefrence();
    }

    void getSharedPrefrence(){
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_main_prefrence),Context.MODE_PRIVATE);
    }

    public void writeSharedPrefrenceBoolean(String key, boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public boolean readSharedPrefrenceBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public void clearPrefrences(){
        sharedPreferences.edit().clear().commit();
    }
}
