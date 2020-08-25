package com.renj.imagepicker;

import com.renj.imagepicker.listener.ImagePickerViewModule;
import com.renj.imagepicker.listener.OnResultCallBack;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-07-24   16:41
 * <p>
 * 描述：图片加载工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
class RImagePickerHelp {
    private static OnResultCallBack onResultCallBack; // 结果回调
    private static ImagePickerViewModule imagePickerViewModule; // 各种自定义页面

    private RImagePickerHelp() {
    }

    static ImagePickerViewModule getImagePickerViewModule() {
        return imagePickerViewModule;
    }

    static void setImagePickerViewModule(ImagePickerViewModule imagePickerViewModule) {
        RImagePickerHelp.imagePickerViewModule = imagePickerViewModule;
    }

    /************************** 选择/裁剪结果回调  **************************/
    static OnResultCallBack getOnResultCallBack() {
        return onResultCallBack;
    }

    static void setOnResultCallBack(OnResultCallBack onResultCallBack) {
        RImagePickerHelp.onResultCallBack = onResultCallBack;
    }
}
