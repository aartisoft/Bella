package com.spots.bella.di;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final DaggerApplication application;

    public AppModule(DaggerApplication app) {
        this.application = app;
    }

    @Provides
    @Singleton
    Context providesAppContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPrefrences(Context app) {
        return app.getSharedPreferences("My_Prefs_Tile", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    String providesName() {
        return "Bob";
    }
}
