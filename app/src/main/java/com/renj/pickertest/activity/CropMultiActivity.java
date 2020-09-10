package com.renj.pickertest.activity;


import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.renj.imagepicker.ImagePickerParams;
import com.renj.imagepicker.ImagePickerUtils;
import com.renj.imagepicker.listener.OnResultCallBack;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.pickertest.R;
import com.renj.pickertest.adapter.ImageShowAdapter;
import com.renj.pickertest.utils.Utils;

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
public class CropMultiActivity extends BaseActivity {
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.rv_images)
    RecyclerView rvImages;
    private ImageShowAdapter imageShowAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.crop_multi_activity;
    }

    @Override
    protected void initView() {
        imageShowAdapter = new ImageShowAdapter(this);
        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        rvImages.setAdapter(imageShowAdapter);

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
            Toast.makeText(CropMultiActivity.this, "请输入张数", Toast.LENGTH_SHORT).show();
            return;
        }
        int imageNum = Utils.parseInteger(textViewContent);
        ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .maxCount(imageNum)
                .isShowCamera(true)
                .isCrop(true)
                .width(300)
                .height(300)
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
                        imageShowAdapter.setItemDataList(resultList);
                    }
                })
                .build();
        ImagePickerUtils.start(this, imagePickerParams);
    }
}
