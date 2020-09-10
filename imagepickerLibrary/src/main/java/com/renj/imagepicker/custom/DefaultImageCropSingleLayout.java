package com.renj.imagepicker.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.listener.IImageCropPage;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.ImagePickerParams;
import com.renj.imagepicker.weight.IPImageCropView;

import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-21   17:25
 * <p>
 * 描述：默认单张图片裁剪控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class DefaultImageCropSingleLayout extends ImagePickerCropLayout {

    private TextView tvCancel;
    private TextView tvCrop;
    private IPImageCropView imageCropView;

    public DefaultImageCropSingleLayout(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.default_image_crop_single_layout;
    }

    @Override
    protected void initView(View singleCropView) {
        tvCancel = singleCropView.findViewById(R.id.tv_cancel);
        tvCrop = singleCropView.findViewById(R.id.tv_crop);
        imageCropView = singleCropView.findViewById(R.id.image_crop_view);
    }

    @Override
    protected void initListener() {
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCropPage.cancel();
            }
        });

        tvCrop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCropPage.showLoading();
                imageCropView.cut(new IPImageCropView.CutListener() {
                    @Override
                    public void cutFinish(final ImagePickerModel imagePickerModel) {
                        ArrayList<ImagePickerModel> selectResults = new ArrayList<>();
                        selectResults.add(imagePickerModel);

                        imageCropPage.closeLoading();
                        imageCropPage.confirmCropFinish(selectResults);
                    }
                });
            }
        });
    }

    @Override
    protected void onParamsInitFinish(IImageCropPage iImagePickerPage,
                                      ImagePickerParams imagePickerParams,
                                      List<ImagePickerModel> imagePickerList) {
        imageCropView.setCropViewParams(imagePickerParams);
        imageCropView.setImage(imagePickerList.get(0).path);
    }

}
