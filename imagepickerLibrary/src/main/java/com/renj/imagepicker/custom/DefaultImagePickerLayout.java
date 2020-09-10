package com.renj.imagepicker.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.renj.imagepicker.ImagePickerParams;
import com.renj.imagepicker.R;
import com.renj.imagepicker.custom.adapter.ImagePickerAdapter;
import com.renj.imagepicker.listener.IImagePickerPage;
import com.renj.imagepicker.model.ImagePickerFolderModel;
import com.renj.imagepicker.model.ImagePickerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-21   17:25
 * <p>
 * 描述：默认图片选择控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class DefaultImagePickerLayout extends ImagePickerLayout {

    private RecyclerView rvImages;
    private TextView tvSelectMenu;
    private TextView tvCancelSelect;
    private TextView tvConfirmSelect;

    private ImagePickerAdapter imagePickerAdapter; // 图片展示的适配器
    private ImagePickerMenuDialog imagePickerMenuDialog; // 图片目录选择Dialog

    public DefaultImagePickerLayout(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.default_image_picker_layout;
    }

    @Override
    protected void initView(View imagePickerView) {
        rvImages = imagePickerView.findViewById(R.id.rv_images);
        tvSelectMenu = imagePickerView.findViewById(R.id.tv_select_menu);
        tvCancelSelect = imagePickerView.findViewById(R.id.tv_cancel_select);
        tvConfirmSelect = imagePickerView.findViewById(R.id.tv_confirm_select);

        imagePickerAdapter = new ImagePickerAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        rvImages.setLayoutManager(gridLayoutManager);
        rvImages.setAdapter(imagePickerAdapter);

        imagePickerMenuDialog = new ImagePickerMenuDialog(getContext());
    }

    @Override
    protected void initListener() {
        tvCancelSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iImagePickerPage.cancel();
            }
        });

        tvConfirmSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iImagePickerPage.confirmPickerFinish(imagePickerAdapter.getCheckImages());
            }
        });

        tvSelectMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerMenuDialog.show();
            }
        });

        setItemListener();
    }

    /**
     * 设置条目相关监听
     */
    private void setItemListener() {
        // 目录监听
        imagePickerMenuDialog.setMenuClickListener(new ImagePickerMenuDialog.MenuClickListener() {
            @Override
            public void menuClick(ImagePickerFolderModel imagePickerFolderModel) {
                tvSelectMenu.setText(imagePickerFolderModel.name);
                imagePickerAdapter.setImagePickerModels(imagePickerFolderModel.folders);
            }
        });

        // 图片点击监听
        imagePickerAdapter.setOnItemClickListener(new ImagePickerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImagePickerModel imagePickerModel, int position) {
                int selectCount = imagePickerParams.getMaxCount();
                if ((imagePickerModel == null) || (imagePickerParams.isShowCamera() && position == 0)) {
                    if (imagePickerAdapter.getCheckImages().size() >= selectCount) {
                        UIUtils.showToast(getContext(), "最多选择" + selectCount + "张图片");
                    } else {
                        iImagePickerPage.openCamera();
                    }
                    return;
                }
                // 判断是否选择单张还是多张
                if (selectCount > 1) {
                    imagePickerAdapter.addOrClearCheckedPosition(position);
                    tvConfirmSelect.setText("(" + imagePickerAdapter.getCheckImages().size() + " / " + imagePickerParams.getMaxCount() + ") 确定");
                } else {
                    ArrayList<ImagePickerModel> selectResults = new ArrayList<>();
                    selectResults.add(imagePickerModel);
                    iImagePickerPage.confirmPickerFinish(selectResults);
                }
            }
        });
    }

    /**
     * 处理相机照完之后的结果
     *
     * @param imagePickerModel
     */
    @Override
    public void handlerCameraResult(@NonNull ImagePickerModel imagePickerModel) {
        if (imagePickerParams.getMaxCount() == 1) {
            // 如果是单张，判断是否需要裁剪或直接返回结果
            ArrayList<ImagePickerModel> selectResults = new ArrayList<>();
            selectResults.add(imagePickerModel);
            iImagePickerPage.confirmPickerFinish(selectResults);
        } else {
            // 多张时，也直接判断是否需要裁剪然后返回
            List<ImagePickerModel> selectResults = imagePickerAdapter.getCheckImages();
            selectResults.add(imagePickerModel);
            iImagePickerPage.confirmPickerFinish(selectResults);
        }
    }

    @Override
    protected void onParamsInitFinish(IImagePickerPage iImagePickerPage, ImagePickerParams imagePickerParams) {
        if (imagePickerParams.getMaxCount() == 1) {
            tvConfirmSelect.setVisibility(GONE);
        } else {
            tvConfirmSelect.setVisibility(VISIBLE);
            tvConfirmSelect.setText("(0 / " + imagePickerParams.getMaxCount() + ") 确定");
        }
    }

    @Override
    public void onLoadImageFinish(List<ImagePickerModel> imagePickerModels, List<ImagePickerFolderModel> imagePickerFolderModelList) {
        imagePickerAdapter.setImagePickerModels(imagePickerModels);
        imagePickerMenuDialog.setMenuData(imagePickerFolderModelList);

        imagePickerAdapter.setMaxCount(imagePickerParams.getMaxCount());
        imagePickerAdapter.isOpenCamera(imagePickerParams.isShowCamera());
    }
}
