package com.martin.poster;

/**
 * Created by Martin on 2016/8/23 0023.
 * 内部焦点监听
 */
public interface LayerFocusChange {

    void requseFocus(Layer layer);

    void releaseFocus(Layer layer);

    void preSelect(Layer layer);

    void releasePreSelect(Layer layer);

}
