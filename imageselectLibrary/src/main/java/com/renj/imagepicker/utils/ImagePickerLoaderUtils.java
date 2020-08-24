package com.renj.imagepicker.utils;

import android.widget.ImageView;

import com.renj.imagepicker.listener.ImagePickerLoaderModule;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-24   17:44
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePickerLoaderUtils {
    /************************** 图片加载器  **************************/
    private static ImagePickerLoaderModule imagePickerLoaderModule; // 图片加载器

    public static void setImagePickerLoaderModule(ImagePickerLoaderModule imagePickerLoaderModule) {
        ImagePickerLoaderUtils.imagePickerLoaderModule = imagePickerLoaderModule;
    }

    /**
     * 加载图片方法
     *
     * @param path      图片路径
     * @param imageView {@link ImageView} 控件
     */
    public static void loadImage(String path, ImageView imageView) {
        if (imagePickerLoaderModule != null)
            imagePickerLoaderModule.loadImage(path, imageView);
    }
}
