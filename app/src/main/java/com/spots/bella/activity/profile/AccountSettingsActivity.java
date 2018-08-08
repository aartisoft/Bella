package com.spots.bella.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spots.bella.PreferenceManager;
import com.spots.bella.R;
import com.spots.bella.activity.login_activity.LoginActivity;
import com.spots.bella.constants.Common;
import com.spots.bella.constants.ImageManager;
import com.spots.bella.models.BaseUser;
import com.spots.bella.models.UserAccountSettings;
import com.spots.bella.models.UserSettings;
import com.spots.bella.utils.FilePaths;
import com.spots.bella.utils.SectionsStatePagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AccountSettingsActivity";

    @BindView(R.id.lvAccountSettings)
    ListView listView;
    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.profileToolBar)
    android.support.v7.widget.Toolbar toolbar;

    SectionsStatePagerAdapter pagerAdapter;

    @BindView(R.id.viewpager_container)
    ViewPager viewPager;

    @BindView(R.id.layout1)
    LinearLayout mLLinearLayout;

    Context mContext;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "onAuthStateChanged: 1");
                if (firebaseAuth.getCurrentUser() == null) {
                    Log.d(TAG, "onAuthStateChanged: 2");
                    new PreferenceManager(AccountSettingsActivity.this).clearPrefrences();
                    Intent intent = new Intent(AccountSettingsActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    AccountSettingsActivity.this.finish();
                }
            }
        };

        mContext = AccountSettingsActivity.this;
        Log.d(TAG, "onCreate: started");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        setupSettingList();
        setUpFragments();
        getIncomingIntent();
        //setup the backarrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to profile Activity");

            }
        });
        setUpFragments();


    }


    private void setViewPager(int fragmentNumber) {
        Log.d(TAG, "setViewPager: navigating to fragment  #: " + fragmentNumber);
        mLLinearLayout.setVisibility(View.GONE);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(fragmentNumber);
    }

    private void setUpFragments() {
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile)); // fragment 0
//        pagerAdapter.addFragment(new SignOutFragment(),getString(R.string.sign_out));// fragment 1
    }

    private void setupSettingList() {
        Log.d(TAG, "setupSettingList: initializing 'Account Settings.' list");
        final ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile));// fragment 0
        options.add(getString(R.string.sign_out));// fragment 1
        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == options.size() - 1) {
                    mAuth.signOut();
                } else {
                    setViewPager(position);
                }
            }
        });
    }


    private void getIncomingIntent() {
        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.selected_image))
                || intent.hasExtra(getString(R.string.selected_bitmap))) {

            //if there is an imageUrl attached as an extra, then it was chosen from the gallery/photo fragment
            Log.d(TAG, "getIncomingIntent: New incoming imgUrl");
            if (intent.getStringExtra(getString(R.string.return_to_fragment)).equals(getString(R.string.edit_profile))) {

                if (intent.hasExtra(getString(R.string.selected_image))) {
                    //set the new profile picture
                    uploadNewPhoto(intent.getStringExtra(getString(R.string.selected_image)), null);
                }
               /* else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                    //set the new profile picture
                    FirebaseMethods firebaseMethods = new FirebaseMethods(AccountSettingsActivity.this);
                    firebaseMethods.uploadNewPhoto(getString(R.string.profile_photo), null, 0,
                            null, (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap)));
                }*/
            }

        }

        if (intent.hasExtra(getString(R.string.calling_activity))) {
            Log.d(TAG, "getIncomingIntent: received incoming intent from " + getString(R.string.profile_activity));
            setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.edit_profile)));
        }
    }

    double mPhotoUploadProgress = 0;

    public void uploadNewPhoto(final String imgUrl,
                               Bitmap bm) {
        Log.d(TAG, "uploadNewPhoto: attempting to uplaod new photo.");

        FilePaths filePaths = new FilePaths();
        //case1) new photo
        Log.d(TAG, "uploadNewPhoto: uploading new PROFILE photo");


        ((AccountSettingsActivity) mContext).setViewPager(
                ((AccountSettingsActivity) mContext).pagerAdapter
                        .getFragmentNumber(mContext.getString(R.string.edit_profile))
        );

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/profile_photo");

        //convert image url to bitmap
        if (bm == null) {
            bm = ImageManager.getBitmap(imgUrl);
        }
        byte[] bytes = ImageManager.getBytesFromBitmap(bm, 50);

        UploadTask uploadTask = null;
        uploadTask = storageReference.putBytes(bytes);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri firebaseUrl = taskSnapshot.getDownloadUrl();

                Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();

                //insert into 'user_account_settings' node
                setProfilePhoto(firebaseUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Photo upload failed.");
                Toast.makeText(mContext, "Photo upload failed ", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                if (progress - 15 > mPhotoUploadProgress) {
                    Toast.makeText(mContext, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                    mPhotoUploadProgress = progress;
                }

                Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
            }
        });
    }

    private void setProfilePhoto(String url) {
        Log.d(TAG, "setProfilePhoto: setting new profile image: " + url);

        myRef.child(Common.USER_ACCOUNT_SETTINGS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Common.USER_PROFILE_PHOTO_STRING)
                .setValue(url);
    }


    /**
     * Setup the firebase auth object
     */

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Retrieves the account settings for teh user currently logged in
     * Database: user_acount_Settings node
     *
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase.");


        UserAccountSettings settings = new UserAccountSettings();
        BaseUser user = new BaseUser();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            // user_account_settings node
            if (ds.getKey().equals(Common.USER_ACCOUNT_SETTINGS)) {
                Log.d(TAG, "getUserAccountSettings: user account settings node datasnapshot: " + ds);

                try {

                    settings.setDisplay_name(
                            ds.child(mAuth.getUid())
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUser_name(
                            ds.child(mAuth.getUid())
                                    .getValue(UserAccountSettings.class).getUser_name()
                    );
                    settings.setWebsite(
                            ds.child(mAuth.getUid())
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );
                    settings.setDescription(
                            ds.child(mAuth.getUid())
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(mAuth.getUid())
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(mAuth.getUid())
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowing(
                            ds.child(mAuth.getUid())
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFollowers(
                            ds.child(mAuth.getUid())
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: " + settings.toString());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                }
            }


            // users node
            Log.d(TAG, "getUserSettings: snapshot key: " + ds.getKey());
            if (ds.getKey().equals(Common.USER_STRING)) {
                Log.d(TAG, "getUserAccountSettings: users node datasnapshot: " + ds);

                user.setUser_name(
                        ds.child(mAuth.getUid())
                                .getValue(BaseUser.class)
                                .getUser_name()
                );
                user.setEmail(
                        ds.child(mAuth.getUid())
                                .getValue(BaseUser.class)
                                .getEmail()
                );
                user.setPhone(
                        ds.child(mAuth.getUid())
                                .getValue(BaseUser.class)
                                .getPhone()
                );

                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
            }
        }
        return new UserSettings(user, settings);

    }

}
