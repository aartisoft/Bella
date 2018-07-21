package com.spots.bella.activity.login_activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.spots.bella.R;
import com.spots.bella.activity.WizardActivity;
import com.spots.bella.activity.login_activity.fragments.ArtistQuestionsFragment;
import com.spots.bella.activity.login_activity.fragments.ConfirmCodeFragment;
import com.spots.bella.activity.login_activity.fragments.ConfirmCodeFragment.OnConfirmCodeFragmentInteractionListener;
import com.spots.bella.activity.login_activity.fragments.DetectRegisterationFragment;
import com.spots.bella.activity.login_activity.fragments.ForgotPasswordFragment;
import com.spots.bella.activity.login_activity.fragments.ForgotPasswordFragment.OnForgotPasswordFragmentInteractionListener;
import com.spots.bella.activity.login_activity.fragments.LoginFragment;
import com.spots.bella.activity.login_activity.fragments.RegisterationFragment;
import com.spots.bella.activity.login_activity.fragments.ResetPasswordFragment;
import com.spots.bella.activity.login_activity.fragments.ResetPasswordFragment.OnResetPasswordFragmentInteractionListener;
import com.spots.bella.constants.Common;
import com.spots.bella.di.BaseActivity;
import com.spots.bella.models.ArtistUser;

import java.util.HashMap;
import java.util.Map;

import static com.spots.bella.activity.login_activity.fragments.ArtistQuestionsFragment.*;
import static com.spots.bella.activity.login_activity.fragments.DetectRegisterationFragment.*;
import static com.spots.bella.activity.login_activity.fragments.LoginFragment.*;
import static com.spots.bella.activity.login_activity.fragments.RegisterationFragment.*;
import static com.spots.bella.constants.Common.ARTIST_STRING;
import static com.spots.bella.constants.Common.NORMAL_USER;
import static com.spots.bella.constants.Common.USER_STRING;
import static com.spots.bella.constants.Common.darkStatusBarSetup;
import static com.spots.bella.constants.Common.hideDialog;

