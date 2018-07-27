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
import com.spots.bella.activity.login_activity.fragments.ResetPasswordFragment;
import com.spots.bella.activity.login_activity.fragments.ResetPasswordFragment.OnResetPasswordFragmentInteractionListener;
import com.spots.bella.constants.Common;
import com.spots.bella.constants.Utils;
import com.spots.bella.di.BaseActivity;
import com.spots.bella.models.ArtistUser;
import com.spots.bella.models.BaseUser;
import com.spots.bella.models.MoreDetailsUserArtist;
import com.spots.bella.models.NormalUser;

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
    private DatabaseReference normal_users_ref;
    private DatabaseReference artist_users_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        darkStatusBarSetup(getWindow());
        setContentView(R.layout.activity_login);
        initFirebase();
    }
    private void initFirebase() {
        DatabaseReference users = database.child(Common.USER_STRING);
        artist_users_ref = users.child(ARTIST_STRING);
        normal_users_ref = users.child(NORMAL_USER_STRING);
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null) { // USER SIGNED IN // show login
                    Log.d(TAG, "onAuthStateChanged: 1");
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
                            startActivity(new Intent(context, WizardActivity.class));
                            finish();
                        }
                        else
                        {
                            Log.d(TAG, "onAuthStateChanged: 3.2 USER EMAIL NOT VERIFIED");
                            showEmailVerificationFragment();
                        }

                    }
                }
                else // USER NOT SIGNED IN
                {
                    Log.d(TAG, "onAuthStateChanged: 4");
                    showLoginFragment();
                }
            }
        };
    }


    private void sendEmailVerfication() {
        Log.d(TAG, "sendEmailVerfication: ");
        mFirebaseAuth.getCurrentUser()
                .sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: ");
                hideDialog(dialog);
                showLoginFragment();
                showShortMessage("Verification E-Mail sent",findViewById(android.R.id.content));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideDialog(dialog);
                Log.d(TAG, "onFailure: "+e.getMessage());
                showShortMessage("An error occurred!",findViewById(android.R.id.content));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
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

                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(final AuthResult authResult) {
                                Log.d(TAG, "onSuccess: LOGIN");
                                artist_users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ArtistUser artistUser = null;
                                        for (DataSnapshot child:dataSnapshot.getChildren())
                                        {
                                            if (child.getKey().equals(authResult.getUser().getUid())) {
                                                artistUser = child.getValue(ArtistUser.class);
                                            }
                                        }
                                        if (artistUser!=null) {
                                            Log.d(TAG, "onDataChange: USER IS ARTIST");
                                            Common.saveUserData(pM,artistUser.getFrist_name(),artistUser.getLast_name(),artistUser.getEmail(),artistUser.getPassword(),artistUser.getPhone(),artistUser.getType(),artistUser.getMore_details());
                                            hideDialog(dialog);
                                            // TODO: Check email validation
                                            if (authResult.getUser().isEmailVerified()) {
                                                startActivity(new Intent(LoginActivity.this, WizardActivity.class));
                                                finish();
                                            }
                                            else
                                            {
                                                showEmailVerificationFragment();
                                            }

                                        }
                                        else
                                        {
                                            Log.d(TAG, "onDataChange: USER NOT ARTIST");
                                            normal_users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    NormalUser normalUser = null;
                                                    for (DataSnapshot child: dataSnapshot.getChildren())
                                                    {
                                                        if (child.getKey().equals(authResult.getUser().getUid())) {
                                                            normalUser=child.getValue(NormalUser.class);
                                                        }
                                                    }
                                                    if (normalUser!=null) {
                                                        Log.d(TAG, "onDataChange: USER IS NORMAL USER");
                                                        Common.saveUserData(pM,normalUser.getFrist_name(),normalUser.getLast_name(),normalUser.getEmail(),normalUser.getPassword(),normalUser.getPhone(),normalUser.getType(),null);
                                                        hideDialog(dialog);
                                                        // if user use validated email - or not lw not hwdii 3latool 3la fragment checkValidation
                                                        // TODO : check email validation
                                                        if (authResult.getUser().isEmailVerified()) { // verified go home
                                                            Log.d(TAG, "onDataChange: SIGNED IN USER EMAIL IS NOT VERIFIED!");
                                                            startActivity(new Intent(LoginActivity.this, WizardActivity.class));
                                                            finish();
                                                        }
                                                        else // not verified show verification fragment
                                                        {
                                                            Log.d(TAG, "onDataChange: SIGNED IN USER EMAIL IS VERIFIED");
                                                            showEmailVerificationFragment();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Log.d(TAG, "onDataChange: USER IS NOT NORMAL USER & NOT EXIST !");
                                                        hideDialog(dialog);
                                                        showShortMessage("This User No Longer Exist!",findViewById(android.R.id.content));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                                                    hideDialog(dialog);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                                        hideDialog(dialog);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: LOGIN: msg = " + e.getMessage());
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
    public void onRegisterNextBtnClicked(final String first_name, final String last_name, final String email, final String phone, final String password, final int TYPE) {
        Log.d(TAG, "onRegisterNextBtnClicked: ");

        showDialog();

        final String type_u;
        final DatabaseReference ref;

        if (TYPE == NORMAL_USER)  // USER REGISTER
        {
            type_u = NORMAL_USER_STRING;
            ref = normal_users_ref;
            Log.d(TAG, "onRegisterNextBtnClicked: 1");

        } else // ARTIST REGISTER
        {
            type_u = ARTIST_STRING;
            ref = artist_users_ref;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // save user to firebase db

                                final BaseUser user = new NormalUser();
                                user.setFrist_name(first_name);
                                user.setLast_name(last_name);
                                user.setPhone(phone);
                                user.setEmail(email);
                                user.setPassword(password);
                                user.setType(type_u);

                                final String userId = authResult.getUser().getUid();
                                String token_id = FirebaseInstanceId.getInstance().getToken();

                                Log.d(TAG, "onSuccess:Register UID = " + userId);
                                Log.d(TAG, "onSuccess:Register TID = " + token_id);
                                // use uid as key of childs
                                ref.child(authResult.getUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                hideDialog(dialog);
                                                Log.d(TAG, "onSuccess:Register added to db 1.1 ");
                                                //
                                                if (TextUtils.equals(type_u, NORMAL_USER_STRING)) { // normal user register done
                                                    showShortMessage("Registration finished, login now", findViewById(android.R.id.content));
                                                    showShortMessage("Registration Success", findViewById(android.R.id.content));
                                                    while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                                        getSupportFragmentManager().popBackStackImmediate();
                                                    }
                                                    showLoginFragment();
                                                } else { // artist user register done
                                                    showArtistQuestionsFragment(userId, user);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                hideDialog(dialog);
                                                Log.d(TAG, "onFailure:Register Saving to db : 1.2" + e.getMessage());
                                                if (e.getMessage().contains("network error")) {   // EMAIL ALREADY USED!
                                                    showShortMessage("Check network connection!", findViewById(android.R.id.content));
                                                } else {
                                                    showShortMessage("Check log for error", findViewById(android.R.id.content));
                                                }
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideDialog(dialog);
                        showShortMessage("Registeration Failed - log it", findViewById(android.R.id.content));
                        Log.d(TAG, "onFailure: Registration : 1.3" + e.getMessage());
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


    private void showArtistQuestionsFragment(String user_id, BaseUser user) {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        Bundle b = new Bundle();
        b.putString("uid", user_id);
        b.putString("user_first_name", user.getFrist_name());
        b.putString("user_last_name", user.getLast_name());
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
                    artist_users_ref.child(UID).child("more_details").setValue(artist_details)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Common.hideDialog(dialog);
                                    showShortMessage("Registration finished, login now", findViewById(android.R.id.content));
                                    Common.clearBackStackFragments(getSupportFragmentManager());
                                    showLoginFragment();
                                    Log.d(TAG, "onSuccess: added to db");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Common.hideDialog(dialog);
                                    Log.d(TAG, "onFailure: failed to add data to db EXCEPTION msg = " + e.getMessage());
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

    private void showEmailVerificationFragment(){
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



    private void sendResetPasswordEmail(final String email) {
        Log.d(TAG, "sendResetPasswordEmail: ");
        mFirebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: ");
                showLoginFragment();
                hideDialog(dialog);
                showShortMessage("Reset E-Mail sent",findViewById(android.R.id.content));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideDialog(dialog);
                Log.d(TAG, "onFailure: "+e.getMessage());
                Toast.makeText(context, "Error occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
