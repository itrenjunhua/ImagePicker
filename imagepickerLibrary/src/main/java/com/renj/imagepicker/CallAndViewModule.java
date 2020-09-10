package com.renj.imagepicker;

import com.renj.imagepicker.listener.ImagePickerViewModule;
import com.renj.imagepicker.listener.OnResultCallBack;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-25   11:57
 * <p>
 * 描述：辅助数据
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
class CallAndViewModule {
    OnResultCallBack onResultCallBack; // 结果回调
    ImagePickerViewModule imagePickerViewModule; // 各种自定义页面

    CallAndViewModule(OnResultCallBack onResultCallBack, ImagePickerViewModule imagePickerViewModule) {
        this.onResultCallBack = onResultCallBack;
        this.imagePickerViewModule = imagePickerViewModule;
    }
}
