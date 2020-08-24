package com.renj.imagepicker.model;

import android.support.annotation.IntDef;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-07-29   10:19
 * <p>
 * 描述：
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
    int TOUCH_NONE = -1;
    int TOUCH_OFFSET = 1;
    int TOUCH_SCALE = 2;
    int TOUCH_OFFSET_AND_SCALE = 3;
}
