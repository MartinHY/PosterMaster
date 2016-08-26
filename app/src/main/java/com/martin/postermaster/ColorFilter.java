package com.martin.postermaster;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.widget.ImageView;

/**
 * 我收集的颜色滤镜
 * Created by Martin on 2016/8/1 0001.
 */
public class ColorFilter {

    /**
     * 为imageView设置颜色滤镜
     *
     * @param imageView
     * @param colormatrix
     */
    public static void imageViewColorFilter(ImageView imageView, float[] colormatrix) {
        setColorMatrixColorFilter(imageView, new ColorMatrixColorFilter(new ColorMatrix(colormatrix)));
    }

    /**
     * 为imageView设置颜色偏向滤镜
     *
     * @param imageView
     * @param color
     */
    public static void imageViewColorFilter(ImageView imageView, int color) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
        setColorMatrixColorFilter(imageView, new ColorMatrixColorFilter(colorMatrix));
    }


    /**
     * 生成对应颜色偏向滤镜的图片，并回收原图
     *
     * @param bitmap
     * @param color
     * @return
     */
    public static Bitmap bitmapColorFilter(Bitmap bitmap, int color) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
        return setColorMatrixColorFilter(bitmap, new ColorMatrixColorFilter(colorMatrix), true);
    }

    /**
     * 生成对应颜色滤镜的图片，并回收原图
     *
     * @param bitmap
     * @param colormatrix
     * @return
     */
    public static Bitmap bitmapColorFilter(Bitmap bitmap, float[] colormatrix) {
        return setColorMatrix(bitmap, colormatrix, true);
    }

    /**
     * 生成对应颜色滤镜的图片
     *
     * @param bitmap
     * @param colormatrix
     * @param isRecycle
     * @return
     */
    public static Bitmap setColorMatrix(Bitmap bitmap, float[] colormatrix, boolean isRecycle) {
        return setColorMatrixColorFilter(bitmap, new ColorMatrixColorFilter(new ColorMatrix(colormatrix)), isRecycle);
    }


    public static void setColorMatrixColorFilter(ImageView imageView, ColorMatrixColorFilter matrixColorFilter) {
        imageView.setColorFilter(matrixColorFilter);
    }

    public static Bitmap setColorMatrixColorFilter(Bitmap bitmap, ColorMatrixColorFilter matrixColorFilter, boolean isRecycle) {
        Bitmap resource = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColorFilter(matrixColorFilter);
        Canvas canvas = new Canvas(resource);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (isRecycle)
            BitmapUtils.destroyBitmap(bitmap);
        return resource;
    }


    // 黑白
    public static final float colormatrix_heibai[] = {0.8f, 1.6f, 0.2f, 0,
            -163.9f, 0.8f, 1.6f, 0.2f, 0, -163.9f, 0.8f, 1.6f, 0.2f, 0,
            -163.9f, 0, 0, 0, 1.0f, 0};
    // 怀旧
    public static final float colormatrix_huajiu[] = {0.2f, 0.5f, 0.1f, 0,
            40.8f, 0.2f, 0.5f, 0.1f, 0, 40.8f, 0.2f, 0.5f, 0.1f, 0, 40.8f, 0,
            0, 0, 1, 0};
    // 哥特
    public static final float colormatrix_gete[] = {1.9f, -0.3f, -0.2f, 0,
            -87.0f, -0.2f, 1.7f, -0.1f, 0, -87.0f, -0.1f, -0.6f, 2.0f, 0,
            -87.0f, 0, 0, 0, 1.0f, 0};
    // 淡雅
    public static final float colormatrix_danya[] = {0.6f, 0.3f, 0.1f, 0,
            73.3f, 0.2f, 0.7f, 0.1f, 0, 73.3f, 0.2f, 0.3f, 0.4f, 0, 73.3f, 0,
            0, 0, 1.0f, 0};
    // 蓝调
    public static final float colormatrix_landiao[] = {2.1f, -1.4f, 0.6f,
            0.0f, -71.0f, -0.3f, 2.0f, -0.3f, 0.0f, -71.0f, -1.1f, -0.2f, 2.6f,
            0.0f, -71.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    // 光晕
    public static final float colormatrix_guangyun[] = {0.9f, 0, 0, 0, 64.9f,
            0, 0.9f, 0, 0, 64.9f, 0, 0, 0.9f, 0, 64.9f, 0, 0, 0, 1.0f, 0};

    // 梦幻
    public static final float colormatrix_menghuan[] = {0.8f, 0.3f, 0.1f,
            0.0f, 46.5f, 0.1f, 0.9f, 0.0f, 0.0f, 46.5f, 0.1f, 0.3f, 0.7f, 0.0f,
            46.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    // 酒红
    public static final float colormatrix_jiuhong[] = {1.2f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.9f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 胶片
    public static final float colormatrix_fanse[] = {-1.0f, 0.0f, 0.0f, 0.0f,
            255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f,
            255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    // 湖光掠影
    public static final float colormatrix_huguang[] = {0.8f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.9f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 褐片
    public static final float colormatrix_hepian[] = {1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 复古
    public static final float colormatrix_fugu[] = {0.9f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f,
            0, 0, 0, 1.0f, 0};
    // 泛黄
    public static final float colormatrix_huan_huang[] = {1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f,
            0.0f, 0, 0, 0, 1.0f, 0};
    // 传统
    public static final float colormatrix_chuan_tong[] = {1.0f, 0.0f, 0.0f, 0,
            -10f, 0.0f, 1.0f, 0.0f, 0, -10f, 0.0f, 0.0f, 1.0f, 0, -10f, 0, 0,
            0, 1, 0};
    // 胶片2
    public static final float colormatrix_jiao_pian[] = {0.71f, 0.2f, 0.0f,
            0.0f, 60.0f, 0.0f, 0.94f, 0.0f, 0.0f, 60.0f, 0.0f, 0.0f, 0.62f,
            0.0f, 60.0f, 0, 0, 0, 1.0f, 0};

    // 锐色
    public static final float colormatrix_ruise[] = {4.8f, -1.0f, -0.1f, 0,
            -388.4f, -0.5f, 4.4f, -0.1f, 0, -388.4f, -0.5f, -1.0f, 5.2f, 0,
            -388.4f, 0, 0, 0, 1.0f, 0};
    // 清宁
    public static final float colormatrix_qingning[] = {0.9f, 0, 0, 0, 0, 0,
            1.1f, 0, 0, 0, 0, 0, 0.9f, 0, 0, 0, 0, 0, 1.0f, 0};
    // 浪漫
    public static final float colormatrix_langman[] = {0.9f, 0, 0, 0, 63.0f,
            0, 0.9f, 0, 0, 63.0f, 0, 0, 0.9f, 0, 63.0f, 0, 0, 0, 1.0f, 0};
    // 夜色
    public static final float colormatrix_yese[] = {1.0f, 0.0f, 0.0f, 0.0f,
            -66.6f, 0.0f, 1.1f, 0.0f, 0.0f, -66.6f, 0.0f, 0.0f, 1.0f, 0.0f,
            -66.6f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};


}
