package com.spots.bella.activity.login_activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.spots.bella.R;
import com.spots.bella.activity.WizardActivity;
import com.spots.bella.activity.login_activity.fragments.AccountValidationFragment;
import com.spots.bella.activity.login_activity.fragments.ArtistQuestionsFragment;
import com.spots.bella.activity.login_activity.fragments.AccountValidationFragment.OnEmailVerificationFragmentInteractionListener;
import com.spots.bella.activity.login_activity.fragments.DetectRegisterationFragment;
import com.spots.bella.activity.login_activity.fragments.ForgotPasswordFragment;
import com.spots.bella.activity.login_activity.fragments.ForgotPasswordFragment.OnForgotPasswordFragmentInteractionListener;
import com.spots.bella.activity.login_activity.fragments.LoginFragment;
import com.spots.bella.activity.login_activity.fragments.RegisterationFragment;
import com.spots.bella.activity.login_activity.fragments.ResetPasswordFragment.OnResetPasswordFragmentInteractionListener;
import com.spots.bella.constants.Common;
import com.spots.bella.constants.StringManipulation;
import com.spots.bella.constants.Utils;
import com.spots.bella.di.BaseActivity;
import com.spots.bella.models.ArtistUser;
import com.spots.bella.models.BaseUser;
import com.spots.bella.models.MoreDetailsUserArtist;
import com.spots.bella.models.NormalUser;
import com.spots.bella.models.UserAccountSettings;

import static com.spots.bella.activity.login_activity.fragments.ArtistQuestionsFragment.*;
import static com.spots.bella.activity.login_activity.fragments.DetectRegisterationFragment.*;
import static com.spots.bella.activity.login_activity.fragments.LoginFragment.*;
import static com.spots.bella.activity.login_activity.fragments.RegisterationFragment.*;
import static com.spots.bella.constants.Common.ARTIST_STRING;
import static com.spots.bella.constants.Common.NORMAL_USER;
import static com.spots.bella.constants.Common.NORMAL_USER_STRING;
import static com.spots.bella.constants.Common.darkStatusBarSetup;
import static com.spots.bella.constants.Common.hideDialog;
import static com.spots.bella.constants.Common.showShortMessage;

