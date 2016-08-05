package com.martin.postermaster;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.List;

/**
 * Created by Martin on 2016/7/23 0023.
 */
public class Modle {

    private static final String Tag = "Modle";

    private Bitmap cover;//遮盖图片
    private Bitmap drawCover;//遮盖图片

    private int width;
    private int height;

    private float scale;
    private List<Layer> layers;//这里的图层，需要按实际绘图顺序添加

    private int defaultWidth = 720;//防止从本地mipmap or drawable 中加载的图片被studio处理过，导致图片宽高与坐标点的比例不符

    public Modle(Bitmap cover, List<Layer> layers) {
        this.cover = cover;
        width = cover.getWidth();
        height = cover.getHeight();
        Log.i(Tag, "cover的宽 ：" + width + "   cover的高 ：" + height);//这里获取的图片的宽高既然比真实图片还要大··
        this.layers = layers;
    }

    public void setDrawWidth(int drawWidth) {
        scale = drawWidth * 1.0f / width;
        width = drawWidth;
        height = (int) (height * scale);
        drawCover = BitmapUtils.scaleBitmap(cover, scale);
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).caculateDrawLayer(drawWidth * 1.0f / defaultWidth);
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < layers.size(); i++) {
            if (!layers.get(i).isInTouch)
                layers.get(i).draw(canvas);
        }
        canvas.drawBitmap(drawCover, 0, 0, null);
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).isInTouch)
                layers.get(i).draw(canvas);
        }
    }

    /**
     * 清除layer内存
     */
    public void destroyLayer() {

        BitmapUtils.destroyBitmap(cover);
        BitmapUtils.destroyBitmap(drawCover);
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).destroyLayer();
        }

    }

    /**
     * 触摸事件处理
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).onTouchEvent(event);
        }
        return true;
    }


}
