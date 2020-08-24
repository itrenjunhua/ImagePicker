package com.renj.selecttest.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renj.imagepicker.listener.OnResultCallBack;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.ImagePickerParams;
import com.renj.imagepicker.ImagePickerUtils;
import com.renj.selecttest.R;
import com.renj.selecttest.utils.ImageLoaderManager;

import java.util.List;

import butterknife.BindView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2019-04-26   11:02
 * <p>
 * 描述：裁剪单张图片
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ClipSingleActivity extends BaseActivity {
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
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImagePickerModel> resultList) {
                        ImageLoaderManager.loadImageForFile(resultList.get(0).path, ivClipResult);
                    }
                })
                .build();
        ImagePickerUtils.start(this, imagePickerParams);
    }

}
