package com.spots.bella.constants;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseMethods {

    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private Context context;

    @Inject
    public FirebaseMethods(Context context) {
        this.context = context;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    private void setProfilePhoto(String url) {

    }
}
