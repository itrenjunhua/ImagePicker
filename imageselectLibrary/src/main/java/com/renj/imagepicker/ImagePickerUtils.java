package com.renj.imagepicker;

import android.content.Context;
import android.support.annotation.NonNull;

import com.renj.imagepicker.listener.ImagePickerLoaderModule;
import com.renj.imagepicker.listener.ImagePickerViewModule;
import com.renj.imagepicker.utils.ImagePickerLoaderUtils;
import com.renj.imagepicker.utils.RImageFileUtils;

import java.io.File;

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
     * @param imagePickerLoaderModule {@link ImagePickerLoaderModule} 对象，图片选择框架使用配置的框架加载图片
     */
    public static void init(@NonNull ImagePickerLoaderModule imagePickerLoaderModule) {
        RImageFileUtils.setImageSavePath(null);
        ImagePickerLoaderUtils.setImagePickerLoaderModule(imagePickerLoaderModule);
    }

    /**
     * 初始化方法
     *
     * @param imageSavePath           文件保存路径
     * @param imagePickerLoaderModule {@link ImagePickerLoaderModule} 对象，图片选择框架使用配置的框架加载图片
     */
    public static void init(String imageSavePath, @NonNull ImagePickerLoaderModule imagePickerLoaderModule) {
        RImageFileUtils.setImageSavePath(imageSavePath);
        ImagePickerLoaderUtils.setImagePickerLoaderModule(imagePickerLoaderModule);
    }

    /**
     * 初始化方法
     *
     * @param imageSavePath           文件保存路径
     * @param imagePickerLoaderModule {@link ImagePickerLoaderModule} 对象，图片选择框架使用配置的框架加载图片
     */
    public static void init(File imageSavePath, @NonNull ImagePickerLoaderModule imagePickerLoaderModule) {
        RImageFileUtils.setImageSaveFile(imageSavePath);
        ImagePickerLoaderUtils.setImagePickerLoaderModule(imagePickerLoaderModule);
    }

    /**
     * 开始图片选择裁剪
     *
     * @return
     */
    public static void start(Context context, ImagePickerParams paramsConfig) {
        start(context, paramsConfig, new ImagePickerViewModule());
    }

    /**
     * 开始图片选择裁剪
     *
     * @return
     */
    public static void start(Context context, ImagePickerParams paramsConfig, ImagePickerViewModule imagePickerViewModule) {
        if (imagePickerViewModule == null) imagePickerViewModule = new ImagePickerViewModule();
        RImagePickerHelp.setImagePickerViewModule(imagePickerViewModule);
        ImagePickerActivity.open(context, paramsConfig);
    }
}
