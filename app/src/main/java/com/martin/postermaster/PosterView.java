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

    private Modle modle;
    private boolean isFirstDraw = true;
    private float viewRatio = 1080 * 1.0f / 720;

    public PosterView(Context context) {
        super(context);
    }

    public PosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModle(Modle modle) {
        this.modle = modle;
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
        if (null != modle) {
            if (getWidth() != 0) {
                if (isFirstDraw) {
                    modle.setDrawWidth(getWidth());
                }
                modle.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != modle) {
            modle.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}
