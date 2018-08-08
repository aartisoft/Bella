package com.spots.bella.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.spots.bella.R;
import com.spots.bella.activity.Share.ShareActivity;
import com.spots.bella.constants.Common;
import com.spots.bella.di.BaseFragment;
import com.spots.bella.models.BaseUser;
import com.spots.bella.models.UserAccountSettings;
import com.spots.bella.models.UserSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends BaseFragment implements ConfirmPasswordDialog.OnConfirmPasswordListener {
    private static final String TAG = "EditProfileFragment";
    @BindView(R.id.backArrow)
    ImageView back_arrow;
    Unbinder unbinder;


    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    //EditProfile Fragment widgets
    @BindView(R.id.display_name)
    EditText mDisplayName;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.website)
    EditText mWebsite;
    @BindView(R.id.description)
    EditText mDescription;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.phoneNumber)
    EditText mPhoneNumber;
    @BindView(R.id.changeProfilePhoto)
    TextView mChangeProfilePhoto;
    @BindView(R.id.profile_photo)
    CircleImageView mProfilePhoto;
    @BindView(R.id.saveChanges)
    ImageView checkmark;


    //vars
    private UserSettings mUserSettings;
    ValueEventListener infoEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editprofile, container, false);
        Log.d(TAG, "onCreateView: ");
        unbinder = ButterKnife.bind(this, v);
        setupFirebaseAuth();
        // back arrow
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to profile activity.");
                getActivity().finish();
            }
        });
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
                getActivity().finish();
            }
        });

        return v;
    }


          /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();

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

        infoEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(getUserSettings(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(infoEventListener);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
        unbinder.unbind();
        myRef.removeEventListener(infoEventListener);
    }


    private void setProfileWidgets(UserSettings userSettings) {
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getUser().getEmail());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getUser().getPhone());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + mGson.toJson(userSettings));

        mUserSettings = userSettings;
        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();
        Log.d(TAG, "setProfileWidgets: mProfilePhoto = " + ((mProfilePhoto == null) ? "NULL" : "NOT NULL"));
        Log.d(TAG, "setProfileWidgets: mProfilePhoto = " + ((mDisplayName == null) ? "NULL" : "NOT NULL"));
        Glide.with(context).load(settings.getProfile_photo()).into(mProfilePhoto);
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUser_name());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(userSettings.getUser().getEmail());
        mPhoneNumber.setText(String.valueOf(userSettings.getUser().getPhone()));

        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: changing profile photo");
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //268435456
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
    }

    /**
     * Retrieves the data contained in the widgets and submits it to the database
     * Before donig so it chekcs to make sure the username chosen is unqiue
     */
    private void saveProfileSettings() {
        Log.d(TAG, "saveProfileSettings: ");
        final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final String phoneNumber = mPhoneNumber.getText().toString();


//        case1: if the user made a change to their username
        if (!mUserSettings.getUser().getUser_name().equals(username)) {
            checkIfUsernameExists(username);
        }
        //case2: if the user made a change to their email
        if (!mUserSettings.getUser().getEmail().equals(email)) {

            // step1) Reauthenticate
            //          -Confirm the password and email
            if (Common.isValidEmailForm(mEmail.getText().toString())) {
                ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));
                dialog.setTargetFragment(EditProfileFragment.this, 1);
            } else {
                Toast.makeText(context, "Invalid e-mail address!", Toast.LENGTH_SHORT).show();
            }


//             step2) check if the email already is registered
//                      -'fetchProvidersForEmail(String email)'
//             step3) change the email
//                      -submit the new email to the database and authentication
        }
