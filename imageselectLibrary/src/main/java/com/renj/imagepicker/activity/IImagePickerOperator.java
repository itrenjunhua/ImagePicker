package com.renj.imagepicker.activity;

import com.renj.imagepicker.model.FolderModel;
import com.renj.imagepicker.model.ImageModel;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-21   19:37
 * <p>
 * 描述：选择图片操作方法
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface IImagePickerOperator {
    interface PageCallPickerView {
        void onLoadImageFinish(List<FolderModel> folderModelList);
    }

    interface PickerViewCallPage {
        void cancel();

        void confirm();

        void showMenu();

        void startCrop(List<ImageModel> imageModelList);
    }

}
