package com.spots.bella.activity.make_post;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spots.bella.constants.Common;
import com.spots.bella.constants.ImageManager;
import com.spots.bella.constants.StringManipulation;
import com.spots.bella.models.Photo;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

class MakePostInteractorIMP implements MakePostInteractor {
    private static final String TAG = "MakePostInteractorIMP";
    private Context context;
    private String timeStamp;

    public MakePostInteractorIMP(Context context) {
        this.context = context;
    }

    @Override
    public void sharePost(final OnSharePostResponseListener responseListener, final String caption, String imageUrl, final int image_count) {
        Log.d(TAG, "sharePost: IMAGE_URI = " + imageUrl);
        Log.d(TAG, "sharePost: POST_TEXT = " + caption);
        Log.d(TAG, "sharePost: IMAGE_COUNT = " + image_count);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if (imageUrl != null) { // POST WITH IMAGE

            // TODO: upload photo
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child(Common.FIREBASE_IMAGE_STORAGE + "/" + uid + "/photo" + (image_count + 1));


            Bitmap bm = ImageManager.getBitmap(imageUrl);
            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 50);

            UploadTask uploadTask = null;
            uploadTask = storageReference.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: UPLOAD ");
                    String image_url = String.valueOf(taskSnapshot.getDownloadUrl());
//                        responseListener.onShareSuccess("Uploaded Success", 1);
                    // add the new photo to 'photo' node and 'user_photos' node
                    // navigate to the main feed so user can see their photo
                    addPhotoToDatabase(caption, image_url, responseListener);
                    responseListener.onShareSuccess("");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: UPLOAD");
                    responseListener.onShareFailure("Failed to post!", e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onProgress: Progress...");
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    if (progress - 15 > mPhotoUploadProgress) {
                        Log.d(TAG, "onProgress: = " + String.format("%.0f", progress));
                        mPhotoUploadProgress = progress;
                    }
                    Log.d(TAG, "onProgress: upload progress " + progress + " % done");
                }
            });


        } else // POST WITH TEXT
        {

        }
    }

    double mPhotoUploadProgress = 0;

    private void addPhotoToDatabase(String caption, String image_url, OnSharePostResponseListener responseListener) {
        Log.d(TAG, "addPhotoToDatabase: Adding photo to database.");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String new_photo_key = databaseReference.child(Common.STRING_PHOTOS).push().getKey();

        String tags = StringManipulation.getTags(caption);
        Photo photo = new Photo();
        photo.setCaption(caption);
        photo.setDate_created(getTimeStamp());
        photo.setImage_path(image_url);
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setTags(tags);
        photo.setPhoto_id(new_photo_key);

        // insert into database
        databaseReference.child(Common.USER_PHOTOS_STRING).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(new_photo_key).setValue(photo);
        databaseReference.child(Common.STRING_PHOTOS).child(new_photo_key).setValue(photo);
        Log.d(TAG, "addPhotoToDatabase: finish");

        responseListener.onShareSuccess("Image shared successfully");

    }

    public String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));

        return simpleDateFormat.format(new Date());
    }

}