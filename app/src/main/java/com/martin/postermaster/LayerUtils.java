package com.martin.postermaster;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * @author Martin
 *         created at 2016/8/6 0006 1:58
 */
public class LayerUtils {


    // 计算两个触摸点之间的距离
    public static float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 计算两个触摸点的中点
    public static PointF middle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x / 2, y / 2);
    }

    /**
     * 获取旋转角度
     *
     * @param start
     * @param end
     * @param center
     * @return
     */
    public static float getDegrees(PointF start, PointF end, PointF center) {
        float degrees = getDegrees(end.x, end.y, start.x, start.y, center.x, center.y);
        return degrees;
    }

    /**
     * 根据连点，跟圆心的左边，算出夹角
     *
     * @param curX
     * @param curY
     * @param preX
     * @param preY
     * @param centerX
     * @param centerY
     * @return
     */
    public static float getDegrees(float curX, float curY, float preX, float preY, float centerX, float centerY) {
        double dCur = getRadian(curX, curY, centerX, centerY);
        double dPre = getRadian(preX, preY, centerX, centerY);
        return (float) ((dPre - dCur) * 180 / Math.PI);
    }

    /**
     * 算出一点和边的弧度
     *
     * @param x
     * @param y
     * @param centerX
     * @param centerY
     * @return
     */
    public static double getRadian(float x, float y, float centerX, float centerY) {
        double radian = 0;
        y -= centerY;
        x -= centerX;
        if (x == 0)
            return 0;
        double delt = Math.abs(y / x);
        if (y > 0 && x > 0) {
            radian = Math.atan(delt);
        } else if (y > 0 && x < 0) {
            radian = Math.PI - Math.atan(delt);
        } else if (y < 0 && x < 0) {
            radian = Math.PI + Math.atan(delt);
        } else if (y < 0 && x > 0) {
            radian = 2 * Math.PI - Math.atan(delt);
        }
        return radian;
    }

    /**
     * 计算缩放比例
     *
     * @param width
     * @param height
     * @param canvasW
     * @param canvasH
     * @return
     */
    public static float calculateFitScale(int width, int height, int canvasW, int canvasH) {
        float scaleW = 1;
        float scaleH = 1;
        if (width > 0) {
            scaleW = (float) canvasW / width;
        }
        if (height > 0) {
            scaleH = (float) canvasH / height;
        }
        if (scaleW < scaleH) {
            return scaleH;
        } else {
            return scaleW;
        }
    }

}
