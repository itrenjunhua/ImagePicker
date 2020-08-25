package com.renj.selecttest.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.renj.imagepicker.custom.ImagePickerCropLayout;
import com.renj.imagepicker.listener.ImagePickerViewModule;
import com.renj.imagepicker.listener.OnResultCallBack;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.ImagePickerParams;
import com.renj.imagepicker.ImagePickerUtils;
import com.renj.selecttest.R;
import com.renj.selecttest.adapter.ImageShowAdapter;
import com.renj.selecttest.custom.CustomImageCropMultiView;
import com.renj.selecttest.utils.Utils;

import java.util.List;

import butterknife.BindView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2019-04-26   11:02
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class CustomCropMultiActivity extends BaseActivity {
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.gv_images)
    GridView gvImages;
    private ImageShowAdapter imageShowAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.crop_multi_activity;
    }

    @Override
    protected void initView() {
        imageShowAdapter = new ImageShowAdapter(this);
        gvImages.setAdapter(imageShowAdapter);

        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreImages();
            }
        });
    }

    private void moreImages() {
        String textViewContent = Utils.getTextViewContent(etCount);
        if (Utils.isEmpty(textViewContent)) {
            Toast.makeText(CustomCropMultiActivity.this, "请输入张数", Toast.LENGTH_SHORT).show();
            return;
        }
        int imageNum = Utils.parseInteger(textViewContent);
        ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .selectCount(imageNum)
                .isShowCamera(true)
                .isCrop(true)
                .width(240)
                .height(320)
                .isOvalCrop(true)
                .cropBorderWidth(0.5f)
                .maxScale(6f)
                .minScale(0.8f)
                .isContinuityEnlarge(false)
                .maskColor(Color.parseColor("#80000000"))
                .cropBorderColor(Color.parseColor("#ffffff"))
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImagePickerModel> resultList) {
                        imageShowAdapter.setDatas(resultList);
                    }
                })
                .build();
        ImagePickerUtils.start(this, imagePickerParams, new ImagePickerViewModule() {
            @Override
            public ImagePickerCropLayout onCreateImagePickerCropMultiView(AppCompatActivity activity) {
                return new CustomImageCropMultiView(activity);
            }
        });
    }
}
