package com.spots.bella.constants;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

}
