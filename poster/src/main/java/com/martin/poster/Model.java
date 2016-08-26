package com.martin.poster;

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
public class Model {

    private static final String Tag = "Model";
    private Bitmap cover;//遮盖图片
    private Bitmap drawCover;//遮盖图片

    private int width, drawWidth;
    private int height;

    private float scale;
    private List<Layer> layers;//这里的图层，需要按实际绘图顺序添加

    private int defaultWidth = 720;//防止从本地mipmap or drawable 中加载的图片被studio处理过，导致图片宽高与坐标点的比例不符

    private ModelView modelView;

    private Layer focusLayer, preSelectLayer;
    private Paint framePaint;
    private static final int frameColor = Color.parseColor("#FF3E96");

    public Model(Bitmap cover, List<Layer> layers) {
        this.cover = cover;
        width = cover.getWidth();
        height = cover.getHeight();
        this.layers = layers;
        for (Layer layer : layers)
            layer.setLayerFocusChange(focusChange);

        framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        framePaint.setColor(frameColor);
        framePaint.setAntiAlias(true);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(3);
    }

    public void setDrawWidth(int drawWidth) {
        this.drawWidth = drawWidth;
        scale = drawWidth * 1.0f / width;
        width = drawWidth;
        height = (int) (height * scale);
        drawCover = BitmapUtils.scaleBitmap(cover, scale);
        for (Layer layer : layers)
            layer.caculateDrawLayer(drawWidth * 1.0f / defaultWidth);
    }

    public void draw(Canvas canvas) {
        for (Layer layer : layers)
            if (!layer.isThouched && !layer.isPreSelect())
                layer.draw(canvas);
        canvas.drawBitmap(drawCover, 0, 0, null);
        if (focusLayer != null && preSelectLayer == null) {
            focusLayer.setCanDrawFrame(true);
            focusLayer.draw(canvas);
        } else if (focusLayer != null && preSelectLayer != null) {
            preSelectLayer.setCanDrawFrame(false);
            preSelectLayer.draw(canvas);
            focusLayer.setCanDrawFrame(false);
            focusLayer.draw(canvas);
            canvas.drawRect(preSelectLayer.layerRectF, framePaint);
        }
    }

    /**
     * 清除layer内存
     */
    public void destroyLayer() {
        BitmapUtils.destroyBitmap(cover);
        BitmapUtils.destroyBitmap(drawCover);
        for (Layer layer : layers)
            layer.destroyLayer();

    }

    /**
     * 触摸事件处理
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(0);
        float y = event.getY(0);

        if (focusLayer != null && event.getAction() == MotionEvent.ACTION_UP) {
            int selectedCount = 0;
            for (Layer layer1 : layers) {
                if (layer1 != focusLayer && layer1.isPreSelect()) {//进行计数如果选中的图层数量大于2，那么进行交换
                    swithLayer(focusLayer, layer1);
                    return true;
                }
            }
        }

        for (Layer layer : layers) {
            if (layer.onTouchEvent(event)) {//必须要在它处理之前进行交换
                for (Layer layer1 : layers) {
                    if (layer1 != layer) {
                        if (layer1.isTouchInLayer(x, y)) {//判断是否移动到了其他的矩形块上
                            layer1.setPreSelect(true);
                        } else {
                            layer1.setPreSelect(false);
                        }
                    }
                }
                return true;
            }
        }

        return true;
    }

    private void swithLayer(Layer layer, Layer layer1) {
        Bitmap preBitmap = layer.getLayer();
        Bitmap preFilterBitmap = layer.getFilterLayer();
        layer.resetLayer(layer1.getLayer(), layer1.getFilterLayer());
        layer1.resetLayer(preBitmap, preFilterBitmap);
        layer.caculateDrawLayer(drawWidth * 1.0f / defaultWidth);
        layer1.caculateDrawLayer(drawWidth * 1.0f / defaultWidth);
        focusLayer = null;
        if (modelView != null) {
            modelView.invalidate();
        }
    }

    public void setOnLayerSelectListener(OnLayerSelectListener onLayerSelectListener) {
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).setOnLayerSelectListener(onLayerSelectListener);
        }
    }

    LayerFocusChange focusChange = new LayerFocusChange() {
        @Override
        public void requseFocus(Layer layer) {
            focusLayer = layer;
        }

        @Override
        public void releaseFocus(Layer layer) {
            if (focusLayer == layer)
                focusLayer = null;
        }

        @Override
        public void preSelect(Layer layer) {
            preSelectLayer = layer;
        }

        @Override
        public void releasePreSelect(Layer layer) {
            if (preSelectLayer == layer)
                preSelectLayer = null;
        }
    };

    public void bindView(ModelView modelView) {
        this.modelView = modelView;
    }

    public void releaseAllFocus() {
        for (Layer layer : layers)
            layer.releaseAllFocus();
    }

}
