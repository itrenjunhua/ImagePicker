package com.renj.selecttest.activity;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.renj.imageselect.listener.OnResultCallBack;
import com.renj.imageselect.listener.OnSelectedImageChange;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.ImageSelectUtils;
import com.renj.imageselect.utils.CommonUtils;
import com.renj.selecttest.R;
import com.renj.selecttest.adapter.ImageShowAdapter;
import com.renj.selecttest.utils.Utils;

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
public class SelectedMyActivity extends BaseActivity {
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

        tvSelect.setOnClickListener(v -> selectedImage());
    }

    private void selectedImage() {
        String textViewContent = Utils.getTextViewContent(etCount);
        if (Utils.isEmpty(textViewContent)) {
            Toast.makeText(SelectedMyActivity.this, "请输入张数", Toast.LENGTH_SHORT).show();
            return;
        }
        int imageNum = Utils.parseInteger(textViewContent);
        ImageParamsConfig imageParamsConfig = new ImageParamsConfig
                .Builder()
                .selectCount(imageNum)
                .isShowCamera(true)
                .isContinuityEnlarge(false)
                .isClip(false)
                .build();
        ImageSelectUtils.getInstance().create()
                .selectedLayoutId(R.layout.my_selected_layout)
                .selectItemCameraLayoutId(R.layout.my_image_select_camera_item)
                .selectItemImageLayoutId(R.layout.my_image_select_item)
                .onSelectedImageChange(new OnSelectedImageChange() {
                    @Override
                    public void onDefault(@NonNull TextView confirmView, @NonNull TextView cancelView, int selectedCount, int totalCount) {
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }

                    @Override
                    public void onSelectedChange(@NonNull TextView confirmView, @NonNull TextView cancelView, @NonNull ImageModel imageModel, boolean isSelected,
                                                 @NonNull List<ImageModel> selectedList, int selectedCount, int totalCount) {
                        Log.i("SelectedMyActivity", "imageModel = [" + imageModel + "], isSelected = [" + isSelected + "], selectedList = [" + selectedList + "], selectedCount = [" + selectedCount + "], totalCount = [" + totalCount + "]");
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }
                })
                .imageParamsConfig(imageParamsConfig)
                .openImageSelectPage(this)
                .onResult(resultList -> imageShowAdapter.setDatas(resultList));
    }
}
