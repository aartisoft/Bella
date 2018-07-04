package com.spots.bella.activity.login_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.spots.bella.R;
import com.spots.bella.activity.WizardActivity;
import com.spots.bella.activity.login_activity.fragments.ArtistQuestionsFragment;
import com.spots.bella.activity.login_activity.fragments.DetectRegisterationFragment;
import com.spots.bella.activity.login_activity.fragments.LoginFragment;
import com.spots.bella.activity.login_activity.fragments.RegisterationFragment;

import static com.spots.bella.constants.Common.NORMAL_USER;
import static com.spots.bella.constants.Common.darkStatusBarSetup;

public class LoginActivity extends AppCompatActivity implements
        LoginFragment.OnLoginFragmentInteractionListener,
        DetectRegisterationFragment.OnDetectRegisterationFragmentInteractionListener,
        ArtistQuestionsFragment.OnArtistQuestionsFragmentInteractionListener,
        RegisterationFragment.OnRegisterFragmentInteractionListener {

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
        Toast.makeText(this, "Design not ready!", Toast.LENGTH_SHORT).show();
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
                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                showLoginFragment();
            }
        }, 3000);
    }

    AlertDialog dialog;

    private void showDialog() {
//        @BindView(R.id.progress)
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setTitle("Title");
//        dialog.setMessage("Message");
        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
        View sign_in_layout = inflater.inflate(R.layout.sign_in_layout, null);
        ProgressBar progressBar = sign_in_layout.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        doubleBounce.setBounds(0, 0, 100, 100);
//        doubleBounce.setColor(getResources().getColor(R.color.white));
        doubleBounce.setColor(Color.WHITE);
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.setView(sign_in_layout);
        dialog.setCancelable(false);
        /*dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG, "onCancel: ");
            }
        });*/
//        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(final DialogInterface dialogInterface, int i) {
//
//
//            }
//        });
//        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
        dialog.show();
    }

    //    private void hideToolbar() {
//        toolbar.setVisibility(View.GONE);
//    }
//
//    private void hideToolbarBack() {
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            getSupportActionBar().setHomeButtonEnabled(false);
//        }
//    }

//    private void showToolbarBack() {
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//        }
//    }
//
//    private void showToolbar() {
//        toolbar.setVisibility(View.VISIBLE);
//    }
//    public void enableBackButton(boolean enable) {
//        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
//        // when you enable on one, you disable on the other.
//        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
//        if (enable) {
//            // Remove hamburger
//            mDrawerToggle.setDrawerIndicatorEnabled(false);
//            // Show back button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
//            // clicks are disabled i.e. the UP button will not work.
//            // We need to add a listener, as in below, so DrawerToggle will forward
//            // click events to this listener.
//            if (!mToolBarNavigationListenerIsRegistered) {
//                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Doesn't have to be onBackPressed
//                        onBackPressed();
//                    }
//                });
//
//                mToolBarNavigationListenerIsRegistered = true;
//                mSetting.saveToolbarNavigationState(mToolBarNavigationListenerIsRegistered);
//            }
//
//        } else {
//            // Remove back button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            // Show hamburger
//            mDrawerToggle.setDrawerIndicatorEnabled(true);
//            // Remove the/any drawer toggle listener
//            mDrawerToggle.setToolbarNavigationClickListener(null);
//            mToolBarNavigationListenerIsRegistered = false;
//            mSetting.saveToolbarNavigationState(mToolBarNavigationListenerIsRegistered);
//        }
//
//        // So, one may think "Hmm why not simplify to:
//        // .....
//        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
//        // mDrawer.setDrawerIndicatorEnabled(!enable);
//        // ......
//        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
//    }


//        et_login_fragment_password.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (et_login_fragment_password.getRight() - et_login_fragment_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        // your action here
//                        et_login_fragment_password.requestFocus();
//                        /*et_login_fragment_password.setInputType(InputType.TYPE_CLASS_TEXT |
//                                InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                        et_login_fragment_password.setSelection(et_login_fragment_password.getText().length());
//                      */  return true;
//                    }
//                }
//                return false;
//            }
//        });
}
