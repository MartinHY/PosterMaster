package com.martin.postermaster;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.nfc.Tag;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author Martin
 *         created at 2016/8/6 0006 2:04
 */
public class Layer {

    private static final String Tag = "Layer";
    private Bitmap layer;
    private Path layerPath;
    private RectF layerRectF;//layerPath外矩形

    private Bitmap drawLayer;//提供绘图的原图
    private Paint layerPaint;
    private Paint framePaint;
    private static final int frameColor = Color.parseColor("#FF3E96");

    private PaintFlagsDrawFilter drawFilter;

    private float x, y;
    private int width, height;

    private int degree = 0;//layer在cover中的旋转角度，tip：这里的旋转角度以在cover标注的layer的中心点的旋转角度为基准
    private float scale = 1;//以在视图中的宽度计算出来的缩放比例。

    public Layer(Bitmap layer, int degree) {
        this.layer = layer;
        this.degree = degree;
    }

    /**
     * 标记坐标点
     *
     * @param x
     * @param y
     * @return
     */
    public Layer markPoint(float x, float y) {
        if (null == layerPath) {
            layerPath = new Path();
            layerPath.moveTo(x, y);
        } else {
            layerPath.lineTo(x, y);
        }
        return this;
    }

    public Layer build() {
        layerPath.close();
        width = layer.getWidth();
        height = layer.getHeight();

        layerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        layerPaint.setAntiAlias(true);
        layerPaint.setFilterBitmap(true);

        framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        framePaint.setColor(frameColor);
        framePaint.setAntiAlias(true);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(3);

        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        return this;
    }

    public Layer build(Path path) {
        layerPath = path;
        width = layer.getWidth();
        height = layer.getHeight();

        layerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        layerPaint.setAntiAlias(true);
        layerPaint.setFilterBitmap(true);

        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        return this;
    }


    /**
     * 计算出 真实起点坐标，和结束坐标，可以确定出显示矩形框
     *
     * @param coverScale 遮盖层图片的缩放比例
     * @return
     */
    public void caculateDrawLayer(float coverScale) {
//        if (layerPath==null)
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.postScale(coverScale, coverScale);
        layerPath.transform(scaleMatrix);
        layerRectF = new RectF();
        layerPath.computeBounds(layerRectF, false);

        scale = LayerUtils.calculateFitScale(width, height, (int) layerRectF.width(), (int) layerRectF.height());
        drawLayer = BitmapUtils.scaleBitmap(layer, scale);
        // 更新Layer的坐标
        setLayerX(layerRectF.left - ((drawLayer.getWidth() - layerRectF.width()) / 2));
        setLayerY(layerRectF.top - ((drawLayer.getHeight() - layerRectF.height()) / 2));
    }

    public void draw(Canvas canvas) {
//        Paint paint1 = new Paint();
//        paint1.setColor(Color.BLUE);
//        canvas.drawRect(layerRectF, paint1);
//        layerPaint.setColor(Color.RED);
//        canvas.drawPath(layerPath, layerPaint);
        int a = canvas.save(Canvas.ALL_SAVE_FLAG);
        if (isInTouch) {
            layerPaint.setAlpha(125);//如果是点击状态 50%的透明度
        } else {
            canvas.clipPath(layerPath);
            layerPaint.setAlpha(255);
        }
        canvas.rotate(degree, layerRectF.centerX(), layerRectF.centerY());
        canvas.translate(x, y);
        canvas.drawBitmap(drawLayer, 0, 0, layerPaint);
        canvas.setDrawFilter(drawFilter);
        canvas.restoreToCount(a);
        if (isInTouch) {
            canvas.drawPath(layerPath, framePaint);
        }
    }


    public boolean isInTouch = false;

    private boolean isMultTouch = false;

    private PointF firstPointF = new PointF();
    private PointF centerPointF = new PointF();
    private PointF secondPointF = new PointF();
    private float lastX, lastY;

    private float originalDis = 1f; // 初始的两个手指按下的触摸点的距离
    private float lastDegrees;

    /**
     * 触摸事件处理
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(0);
        float y = event.getY(0);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchInLayer(x, y)) {
                    lastDegrees = 0;
                    lastX = x;
                    lastY = y;
                    firstPointF.set(x, y);//默认取得位置是单指的位置
                    isInTouch = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN://双指操作
                float x1 = event.getX(1);
                float y1 = event.getY(1);
                originalDis = LayerUtils.distance(event);

                isMultTouch = true;
                secondPointF.set(x1, y1);
                centerPointF = LayerUtils.middle(event);
                lastDegrees = LayerUtils.getDegrees(firstPointF, secondPointF, centerPointF);
//                Log.i(Tag, x1 + "  isMultTouch Layer   " + isMultTouch);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInTouch) {
                    if (isMultTouch) {
                        //做的旋转，缩放操作
                        secondPointF.set(event.getX(1), event.getY(1));
                        float newSpin = LayerUtils.getDegrees(firstPointF, secondPointF, centerPointF);
                        degree += (int) ((lastDegrees - newSpin) * 0.7f);
                        lastDegrees = newSpin;
//                        Log.i(Tag, "  rotate Layer   " + degree);
                    } else {//平移操作
                        moveLayer(event.getX(0) - lastX, event.getY(0) - lastY);
                        lastX = x;
                        lastY = y;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isInTouch = false;
                isMultTouch = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isMultTouch = false;
                isInTouch = false;
                break;
        }
        return isInTouch;
    }

    protected boolean isTouchInLayer(float x, float y) {
        return layerRectF.contains(x, y);
    }


    /**
     * 缩放图层
     *
     * @param toSacle
     */
    protected void scaleLayer(float toSacle) {
        scale = scale * toSacle;
        drawLayer = BitmapUtils.scaleBitmap(layer, scale);
        // 更新Layer的坐标
        setLayerX(x - (drawLayer.getWidth() - width) / 2);
        setLayerY(y - (drawLayer.getHeight() - height) / 2);
    }

    protected RectF scaleRect(float scale, RectF layerRectF) {
        return new RectF(layerRectF.left * 1.0f * scale, layerRectF.top * 1.0f * scale, layerRectF.right * 1.0f * scale, layerRectF.bottom * 1.0f * scale);
    }

    /**
     * 移动图层
     *
     * @param disX
     * @param disY
     */
    protected void moveLayer(float disX, float disY) {
        x = x + disX;
        y = disY + y;
    }

    protected void setLayerX(float x) {
        this.x = x;
    }

    protected void setLayerY(float y) {
        this.y = y;

    }

    /**
     * 清除layer内存
     */
    public void destroyLayer() {

        BitmapUtils.destroyBitmap(layer);
        BitmapUtils.destroyBitmap(drawLayer);

    }


    public Bitmap getLayer() {
        return layer;
    }

    public void setLayer(Bitmap layer) {
        this.layer = layer;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public float getScale() {
        return scale;
    }


}
