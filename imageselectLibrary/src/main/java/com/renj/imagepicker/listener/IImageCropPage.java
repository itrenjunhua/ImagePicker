package com.renj.imagepicker.listener;

import com.renj.imagepicker.model.ImagePickerModel;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-24   11:39
 * <p>
 * 描述：图片裁剪页面公共操作
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface IImageCropPage extends IImagePage{
    void confirmCropFinish(List<ImagePickerModel> imagePickerList);
}
