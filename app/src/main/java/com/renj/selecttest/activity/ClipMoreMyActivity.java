package com.renj.selecttest.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.renj.imageselect.listener.OnClipImageChange;
import com.renj.imageselect.listener.OnResultCallBack;
import com.renj.imageselect.listener.OnSelectedImageChange;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.ImageSelectUtils;
import com.renj.imageselect.utils.Logger;
import com.renj.selecttest.R;
import com.renj.selecttest.adapter.ImageShowAdapter;

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
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ClipMoreMyActivity extends BaseActivity {
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.gv_images)
    GridView gvImages;
    private ImageShowAdapter imageShowAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.clip_more_activity;
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
        int imageNum = Integer.parseInt(etCount.getText().toString());
        ImageParamsConfig imageParamsConfig = new ImageParamsConfig
                .Builder()
                .selectCount(imageNum)
                .isShowCamera(true)
                .isClip(true)
                .width(600)
                .height(800)
                .isCircleClip(true)
                .clipBorderWidth(0.5f)
                .maxScale(6f)
                .minScale(0.8f)
                .isContinuityEnlarge(false)
                .maskColor(Color.parseColor("#80000000"))
                .clipBorderColor(Color.parseColor("#ffffff"))
                .build();
        ImageSelectUtils.newInstance().create()
                .selectedLayoutId(R.layout.my_selected_layout)
                .clipMoreLayoutId(R.layout.my_clip_more_layout)
                .onSelectedImageChange(new OnSelectedImageChange() {
                    @Override
                    public void onDefault(@NonNull TextView confirmView, @NonNull TextView cancelView, int selectedCount, int totalCount) {
                        Logger.i("selectedCount = [" + selectedCount + "], totalCount = [" + totalCount + "]");
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }

                    @Override
                    public void onSelectedChange(@NonNull TextView confirmView, @NonNull TextView cancelView, @NonNull ImageModel imageModel, boolean isSelected,
                                                 @NonNull List<ImageModel> selectedList, int selectedCount, int totalCount) {
                        Logger.i("imageModel = [" + imageModel + "], isSelected = [" + isSelected + "], selectedList = [" + selectedList + "], selectedCount = [" + selectedCount + "], totalCount = [" + totalCount + "]");
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }
                })
                .onClipImageChange(new OnClipImageChange() {
                    @Override
                    public void onClipChange(@NonNull TextView clipView, @NonNull TextView cancelView,
                                             @NonNull ImageModel imageModel, @NonNull List<ImageModel> clipResultList,
                                             boolean isCircleClip, int clipCount, int totalCount) {
                        Logger.i("imageModel = [" + imageModel + "], clipResultList = [" + clipResultList + "], isCircleClip = [" + isCircleClip + "], clipCount = [" + clipCount + "], totalCount = [" + totalCount + "]");
                    }
                })
                .imageParamsConfig(imageParamsConfig)
                .openImageSelectPage(this)
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        imageShowAdapter.setDatas(resultList);
                    }
                });
    }
}
