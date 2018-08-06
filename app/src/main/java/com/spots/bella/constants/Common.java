package com.spots.bella.constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spots.bella.PreferenceManager;
import com.spots.bella.models.BaseUser;
import com.spots.bella.models.MoreDetailsUserArtist;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class Common {
    private static final String TAG = "Common";
    public static final int ARTIST = 280;
    public static final int NORMAL_USER = 329;
    public static final String ARTIST_STRING = "ARTIST";
    public static final String NORMAL_USER_STRING = "NORMAL_USER";
    public static final String USER_STRING = "USER";
    public static final int LOADING_DURATION = 1500;

    public static final int GALLERY_PICK_POST_IMAGE = 160;
    public static final String FIREBASE_IMAGE_STORAGE = "photos/users";
    public static final String USER_PHOTOS_STRING = "user_photos";
    public static final String STRING_PHOTOS = "photos";
    public static final String USER_ACCOUNT_SETTINGS = "user_account_settings";
    public static final String USER_NAME_FIELD_STRING = "user_name";
    public static final String STRING_WEBSITE_FIELD = "website";
    public static final String STRING_USER_DESCRIPTION = "description";
    public static final String PHONE_STRING = "phone";

    public static void darkStatusBarSetup(Window window) {
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT *//*&& Build.VERSION.SDK_INT < Build.VERSION_CODES.M*//*) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
            View decor = window.getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static Pattern phonePattern = Pattern.compile("\\d{11}");

    public static Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[c-o-m]{3,})$");

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public static boolean isValidEmailForm(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static int getStatusBarHeight(Context ctx) {
        float result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimension(resourceId);
        }
        return (int) result;
    }

    public static void clearBackStackFragments(FragmentManager supportFragmentManager) {
        while (supportFragmentManager.getBackStackEntryCount() > 0) {
            supportFragmentManager.popBackStackImmediate();
        }
    }

    public static void setTranslucentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void syncToolbar(TextView textView, int gravity, String title) {
        assert textView != null;
        textView.setGravity(gravity);
        textView.setText(title);
    }

    public static void syncToolbar(TextView textView, int gravity, int title) {
        assert textView != null;
        textView.setGravity(gravity);
        textView.setText(title);
    }

    public static void setMenuCounter(NavigationView navigationView, @IdRes int itemId, int count) {
        assert navigationView != null;
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    public static void hideDialog(AlertDialog dialog) {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    public static void showShortMessage(String message, View v) {
        final Snackbar snack = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
        snack.show();
    }

    public static boolean isWordContainsDigit(String s) {
//        return s.matches("[A-Za-z][^.]*");
        return s.matches(".*\\d+.*");
    }

    public static BaseUser getUserData(PreferenceManager pM) {
        BaseUser currentUser;
        String first_name = pM.readeString(PreferenceManager.USER_FULL_NAME);
        String last_name = pM.readeString(PreferenceManager.USER_LAST_NAME);
        String email = pM.readeString(PreferenceManager.USER_EMAIL);
        String password = pM.readeString(PreferenceManager.USER_PASSWORD);
        String phone = pM.readeString(PreferenceManager.USER_PHONE);
        String type = pM.readeString(PreferenceManager.USER_TYPE);
        if (first_name == null &&
                email == null &&
                last_name == null &&
                password == null &&
                phone == null) {
            return null;
        } else {
            currentUser = new BaseUser();
            currentUser.setFull_name(first_name);
            currentUser.setEmail(email);
            currentUser.setPassword(password);
            currentUser.setPhone(phone);
            currentUser.setType(type);
            return currentUser;
        }
    }

    public static void saveUserData(PreferenceManager pM, String full_name, String email, String password, String user_phone, String user_type, MoreDetailsUserArtist moreDetails) {
        pM.writeSharedPrefrenceString(PreferenceManager.USER_FULL_NAME, full_name);
        pM.writeSharedPrefrenceString(PreferenceManager.USER_EMAIL, email);
        pM.writeSharedPrefrenceString(PreferenceManager.USER_PASSWORD, password);
        pM.writeSharedPrefrenceString(PreferenceManager.USER_PHONE, user_phone);
        pM.writeSharedPrefrenceString(PreferenceManager.USER_TYPE, user_type);
        if (moreDetails != null && user_type.equals(ARTIST_STRING)) {
            pM.writeSharedPrefrenceString(PreferenceManager.USER_CITY, moreDetails.getCity());
            pM.writeSharedPrefrenceString(PreferenceManager.USER_MAX_ORDERS, moreDetails.getMax_orders());
        }
    }


    public static String capitalizeFristLetter(String word) {
        if (word == null || word.length() == 0) {
            return "N/A";
        }

        return StringUtils.capitalize(word.toLowerCase().trim());
    }

    public static void hideSoftKeyboard() {

    }

    public static void openGallery(Activity activity, int code) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        activity.startActivityForResult(galleryIntent, code);
    }
}
