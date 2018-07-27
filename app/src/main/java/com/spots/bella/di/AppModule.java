package com.spots.bella.di;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

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
    String providesName() {
        return "Bob";
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    DatabaseReference providesRootRefrence() {
        return FirebaseDatabase.getInstance().getReference();
    }
    @Provides
    @Singleton
    FirebaseAuth providesFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