//
        /**
         * change the rest of the settings that do not require uniqueness
         */
        if (!mUserSettings.getSettings().getDisplay_name().equals(displayName)) {
            //update displayname
            updateUserAccountSettings(displayName, null, null, null);
        }
        if (!mUserSettings.getSettings().getWebsite().equals(website)) {
            //update website
            updateUserAccountSettings(null, website, null, null);
        }
        if (!mUserSettings.getSettings().getDescription().equals(description)) {
            //update description
            updateUserAccountSettings(null, null, description, null);
        }
        if (!mUserSettings.getSettings().getProfile_photo().equals(phoneNumber)) {
            //update phoneNumber
            updateUserAccountSettings(null, null, null, phoneNumber);
        }
    }

    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(Common.USER_STRING)
                .orderByChild(Common.USER_NAME_FIELD_STRING)
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    //add the username
                    updateUsername(username);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();

                }
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(BaseUser.class).getUser_name());
                        Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * update username in the 'users' node and 'user_account_settings' node
     *
     * @param username
     */
    public void updateUsername(String username) {
        Log.d(TAG, "updateUsername: upadting username to: " + username);
        assert mAuth.getCurrentUser() != null;
        myRef.child(Common.USER_STRING)
                .child(mAuth.getCurrentUser().getUid())
                .child(Common.USER_NAME_FIELD_STRING)
                .setValue(username);

        myRef.child(Common.USER_ACCOUNT_SETTINGS)
                .child(userID)
                .child(Common.USER_NAME_FIELD_STRING)
                .setValue(username);
    }

    @Override
    public void onConfirmPassword(String password) {
        Log.d(TAG, "onConfirmPassword: got the password: " + password);

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password);

        ///////////////////// Prompt the user to re-provide their sign-in credentials
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User re-authenticated.");

                            ///////////////////////check to see if the email is not already present in the database
                            mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        try {
                                            if (task.getResult().getProviders().size() == 1) {
                                                Log.d(TAG, "onComplete: that email is already in use.");
                                                Toast.makeText(getActivity(), "That email is already in use", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d(TAG, "onComplete: That email is available.");

                                                //////////////////////the email is available so update it
                                                mAuth.getCurrentUser().updateEmail(mEmail.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User email address updated.");
                                                                    Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();
                                                                    updateEmail(mEmail.getText().toString());
                                                                }
                                                            }
                                                        });
                                            }
                                        } catch (NullPointerException e) {
                                            Log.e(TAG, "onComplete: NullPointerException: " + e.getMessage());
                                        }
                                    }
                                }
                            });


                        } else {
                            Log.d(TAG, "onComplete: re-authentication failed.");
                        }

                    }
                });
    }

    /**
     * update the email in the 'user's' node
     *
     * @param email
     */
    public void updateEmail(String email) {
        Log.d(TAG, "updateEmail: upadting email to: " + email);

        myRef.child(Common.USER_STRING)
                .child(userID)
                .child(Common.USER_STRING)
                .setValue(email);

    }


    /**
     * Update 'user_account_settings' node for the current user
     *
     * @param displayName
     * @param website
     * @param description
     * @param phoneNumber
     */
    public void updateUserAccountSettings(String displayName, String website, String description, String phoneNumber) {

        Log.d(TAG, "updateUserAccountSettings: updating user account settings.");

        if (displayName != null) {
            myRef.child(Common.USER_ACCOUNT_SETTINGS)
                    .child(userID)
                    .child(Common.USER_NAME_FIELD_STRING)
                    .setValue(displayName);
        }


        if (website != null) {
            myRef.child(Common.USER_ACCOUNT_SETTINGS)
                    .child(userID)
                    .child(Common.STRING_WEBSITE_FIELD)
                    .setValue(website);
        }

        if (description != null) {
            myRef.child(Common.USER_ACCOUNT_SETTINGS)
                    .child(userID)
                    .child(Common.STRING_USER_DESCRIPTION)
                    .setValue(description);
        }

        if (phoneNumber != null) { /*UPDATE PHONE NUMBER IF CHANGED IN THE USER DATA 'user'*/
            myRef.child(Common.USER_STRING)
                    .child(userID)
                    .child(Common.PHONE_STRING)
                    .setValue(phoneNumber);
        }
    }

}
