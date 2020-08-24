package com.renj.selecttest.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renj.imagepicker.listener.OnCropImageChange;
import com.renj.imagepicker.listener.OnResultCallBack;
import com.renj.imagepicker.model.ImageModel;
import com.renj.imagepicker.model.ImagePickerParams;
import com.renj.imagepicker.utils.ImagePickerUtils;
import com.renj.selecttest.R;
import com.renj.selecttest.utils.ImageLoaderManager;

import java.util.List;

import butterknife.BindView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-04-26   11:02
 * <p>
 * 描述：使用自定义样式裁剪单张图片
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ClipSingleMyActivity extends BaseActivity {
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.iv_clip_result)
    ImageView ivClipResult;

    @Override
    protected int getLayoutId() {
        return R.layout.clip_single_activity;
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
                .selectCount(1)
                .isShowCamera(true)
                .isCrop(true)
                .width(200)
                .height(200)
                .isOvalCrop(false)
                .cropBorderWidth(2)
                .maskColor(Color.parseColor("#88000000"))
                .cropBorderColor(Color.parseColor("#ff0000"))
                .onCropImageChange(new OnCropImageChange() {
                    @Override
                    public void onCropChange(@NonNull TextView clipView, @NonNull TextView cancelView,
                                             @NonNull ImageModel imageModel, @NonNull List<ImageModel> cropResultList,
                                             boolean isOvalCrop, int cropCount, int totalCount) {
                        // clipView.setText(clipCount + "/" + totalCount + "裁剪");
                        Log.i("ClipSingleMyActivity", "imageModel = [" + imageModel + "], clipResultList = [" + cropResultList + "], isCircleClip = [" + isOvalCrop + "], clipCount = [" + cropCount + "], totalCount = [" + totalCount + "]");
                    }
                })
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        ImageLoaderManager.loadImageForFile(resultList.get(0).path, ivClipResult);
                    }
                })
                .build();
        ImagePickerUtils.start(this, imagePickerParams);
    }
}
