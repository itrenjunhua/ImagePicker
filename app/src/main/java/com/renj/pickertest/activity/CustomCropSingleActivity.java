package com.renj.pickertest.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renj.imagepicker.ImagePageStyle;
import com.renj.imagepicker.ImagePickerParams;
import com.renj.imagepicker.ImagePickerUtils;
import com.renj.imagepicker.custom.ImagePickerCropLayout;
import com.renj.imagepicker.custom.ImagePickerLayout;
import com.renj.imagepicker.listener.ImagePickerViewModule;
import com.renj.imagepicker.listener.OnResultCallBack;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.pickertest.R;
import com.renj.pickertest.custom.CustomImageCropSingleView;
import com.renj.pickertest.custom.CustomImagePickerView;
import com.renj.pickertest.utils.ImageLoaderManager;

import java.util.List;

import butterknife.BindView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2019-04-26   11:02
 * <p>
 * 描述：使用自定义样式裁剪单张图片
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class CustomCropSingleActivity extends BaseActivity {
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.iv_crop_result)
    ImageView ivCropResult;

    @Override
    protected int getLayoutId() {
        return R.layout.crop_single_activity;
    }

    @Override
    protected void initView() {
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleImage();
            }
        });
    }

    private void singleImage() {
        ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .maxCount(1)
                .isShowCamera(true)
                .isCrop(true)
                .width(200)
                .height(200)
                .isOvalCrop(false)
                .cropBorderWidth(2)
                .maskColor(Color.parseColor("#88000000"))
                .cropBorderColor(Color.parseColor("#ff0000"))
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImagePickerModel> resultList) {
                        ImageLoaderManager.loadImageForFile(resultList.get(0).path, ivCropResult);
                    }
                })
                .build();
        ImagePageStyle pageStyle = new ImagePageStyle.Builder()
                .statusBarDark(true)
                .statusBarColor(Color.WHITE)
                .build();
        ImagePickerUtils.start(this, pageStyle, imagePickerParams, new ImagePickerViewModule() {
            @Override
            public ImagePickerLayout onCreateImagePickerView(AppCompatActivity activity) {
                return new CustomImagePickerView(activity);
            }

            @Override
            public ImagePickerCropLayout onCreateImagePickerCropSingleView(AppCompatActivity activity) {
                // CustomImageCropSingleView extends ImagePickerCropLayout
                return new CustomImageCropSingleView(activity);
            }
        });
    }
}
