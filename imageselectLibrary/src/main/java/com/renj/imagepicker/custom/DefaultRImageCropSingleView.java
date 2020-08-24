package com.renj.imagepicker.custom;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.activity.IImageCropPage;
import com.renj.imagepicker.model.ImageModel;
import com.renj.imagepicker.model.ImagePickerParams;
import com.renj.imagepicker.utils.ConfigUtils;
import com.renj.imagepicker.weight.ImageCropView;

import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-21   17:25
 * <p>
 * 描述：自定义图片选择页面
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class DefaultRImageCropSingleView extends RImageCropView {

    private LinearLayout cropLayout;
    private TextView tvCancel;
    private TextView tvCrop;
    private ImageCropView imageCropView;

    public DefaultRImageCropSingleView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.default_image_crop_single_layout;
    }

    @Override
    protected void initView(View singleCropView) {
        cropLayout = singleCropView.findViewById(R.id.crop_layout);
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
                imageCropView.cut(new ImageCropView.CutListener() {
                    @Override
                    public void cutFinish(final ImageModel imageModel) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<ImageModel> selectResults = new ArrayList<>();
                                selectResults.add(imageModel);

                                imageCropPage.closeLoading();
                                if (ConfigUtils.isShowLogger())
                                    ConfigUtils.i("单张裁剪图片完成");
                                imageCropPage.confirmCropFinish(selectResults);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onParamsInitFinish(IImageCropPage iImagePickerPage,
                                      ImagePickerParams imagePickerParams,
                                      List<ImageModel> imagePickerList) {
        imageCropView.setCropViewParams(imagePickerParams);
        imageCropView.setImage(imagePickerList.get(0).path);
    }

}
