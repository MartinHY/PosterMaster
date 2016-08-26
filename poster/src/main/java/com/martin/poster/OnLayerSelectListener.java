package com.martin.poster;

/**
 * Created by Martin on 2016/8/20 0020.
 *
 * 外部点击监听
 */
public interface OnLayerSelectListener {

    void onSelected(Layer layer);

    void dismiss(Layer layer);
}
