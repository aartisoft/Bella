package com.spots.bella.activity.make_post;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spots.bella.constants.Common;
import com.spots.bella.models.Photo;

class MakePostInteractorIMP implements MakePostInteractor {
    private static final String TAG = "MakePostInteractorIMP";
    private String timeStamp;

    public MakePostInteractorIMP() {
    }

    @Override
    public void sharePost(final OnSharePostResponseListener responseListener, final String caption, Uri imageUri) {
        Log.d(TAG, "sharePost: IMAGE_URI = " + imageUri);
        Log.d(TAG, "sharePost: POST_TEXT = " + caption);
        if (imageUri != null) { // POST WITH IMAGE

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference post_images_file_path = storageReference.child(Common.STRING_PHOTOS).child(imageUri.getLastPathSegment() + System.currentTimeMillis() + ".jpg");
            post_images_file_path.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Uri image_url = task.getResult().getDownloadUrl();
//                        responseListener.onShareSuccess("Uploaded Success", 1);
                        // add the new photo to 'photo' node and 'user_photos' node
                        // navigate to the main feed so user can see their photo
                        addPhotoToDatabase(caption, image_url);
                    } else {
                        responseListener.onShareFailure("Uploaded Fsiled!", task.getException().getMessage().toString());
                        Log.d(TAG, "onComplete: ERROR: " + task.getException().getMessage());
                    }
                }
            });
        } else // POST WITH TEXT
        {

        }
    }

    private void addPhotoToDatabase(String caption, String image_url) {
        Log.d(TAG, "addPhotoToDatabase: Adding photo to database.");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String new_photo_key = databaseReference.child(Common.STRING_PHOTOS).push().getKey();
        Photo photo = new Photo();
        photo.setCaption(caption);
        photo.setDate_created(getTimeStamp());
        photo.setImage_path(image_url);
    }

    public String getTimeStamp() {

        return timeStamp;
    }
}