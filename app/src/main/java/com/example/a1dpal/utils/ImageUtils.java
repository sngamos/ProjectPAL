package com.example.a1dpal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImageUtils {

    public static Bitmap rotateBitmap(Context context, Bitmap source) {
        int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        switch (rotation) {
            case Surface.ROTATION_90:
                matrix.postRotate(-90); // Rotate counter-clockwise for landscape
                break;
            case Surface.ROTATION_270:
                matrix.postRotate(90); // Rotate clockwise for reverse landscape
                break;
            default:
                // No rotation for portrait or reverse portrait
                return source;
        }
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static void setLandscapeImage(Context context, ImageView imageView, int resourceId, int targetWidth, int targetHeight) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        Bitmap rotatedBitmap = rotateBitmap(context, originalBitmap);

        // Resize the bitmap to the target width and height
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, targetWidth, targetHeight, true);

        imageView.setImageBitmap(resizedBitmap);
    }
}


