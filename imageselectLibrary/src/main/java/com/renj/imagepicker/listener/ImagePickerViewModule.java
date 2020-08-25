package com.renj.imagepicker.listener;

import android.support.v7.app.AppCompatActivity;

import com.renj.imagepicker.custom.DefaultImageCropMultiLayout;
import com.renj.imagepicker.custom.DefaultImageCropSingleLayout;
import com.renj.imagepicker.custom.DefaultImagePickerLayout;
import com.renj.imagepicker.custom.ImagePickerCropLayout;
import com.renj.imagepicker.custom.ImagePickerLayout;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-25   09:24
 * <p>
 * 描述：需要自定义样式时传递的对象,重写相关方法
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePickerViewModule {
    /**
     * 返回图片选择页面样式
     *
     * @param activity
     * @return
     */
    public ImagePickerLayout onCreateImagePickerView(AppCompatActivity activity) {
        return new DefaultImagePickerLayout(activity);
    }

    /**
     * 返回单张图片裁剪页面样式
     *
     * @param activity
     * @return
     */
    public ImagePickerCropLayout onCreateImagePickerCropSingleView(AppCompatActivity activity) {
        return new DefaultImageCropSingleLayout(activity);
    }

    /**
     * 返回多张图片裁剪页面样式
     *
     * @param activity
     * @return
     */
    public ImagePickerCropLayout onCreateImagePickerCropMultiView(AppCompatActivity activity) {
        return new DefaultImageCropMultiLayout(activity);
    }
}