public class LoginActivity extends BaseActivity implements
        OnLoginFragmentInteractionListener,
        OnDetectRegisterationFragmentInteractionListener,
        OnArtistQuestionsFragmentInteractionListener,
        OnForgotPasswordFragmentInteractionListener,
        OnEmailVerificationFragmentInteractionListener,
        OnResetPasswordFragmentInteractionListener,
        OnRegisterFragmentInteractionListener {

    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    private boolean isVerficationSent;
    DatabaseReference users;
    private boolean isSignedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        darkStatusBarSetup(getWindow());
        setContentView(R.layout.activity_login);
        initFirebaseAuth();
        isSignedIn = false;
    }

    private void initFirebaseAuth() {
        users = mDatabase.child(Common.USER_STRING);
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) { // USER SIGNED IN // show login
                    Log.d(TAG, "onAuthStateChanged: 1 User signed in.");
                    BaseUser user_logged_in = Common.getUserData(pM); // check saved user data
                    if (user_logged_in == null) { // USER SIGNED IN BUT APP'S DATA CLEARED // problem in saved data
                        Log.d(TAG, "onAuthStateChanged: 2");
                        Log.d(TAG, "onAuthStateChanged: USER LOGGED IN DATA NOT AVAILABLE");
                        showLoginFragment();
                    } else {// USER SIGNED IN && AND HIS DATA EXISTS
                        Log.d(TAG, "onAuthStateChanged: 3");
                        Log.d(TAG, "onAuthStateChanged: USER LOGGED IN ");

                        // TODO: check mail is valid!
                        if (user.isEmailVerified()) {   // show home
                            Log.d(TAG, "onAuthStateChanged: 3.1 EMAIL IS VERFIED");
                            if (!isSignedIn) {
                                startActivity(new Intent(context, WizardActivity.class));
                                finish();
                            }
                        } else {
                            Log.d(TAG, "onAuthStateChanged: 3.2 USER EMAIL NOT VERIFIED");
                            if (isVerficationSent) {
                                showLoginFragment();
                            } else {
                                showEmailVerificationFragment();
                            }
                        }

                    }
                } else // USER NOT SIGNED IN
                {
                    Log.d(TAG, "onAuthStateChanged: 4 user signed out.");
                    showLoginFragment();
                }
            }
        };
    }


    private void sendEmailVerfication() {
        Log.d(TAG, "sendEmailVerfication: ");
        mAuth.getCurrentUser()
                .sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onShareSuccess: ");
                mAuth.signOut();
                isVerficationSent = true;
                hideDialog(dialog);
                showLoginFragment();
                showShortMessage("Verification E-Mail sent", findViewById(android.R.id.content));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideDialog(dialog);
                Log.d(TAG, "onShareFailure: " + e.getMessage());
                showShortMessage("An error occurred!", findViewById(android.R.id.content));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSignedIn = false;
    }

    private void showLoginFragment() {
        Log.d(TAG, "showLoginFragment: ");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_login_activity_container, new LoginFragment(), getResources().getString(R.string.login_fragment_tag));
        fragmentTransaction.commit();
    }

    @Override
    public void onLoginFragmentOpened() {
        Log.d(TAG, "onLoginFragmentOpened: ");
    }

    private static final String TAG = "LoginActivity";

    @Override
    public void onRegisterBtnClicked() {
        Log.d(TAG, "onRegisterBtnClicked: ");
        showDetectRegisterationFragment();
    }

    @Override
    public void onLoginBtnClicked(final String email, final String password) {

        Log.d(TAG, "onLoginBtnClicked: ");
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(final AuthResult authResult) {
                                Log.d(TAG, "onShareSuccess: LOGIN");
                                isSignedIn = true;
                                users.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ArtistUser artistUser = null;
                                        NormalUser normalUser = null;

                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            if (child.getKey().equals(authResult.getUser().getUid())) {
                                                if (child.getValue(BaseUser.class).getType().equals(Common.ARTIST_STRING)) {
                                                    artistUser = child.getValue(ArtistUser.class);
                                                } else {
                                                    normalUser = child.getValue(NormalUser.class);
                                                }
                                            }
                                        }

                                        if (artistUser != null) {
                                            Log.d(TAG, "onDataChange: USER IS ARTIST");
                                            Common.saveUserData(pM, artistUser.getFull_name(), artistUser.getEmail(), artistUser.getPassword(), String.valueOf(artistUser.getPhone()), artistUser.getType(), artistUser.getMore_details());
                                            hideDialog(dialog);
                                            // TODO: Check email validation
                                            if (authResult.getUser().isEmailVerified()) {
                                                startActivity(new Intent(LoginActivity.this, WizardActivity.class));
                                                finish();
                                            } else {
//                                                mAuth.signOut();
                                                showEmailVerificationFragment();
                                            }

                                        } else {
                                            Log.d(TAG, "onDataChange: USER NOT ARTIST");
                                            if (normalUser != null) {
                                                Log.d(TAG, "onDataChange: USER IS NORMAL USER");
                                                Common.saveUserData(pM, normalUser.getFull_name(), normalUser.getEmail(), normalUser.getPassword(), String.valueOf(normalUser.getPhone()), normalUser.getType(), null);
                                                hideDialog(dialog);
                                                // if user use validated email - or not lw not hwdii 3latool 3la fragment checkValidation
                                                // TODO : check email validation
                                                if (authResult.getUser().isEmailVerified()) { // verified go home
                                                    Log.d(TAG, "onDataChange: SIGNED IN USER EMAIL IS VERIFIED!");
                                                    startActivity(new Intent(LoginActivity.this, WizardActivity.class));
                                                    finish();
                                                } else // not verified show verification fragment
                                                {
                                                    Log.d(TAG, "onDataChange: SIGNED IN USER EMAIL IS NOT VERIFIED");
                                                    showEmailVerificationFragment();
                                                }
                                            } else {
                                                Log.d(TAG, "onDataChange: USER IS NOT NORMAL USER & NOT EXIST !");
                                                hideDialog(dialog);
                                                showShortMessage("This User No Longer Exist!", findViewById(android.R.id.content));
                                            }
                                        }
                                        isSignedIn = false;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                                        hideDialog(dialog);
                                        isSignedIn = false;
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                isSignedIn = false;
                                Log.d(TAG, "onShareFailure: LOGIN: msg = " + e.getMessage());
                                hideDialog(dialog);
                                if (e.getMessage().contains("network error")) {   // EMAIL ALREADY USED!
                                    showShortMessage("Check network connection!", findViewById(android.R.id.content));
                                } else if (e.getMessage().contains("There is no user record corresponding to this identifier")) {   // No Email like that
                                    showShortMessage("Wrong email or password!", findViewById(android.R.id.content));
                                } else if (e.getMessage().contains("The password is invalid or the user does not have a password")) {   // Wrong password
                                    showShortMessage("Wrong password!", findViewById(android.R.id.content));
                                } else
                                    showShortMessage("Unknown error, try again!", findViewById(android.R.id.content));
                            }
                        });
            }
        }, Common.LOADING_DURATION);
    }

    @Override
    public void onForgotPasswordBtnClicked() {
        Log.d(TAG, "onForgotPasswordBtnClicked: ");
        showForgotPasswordFragment();
    }

    private void showForgotPasswordFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_login_activity_container, new ForgotPasswordFragment(), getResources().getString(R.string.forgot_password_fragment_tag));
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void showDetectRegisterationFragment() {
        Log.d(TAG, "showDetectRegisterationFragment: ");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_login_activity_container, new DetectRegisterationFragment(), getResources().getString(R.string.detect_registeration_fragment));
        fragmentTransaction.addToBackStack(null).commit();
    }

    @Override
    public void onDetectRegisterationFragmentOpened() {
        Log.d(TAG, "onDetectRegisterationFragmentOpened: ");
    }

    @Override
    public void onRegisterTypeBtnClicked(int type) {
        Log.d(TAG, "onRegisterTypeBtnClicked: ");
        showRegisteration(type);
    }

    private void showRegisteration(int type) {
        RegisterationFragment fragment = new RegisterationFragment();
        Bundle b = new Bundle();
        b.putInt("type", type);
        fragment.setArguments(b);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_login_activity_container, fragment, getResources().getString(R.string.user_registeration_fragment_tag));
        fragmentTransaction.addToBackStack(null).commit();
    }

    @Override
    public void onRegisterFragmentOpened() {
        Log.d(TAG, "onRegisterFragmentOpened: ");
    }

    @Override
    public void onRegisterNextBtnClicked(final String full_name, final String email, final String phone, final String password, final int TYPE) {
        Log.d(TAG, "onRegisterNextBtnClicked: ");

        showDialog();

        final String type_u;

        if (TYPE == NORMAL_USER)  // USER REGISTER
        {
            type_u = NORMAL_USER_STRING;
        } else // ARTIST REGISTER
        {
            type_u = ARTIST_STRING;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(final AuthResult authResult) {
                                // save user to firebase db

                                users.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        final String username = StringManipulation.condenseUsername(full_name);
                                        final BaseUser user = new BaseUser();
                                        user.setFull_name(full_name);
                                        user.setPhone(phone);
                                        user.setEmail(email);
                                        user.setPassword(password);
                                        user.setType(type_u);
                                        user.setUser_name(username);

                                        checkIfUsernameExists(user);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d(TAG, "onCancelled: ");
                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideDialog(dialog);
                        showShortMessage("Registeration Failed - log it", findViewById(android.R.id.content));
                        Log.d(TAG, "onShareFailure: Registration : 1.3" + e.getMessage());
                        String error_msg = e.getMessage();
                        if (error_msg.contains("email address is already in use by another account")) {   // EMAIL ALREADY USED!
                            showShortMessage("email is already used", findViewById(android.R.id.content));
                        } else if (error_msg.contains("network error")) {   // EMAIL ALREADY USED!
                            showShortMessage("Check network connection!", findViewById(android.R.id.content));
                        } else {
                            showShortMessage("Check log for error", findViewById(android.R.id.content));
                        }
                    }
                });
            }
        }, Common.LOADING_DURATION);
    }

    /**
     * Check is @param username already exists in teh database
     *
     * @param user
     */
    private void checkIfUsernameExists(final BaseUser user) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + user.getUser_name() + " already exists.");

        Query query = mDatabase
                .child(Common.USER_STRING)
                .orderByChild(Common.USER_NAME_FIELD_STRING)
                .equalTo(user.getUser_name());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assert mAuth.getCurrentUser() != null;
                String append = "";
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(BaseUser.class).getUser_name());
                        append = users.push().getKey().substring(3, 10);
                        Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
                    }
                }

                String mUsername = "";
                mUsername = user.getUser_name() + append;

                //add new user to the database
                user.setUser_name(mUsername);

                //TODO: // use uid as key of childs   // add new user to database
                users.child(mAuth.getCurrentUser().getUid()).setValue(user);

                // add new user account settings to the database
                UserAccountSettings settings = new UserAccountSettings();
                settings.setDescription("Hey, i am new to Bella.");
                settings.setUser_name(user.getUser_name());
                settings.setFollowers(0);
                settings.setFollowing(0);
                settings.setPosts(0);
                settings.setDisplay_name(user.getUser_name());
                settings.setWebsite("");
                settings.setId(mAuth.getUid());
                settings.setProfile_photo("");

                mDatabase.child(Common.USER_ACCOUNT_SETTINGS).child(mAuth.getCurrentUser().getUid()).setValue(settings); // add user settings
                mAuth.signOut();

                if (TextUtils.equals(user.getType(), NORMAL_USER_STRING)) { // normal user register done
                    while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    sendEmailVerfication();
                    showLoginFragment();
                } else { // artist user register done
                    showArtistQuestionsFragment(mAuth.getUid(), user);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showArtistQuestionsFragment(String user_id, BaseUser user) {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        Bundle b = new Bundle();
        b.putString("uid", user_id);
        b.putString("user_first_name", user.getFull_name());
        b.putString("user_phone", user.getPhone());
        b.putString("user_email", user.getEmail());
        Fragment f = new ArtistQuestionsFragment();
        f.setArguments(b);
//        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_login_activity_container, f, getResources().getString(R.string.user_registeration_fragment_tag));
        fragmentTransaction.commit();

    }

    @Override
    public void onArtistQuestionsFragmentOpened() {
        Log.d(TAG, "onArtistQuestionsFragmentOpened: ");
        // DON'T FORGET HIDE THE SOFT KEYBOARD #
    }

    @Override
    public void onArtistQuestionsFragmentBtnClickFinish(final String UID, final int i, final String city) {
        Log.d(TAG, "onArtistQuestionsFragmentBtnClickFinish: ");
        if (Utils.isNetworkAvailable(context)) {
            showDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MoreDetailsUserArtist artist_details = new MoreDetailsUserArtist(city, i + "");
                    users.child(UID).child("more_details").setValue(artist_details)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Common.hideDialog(dialog);
                                    showShortMessage("Registration finished, login now", findViewById(android.R.id.content));
                                    Common.clearBackStackFragments(getSupportFragmentManager());
                                    sendEmailVerfication();
                                    showLoginFragment();
                                    Log.d(TAG, "onShareSuccess: added to db");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Common.hideDialog(dialog);
                                    Log.d(TAG, "onShareFailure: failed to add data to db EXCEPTION msg = " + e.getMessage());
                                    if (e.getMessage().contains("network error")) {   // EMAIL ALREADY USED!
                                        showShortMessage("Check network connection!", findViewById(android.R.id.content));
                                    } else {
                                        showShortMessage("Check log for error", findViewById(android.R.id.content));
                                    }
                                }
                            });
                }
            }, Common.LOADING_DURATION);
        } else {
            showShortMessage("No internet connection!", findViewById(android.R.id.content));
        }
    }

    AlertDialog dialog;

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
        View sign_in_layout = inflater.inflate(R.layout.sign_in_layout, null);
        ProgressBar progressBar = sign_in_layout.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        doubleBounce.setBounds(0, 0, 100, 100);
        doubleBounce.setColor(Color.WHITE);
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.setView(sign_in_layout);
        dialog.setCancelable(false);

        dialog.show();
    }

    @Override
    public void onForgotPasswordFragmentOpened() {
        Log.d(TAG, "onForgotPasswordFragmentOpened: ");
    }

    @Override
    public void onForgotPasswordAlreadyHaveAccountClicked() {
        Log.d(TAG, "onForgotPasswordAlreadyHaveAccountClicked: ");
        onBackPressed();
    }

    @Override
    public void onForgotPasswordNextClicked(final String email) {
        Log.d(TAG, "onForgotPasswordNextClicked: ");
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendResetPasswordEmail(email);
            }
        }, Common.LOADING_DURATION);
    }

    private void showEmailVerificationFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_login_activity_container, new AccountValidationFragment(), getResources().getString(R.string.email_verficarion_fragment_tag));
        fragmentTransaction.commit();
    }

    @Override
    public void onEmailVerificationFragmentOpened() {
        Log.d(TAG, "onEmailVerificationFragmentOpened: ");
    }

    @Override
    public void onVerificationClicked() {
        Log.d(TAG, "onVerificationClicked: ");
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendEmailVerfication();
            }
        }, Common.LOADING_DURATION);
    }

    @Override
    public void onLoginBtnClicked() {
        Log.d(TAG, "onLoginBtnClicked: ");
        showLoginFragment();
    }


    private void sendResetPasswordEmail(final String email) {
        Log.d(TAG, "sendResetPasswordEmail: ");
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onShareSuccess: ");
                showLoginFragment();
                hideDialog(dialog);
                showShortMessage("Reset E-Mail sent", findViewById(android.R.id.content));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideDialog(dialog);
                Log.d(TAG, "onShareFailure: " + e.getMessage());
                Toast.makeText(context, "Error occured " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//                showEmailVerificationFragment(email);
    }

    @Override
    public void onResetPasswordFragmentOpened() {
        Log.d(TAG, "onResetPasswordFragmentOpened: ");
    }

    @Override
    public void onSaveNewPasswordClicked() {
        Log.d(TAG, "onSaveNewPasswordClicked: ");
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.cancel();
                }
                Toast.makeText(LoginActivity.this, "Password Changes Successfully", Toast.LENGTH_SHORT).show();
                Common.clearBackStackFragments(getSupportFragmentManager());
                showLoginFragment();
            }
        }, Common.LOADING_DURATION);
    }
}
