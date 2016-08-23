package com.martin.postermaster;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Martin on 2016/7/23 0023.
 */
public class PosterView extends View {

    private Model model;
    private boolean isFirstDraw = true;
    private float viewRatio = 1080 * 1.0f / 720;

    public PosterView(Context context) {
        super(context);
    }

    public PosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(Model model) {
        this.model = model;
        this.model.bindView(this);
        invalidate();
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (viewRatio != 0) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            int childWidthSize = getMeasuredWidth();
            int childHeightSize = getMeasuredHeight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            //按比例修改宽高
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * viewRatio * 1.0f), MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != model) {
            if (getWidth() != 0) {
                if (isFirstDraw) {
                    model.setDrawWidth(getWidth());
                    isFirstDraw = false;
                }
                model.draw(canvas);
            }
        }
    }

    public void destoryLayers() {
        if (null != model) {
            model.destroyLayer();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != model) {
            boolean b = model.onTouchEvent(event);
            invalidate();
            return b;
        }

        return super.onTouchEvent(event);
    }
}
