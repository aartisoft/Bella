package com.spots.bella.constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.spots.bella.models.BaseUser;
import com.spots.bella.models.NormalUser;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class Common {
    private static final String TAG = "Common";
    public static final int ARTIST = 280;
    public static final int NORMAL_USER = 329;
    public static final String ARTIST_STRING = "ARTIST";
    public static final String NORMAL_USER_STRING = "NORMAL_USER";
    public static final String USER_STRING = "USER";

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

    public static boolean isValidEmail(String email) {
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

    public static boolean isWordContainsDigit(String s){
//        return s.matches("[A-Za-z][^.]*");
        return s.matches(".*\\d+.*");
    }

    public static BaseUser getUserData(SharedPreferences prefs) {
        BaseUser currentUser;
        String first_name = prefs.getString("user_first_name", null);
        String last_name = prefs.getString("user_last_name", null);
        String email = prefs.getString("user_email", null);
        String password = prefs.getString("user_password", null);
        String phone = prefs.getString("user_phone", null);
        String type = prefs.getString("user_type", null);
        if (first_name == null &&
                email == null &&
                last_name == null &&
                password == null &&
                phone == null) {
            return null;
        } else {
            currentUser = new BaseUser();
            currentUser.setFrist_name(first_name);
            currentUser.setLast_name(last_name);
            currentUser.setEmail(email);
            currentUser.setPassword(password);
            currentUser.setPhone(phone);
            currentUser.setType(type);
            return currentUser;
        }
    }


    public static String capitalizeFristLetter(String word) {
        if (word == null || word.length() == 0) {
            return "N/A";
        }

        return StringUtils.capitalize(word.toLowerCase().trim());
    }
}
