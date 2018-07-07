package com.spots.bella.activity.login_activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.DoubleBounce;
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

import static com.spots.bella.activity.login_activity.fragments.ArtistQuestionsFragment.*;
import static com.spots.bella.activity.login_activity.fragments.DetectRegisterationFragment.*;
import static com.spots.bella.activity.login_activity.fragments.LoginFragment.*;
import static com.spots.bella.activity.login_activity.fragments.RegisterationFragment.*;
import static com.spots.bella.constants.Common.NORMAL_USER;
import static com.spots.bella.constants.Common.darkStatusBarSetup;

public class LoginActivity extends AppCompatActivity implements
        OnLoginFragmentInteractionListener,
        OnDetectRegisterationFragmentInteractionListener,
        OnArtistQuestionsFragmentInteractionListener,
        OnForgotPasswordFragmentInteractionListener,
        OnConfirmCodeFragmentInteractionListener,
        OnResetPasswordFragmentInteractionListener,
        OnRegisterFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        darkStatusBarSetup(getWindow());
        setContentView(R.layout.activity_login);
        showLoginFragment();
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
    public void onRegisterNextBtnClicked(int TYPE) {
        if (TYPE == NORMAL_USER) {
            // register - > success -> show -> frist login -> wizard
            //                              -> Home activity
            showDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.cancel();
                    }
                    Toast.makeText(LoginActivity.this, "Registration Success...", Toast.LENGTH_SHORT).show();
                    while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    showLoginFragment();
                }
            }, 3000);
        } else {
            // register -> success -> show Fragment Questions -> home activity
            showDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.cancel();
                    }
                    showArtistQuestionsFragment("123");
                }
            }, 3000);
        }
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
