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
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePickerViewModule {
    public ImagePickerLayout onCreateImagePickerView(AppCompatActivity activity) {
        return new DefaultImagePickerLayout(activity);
    }

    public ImagePickerCropLayout onCreateImagePickerCropSingleView(AppCompatActivity activity) {
        return new DefaultImageCropSingleLayout(activity);
    }

    public ImagePickerCropLayout onCreateImagePickerCropMultiView(AppCompatActivity activity) {
        return new DefaultImageCropMultiLayout(activity);
    }
}
