package com.renj.imagepicker.utils;

import android.content.Context;

import com.renj.imagepicker.activity.ImagePickerActivity;
import com.renj.imagepicker.listener.ImageLoaderModule;
import com.renj.imagepicker.model.ImagePickerParams;
import com.renj.imagepicker.model.ImagePickerConfig;

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
public class ImagePickerUtils {
    /**
     * 初始化方法
     *
     * @param imageLoaderModule {@link ImageLoaderModule} 对象，图片选择框架使用配置的框架加载图片
     */
    public static void init(ImageLoaderModule imageLoaderModule) {
        init(null, imageLoaderModule);
    }

    /**
     * 初始化方法
     *
     * @param imagePickerConfig {@link ImagePickerConfig} 对象，配置全局参数
     * @param imageLoaderModule {@link ImageLoaderModule} 对象，图片选择框架使用配置的框架加载图片
     */
    public static void init(ImagePickerConfig imagePickerConfig, ImageLoaderModule imageLoaderModule) {
        if (imagePickerConfig == null)
            imagePickerConfig = new ImagePickerConfig.Builder().build();
        ConfigUtils.initParams(imagePickerConfig);
        ImagePickerHelp.getInstance().setImageLoaderModule(imageLoaderModule);
    }

    /**
     * 开始图片选择裁剪
     *
     * @return
     */
    public static void start(Context context, ImagePickerParams paramsConfig) {
        ImagePickerActivity.open(context, paramsConfig);
    }
}
