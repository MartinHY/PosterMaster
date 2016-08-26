package com.martin.postermaster;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Martin on 2016/7/20.
 */
public class BitmapUtils {

    private static final String Tag = "BitmapUtils";

    public static Bitmap postMatrix(Matrix matrix, Bitmap bitmap, boolean isRecycle) {
        Bitmap resource = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        if (isRecycle) destroyBitmap(bitmap);
        return resource;
    }

    /**
     * 真实偏移,图片宽高会根据图片的偏移距离重新生成宽高,回收原始图片
     *
     * @param bitmap,目标图片
     * @param preX,偏移X轴百分比
     * @param preY,偏移Y轴百分比
     * @return
     */
    public static Bitmap translate(Bitmap bitmap, float preX, float preY) {
        return translate(bitmap, preX, preY, true);
    }

    /**
     * 真实偏移,图片宽高会根据图片的偏移距离重新生成宽高
     *
     * @param bitmap,目标图片
     * @param preX,偏移X轴百分比
     * @param preY,偏移Y轴百分比
     * @param isRecycle，是否回收目标图
     * @return
     */
    public static Bitmap translate(Bitmap bitmap, float preX, float preY, boolean isRecycle) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int disX = (int) (width * preX);
        int disY = (int) (height * preY);
        matrix.postTranslate(disX, disY);
        Bitmap resource = Bitmap.createBitmap(bitmap.getWidth() - disX, bitmap.getHeight() - disY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resource);
//        canvas.drawColor(Color.BLACK);//模擬空白
        canvas.concat(matrix);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, -disX, -disY, paint);//从负偏移点开始画，那么如果进行了translate操作，那么图片不会留白
        if (isRecycle) destroyBitmap(bitmap);
        return resource;
    }

    /**
     * 真实缩放,图片宽高会根据图片的缩放比例重新生成宽高,回收原始图片
     *
     * @param bitmap
     * @param scale
     * @return
     */
    public static Bitmap scale(Bitmap bitmap, float scale) {
        return scale(bitmap, scale, true);
    }

    /**
     * 真实缩放,图片宽高会根据图片的缩放比例重新生成宽高
     *
     * @param bitmap,目标
     * @param scale，缩放比例
     * @param isRecycle，是否回收目标图
     * @return
     */
    public static Bitmap scale(Bitmap bitmap, float scale, boolean isRecycle) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        int reWidth, reHeight;
        reWidth = (int) (width * scale);
        reHeight = (int) (height * scale);
        matrix.postScale(scale, scale);

        Bitmap resource = Bitmap.createBitmap(reWidth, reHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resource);
//        canvas.drawColor(Color.BLUE);//模擬空白
        canvas.concat(matrix);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, (float) ((reWidth - width) * 1.0 / 2), (float) ((reHeight - height) * 1.0 / 2), paint);
        if (isRecycle) destroyBitmap(bitmap);
        return resource;
    }


    /**
     * 真实旋转,图片宽高会根据图片的旋转角度重新生成宽高,回收原始图片
     *
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotate(Bitmap bitmap, float degree) {
        return rotate(bitmap, degree, true);
    }

    /**
     * 真实旋转,图片宽高会根据图片的旋转角度重新生成宽高
     *
     * @param bitmap,目标
     * @param degree，旋转角度
     * @param isRecycle，是否回收目标图
     * @return
     */
    public static Bitmap rotate(Bitmap bitmap, float degree, boolean isRecycle) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        int reWidth, reHeight;
//       以弧度为计算方式则新图size为（width*cos(a)+height*sin(a), height*cos(a)+width*sin(a)）
        double angle = (degree * Math.PI) / 180;//生成degree对应的弧度
        double a = Math.abs(Math.sin(angle)), b = Math.abs(Math.cos(angle));
        reWidth = (int) (width * b + height * a);
        reHeight = (int) (height * b + width * a);
        Log.i(Tag, "width: " + width + "   reWidth   :" + reWidth);
        Log.i(Tag, "height: " + height + "   reHeight   :" + reHeight);
        Bitmap resource = Bitmap.createBitmap(reWidth, reHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resource);
//        canvas.drawColor(Color.BLACK);//模擬空白
        matrix.postRotate(degree, reWidth / 2, reHeight / 2);
        canvas.concat(matrix);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, (float) ((reWidth - width) * 1.0 / 2), (float) ((reHeight - height) * 1.0 / 2), paint);
        if (isRecycle) destroyBitmap(bitmap);
        return resource;
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
