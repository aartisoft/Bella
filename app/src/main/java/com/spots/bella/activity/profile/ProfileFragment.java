package com.spots.bella.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spots.bella.R;
import com.spots.bella.constants.Common;
import com.spots.bella.di.BaseFragment;
import com.spots.bella.models.BaseUser;
import com.spots.bella.models.UserAccountSettings;
import com.spots.bella.models.UserSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BaseFragment {

    private static final String TAG = "ProfileFragment";

    private static final int NUM_GRID_COLUMNS = 3;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    //widgets
    @BindView(R.id.tvPosts)
    TextView mPosts;
    @BindView(R.id.tvFollowers)
    TextView mFollowers;
    @BindView(R.id.tvFollowing)
    TextView mFollowing;
    @BindView(R.id.display_name)
    TextView mDisplayName;
    @BindView(R.id.username)
    TextView mUserName;
    @BindView(R.id.website)
    TextView mWebsite;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.textEditProfile)
    TextView editProfile;

    @BindView(R.id.profileProgressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.profile_photo)
    CircleImageView mProfilePhoto;
    @BindView(R.id.profileGride)
    GridView grideView;
    @BindView(R.id.profileToolBar)
    Toolbar toolbar;
    @BindView(R.id.profileMenu)
    ImageView profileMenu;

    //vars
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, v);
        Log.d(TAG, "onCreateView: started.");

        setupToolbar();

        setupFirebaseAuth();
//        setupGridView();
//
//        getFollowersCount();
//        getFollowingCount();
//        getPostsCount();
//
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to " + context.getString(R.string.edit_profile));
                Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        return v;
    }

    private void setupToolbar() {
        ((ProfileActivity) getActivity()).setSupportActionBar(toolbar);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Navigating to account settings.");
                Intent intent = new Intent(context, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }


    private void setProfileWidgets(UserSettings userSettings) {
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getSettings().getUsername());


        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();
        assert settings != null;
        Glide.with(context).load(((settings.getProfile_photo() == null ? R.drawable.ic_profile : settings.getProfile_photo()))).into(mProfilePhoto);

        mDisplayName.setText(settings.getDisplay_name());
        mUserName.setText(settings.getUser_name());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(getUserSettings(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
