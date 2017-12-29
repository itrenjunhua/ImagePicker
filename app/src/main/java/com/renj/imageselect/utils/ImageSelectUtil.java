package com.renj.imageselect.utils;

import com.renj.imageselect.activity.ImageSelectActivity;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-29   16:32
 * <p>
 * 描述：图片选择框操作类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageSelectUtil {
    private OnResultCallBack onResultCallBack;

    private ImageSelectUtil() {
    }

    public static ImageSelectActivity.ImageSelectObservable create() {
        return ImageSelectActivity.create();
    }
}
