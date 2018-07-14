package com.spots.bella.constants;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spots.bella.R;
import com.spots.bella.activity.main_activity.MainActivity;

public class Common {
    public static final int ARTIST = 280;
    public static final int NORMAL_USER = 329;

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
        assert navigationView!=null;
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }
}
