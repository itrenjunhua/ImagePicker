package com.renj.imagepicker.activity;

import com.renj.imagepicker.model.ImageModel;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-22   14:50
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface IImagePickerPage extends IImagePage{
    void openCamera();

    void confirmPickerFinish(List<ImageModel> imagePickerList);
}
