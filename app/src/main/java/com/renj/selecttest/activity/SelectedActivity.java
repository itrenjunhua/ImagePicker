package com.renj.selecttest.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.renj.imageselect.listener.OnResultCallBack;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.ImageSelectUtils;
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
 * 创建时间：2019-04-26   11:01
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class SelectedActivity extends BaseActivity {
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.gv_images)
    GridView gvImages;
    private ImageShowAdapter imageShowAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.selected_activity;
    }

    @Override
    protected void initView() {
        imageShowAdapter = new ImageShowAdapter(this);
        gvImages.setAdapter(imageShowAdapter);

        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage();
            }
        });
    }

    private void selectedImage() {
        int imageNum = Integer.parseInt(etCount.getText().toString());
        ImageParamsConfig imageParamsConfig = new ImageParamsConfig
                .Builder()
                .selectCount(imageNum)
                .isShowCamera(true)
                .isContinuityEnlarge(false)
                .isClip(false)
                .build();
        ImageSelectUtils.newInstance().create()
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