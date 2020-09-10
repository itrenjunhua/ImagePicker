package com.renj.imagepicker.model;

import android.support.annotation.IntDef;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-07-29   10:19
 * <p>
 * 描述：触摸裁剪区域的操作
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
@IntDef({
        ImagePickerTouchType.TOUCH_NONE,
        ImagePickerTouchType.TOUCH_OFFSET,
        ImagePickerTouchType.TOUCH_SCALE,
        ImagePickerTouchType.TOUCH_OFFSET_AND_SCALE,
})
public @interface ImagePickerTouchType {
    /**
     * 没有操作
     */
    int TOUCH_NONE = -1;
    /**
     * 移动
     */
    int TOUCH_OFFSET = 1;
    /**
     * 缩放
     */
    int TOUCH_SCALE = 2;
    /**
     * 移动加缩放
     */
    int TOUCH_OFFSET_AND_SCALE = 3;
}
