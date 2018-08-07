package com.spots.bella.constants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

public class ImageManager {


    private static final String TAG = "ImageManager";

    public static Bitmap getBitmap(Uri uri, Context context) {
        Log.d(TAG, "getBitmap: url " + uri);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getBitmap(String imgUrl) {
        File imageFile = new File(imgUrl);
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage());
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage());
            }
        }
        return bitmap;
    }

    public static byte[] getBytesFromBitmap(Bitmap bm, int quality) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }
}