public class LoginActivity extends BaseActivity implements
        OnLoginFragmentInteractionListener,
        OnDetectRegisterationFragmentInteractionListener,
        OnArtistQuestionsFragmentInteractionListener,
        OnForgotPasswordFragmentInteractionListener,
        OnConfirmCodeFragmentInteractionListener,
        OnResetPasswordFragmentInteractionListener,
        OnRegisterFragmentInteractionListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        darkStatusBarSetup(getWindow());
        setContentView(R.layout.activity_login);

        initFirebase();

        showLoginFragment();

    }

    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onAuthStateChanged: 1");
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: 2");
                  /*  Intent intent = new Intent(SignUp.this,SignUpArtistComplete.class);
                    startActivity(intent);
                    dialog.dismiss();
                    //progressBar.setVisibility(View.GONE);
                    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
                    finish();
                    return;*/
                } else {
                    Log.d(TAG, "onAuthStateChanged: ");
                }
            }
        };
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
    public void onLoginBtnClicked() {
        Log.d(TAG, "onLoginBtnClicked: ");
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    // empty fields
                    dialog.cancel();
                    startActivity(new Intent(LoginActivity.this, WizardActivity.class));
                    finish();
                }
            }
        }, 3000);
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
    public void onRegisterNextBtnClicked(final int TYPE) {
        Log.d(TAG, "onRegisterNextBtnClicked: ");
        register(TYPE);
        if (TYPE == NORMAL_USER) {
            // register - > success -> show -> frist login -> wizard
            //                              -> Home activity
            showDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    register(TYPE);
                   /* if (dialog != null) {
                        dialog.cancel();
                    }
                    Toast.makeText(LoginActivity.this, "Registration Success...", Toast.LENGTH_SHORT).show();
                    while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    showLoginFragment();*/
                }
            }, 3000);
        } else {
            // register -> success -> show Fragment Questions -> home activity
            showDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideDialog(dialog);

                    showArtistQuestionsFragment("123");
                }
            }, 3000);
        }
    }

    private void register(int type) {
        Log.d(TAG, "register: ");

        mFirebaseAuth.createUserWithEmailAndPassword("123@gmail.com", "111111").addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {                                                           // REGISTER SUCCESS
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    assert user != null;
                    String userId = user.getUid();
                    String token_id = FirebaseInstanceId.getInstance().getToken();

                    Log.d(TAG, "onComplete: UID = " + userId);
                    Log.d(TAG, "onComplete: TID = " + token_id);

                    Map<String, ArtistUser> map = new HashMap<>();
                    map.put(userId, new ArtistUser("first_name", "last_name", "xyz@gmail.com", "111111"));
                    DatabaseReference artist_users_ref = FirebaseDatabase.getInstance().getReference().child(USER_STRING).child(ARTIST_STRING);
                    artist_users_ref.setValue(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Log.d(TAG, "onComplete: 1");
                            hideDialog(dialog);
                            if (databaseError != null) {
                                Log.e(TAG, "Data could not be saved " + databaseError.getMessage());
                            } else {
                                Log.e(TAG, "Data saved successfully.");
                            }

                        }
                    });

                } else {                                                            // REGISTER FAIL
                    hideDialog(dialog);
                    Log.d(TAG, "onComplete: 2");
                    // If sign in fails, display a message to the user.
                    Exception exception;
                    exception = task.getException();
                    if (exception != null) {                                            // EXCEPTION
                        Log.w(TAG, "createUserWithEmail:failure", exception);
                        String ex = exception.getMessage();
                        if (ex.contains("email address is already in use by another account")) {
                            Toast.makeText(context, "email address is already in use by another account", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Check log for error", Toast.LENGTH_LONG).show();
                        }
                    } else {                                                      // PROBLEM UNKNOWN
                        Toast.makeText(context, "unknown error", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onComplete: Unknown Error");
                    }
                }
            }
        });
    }

    private void showArtistQuestionsFragment(String user_id) {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
//        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_login_activity_container, new ArtistQuestionsFragment(), getResources().getString(R.string.user_registeration_fragment_tag));
        fragmentTransaction.commit();

    }

    @Override
    public void onArtistQuestionsFragmentOpened() {
        Log.d(TAG, "onArtistQuestionsFragmentOpened: ");
    }

    @Override
    public void onArtistQuestionsFragmentBtnClickFinish() {
        Log.d(TAG, "onArtistQuestionsFragmentBtnClickFinish: ");
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    // empty fields
                    dialog.cancel();
                }
                Toast.makeText(LoginActivity.this, "Registration Success...", Toast.LENGTH_SHORT).show();
                Common.clearBackStackFragments(getSupportFragmentManager());
                showLoginFragment();
            }
        }, 3000);
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
        sendResetPasswordEmail(email);
    }

    private void showConfirmationCodeFragment(String email) {
        ConfirmCodeFragment fragment = new ConfirmCodeFragment();
        Bundle b = new Bundle();
        b.putString("email", email);
        fragment.setArguments(b);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_login_activity_container, fragment, getResources().getString(R.string.confirm_code_fragment_tag));
        fragmentTransaction.addToBackStack(null).commit();
    }

    @Override
    public void onConfirmCodeOpened() {
        Log.d(TAG, "onConfirmCodeOpened: ");
    }

    @Override
    public void onResendCodeClicked(String email) {
        Log.d(TAG, "onResendCodeClicked: ");
        sendResetPasswordEmail(email);
    }

    @Override
    public void onConfirmCodeNextClicked(String code, String email) {
        Log.d(TAG, "onConfirmCodeNextClicked: ");
        checkValidation(code, email);
    }

    private void checkValidation(String code, final String email) {
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    // empty fields
                    dialog.cancel();
                }
                // show next level
                showResetPasswordFragment(email);
            }
        }, 3000);
    }

    private void showResetPasswordFragment(String email) {
        Log.d(TAG, "showResetPasswordFragment: ");
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle b = new Bundle();
        b.putString("email", email);
        fragment.setArguments(b);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_login_activity_container, fragment, getResources().getString(R.string.reset_password_fragment_tag));
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void sendResetPasswordEmail(final String email) {
        Log.d(TAG, "sendResetPasswordEmail: ");
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    // empty fields
                    dialog.cancel();
                }
                Toast.makeText(LoginActivity.this, "Confirmation code sent to your e-mail.", Toast.LENGTH_SHORT).show();
                showConfirmationCodeFragment(email);
            }
        }, 3000);
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
        }, 3000);
    }
}
