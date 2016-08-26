package com.martin.poster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Martin on 2016/7/23 0023.
 */
public class BitmapUtils {

    public static Bitmap scaleBitmap(Bitmap b, float scale) {
        Bitmap resizeBitmap = null;
        Matrix scaleMatrix = new Matrix();
        if (b != null && !b.isRecycled()) {
            scaleMatrix.postScale(scale, scale);
            try {
                resizeBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), scaleMatrix, false);
            } catch (Exception e) {
                return b;
            }
        }
        return resizeBitmap;
    }

    /**
     * 清除bitmap对象
     *
     * @param bitmap 目标对象
     */
    public static void destroyBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            Bitmap b = bitmap;
            if (b != null && !b.isRecycled()) {
                b.recycle();
            }
            bitmap = null;
        }
    }

}
