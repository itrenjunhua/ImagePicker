package com.renj.imagepicker.custom;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.activity.IImagePickerPage;
import com.renj.imagepicker.adapter.ImagePickerAdapter;
import com.renj.imagepicker.model.DefaultConfigData;
import com.renj.imagepicker.model.FolderModel;
import com.renj.imagepicker.model.ImageModel;
import com.renj.imagepicker.model.ImagePickerParams;
import com.renj.imagepicker.utils.ConfigUtils;
import com.renj.imagepicker.weight.ImageMenuDialog;

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
public class ImagePickerView extends FrameLayout {

    private GridView gvImages;
    private TextView tvSelectMenu;
    private TextView tvCancelSelect;
    private TextView tvConfirmSelect;

    private ImagePickerAdapter imagePickerAdapter; // 图片展示的适配器
    private ImageMenuDialog imageMenuDialog; // 图片目录选择Dialog

    protected IImagePickerPage iImagePickerPage;
    protected ImagePickerParams imagePickerParams;

    public ImagePickerView(@NonNull Context context) {
        this(context, null);
    }

    public ImagePickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImagePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View imagePickerView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        this.addView(imagePickerView);

        initView(imagePickerView);
        initListener();
    }

    @LayoutRes
    protected int getLayoutId() {
        return R.layout.custom_image_picker_layout;
    }

    protected void initView(View imagePickerView) {
        gvImages = imagePickerView.findViewById(R.id.gv_images);
        tvSelectMenu = imagePickerView.findViewById(R.id.tv_select_menu);
        tvCancelSelect = imagePickerView.findViewById(R.id.tv_cancel_select);
        tvConfirmSelect = imagePickerView.findViewById(R.id.tv_confirm_select);

        imagePickerAdapter = new ImagePickerAdapter(getContext(), DefaultConfigData.SELECTED_IMAGE_ITEM_CAMERA_LAYOUT,
                DefaultConfigData.SELECTED_IMAGE_ITEM_IMAGE_LAYOUT);
        gvImages.setAdapter(imagePickerAdapter);

        imageMenuDialog = new ImageMenuDialog(getContext());
        setItemListener();
    }

    /**
     * 设置条目相关监听
     */
    private void setItemListener() {
        // 目录监听
        imageMenuDialog.setMenuClickListener(new ImageMenuDialog.MenuClickListener() {
            @Override
            public void menuClick(FolderModel folderModel) {
                if (ConfigUtils.isShowLogger())
                    ConfigUtils.i("选中图片目录：" + folderModel);
                tvSelectMenu.setText(folderModel.name);
                imagePickerAdapter.setImageModels(folderModel.folders);
            }
        });

        // 图片点击监听
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectCount = imagePickerParams.getSelectCount();
                if (imagePickerParams.isShowCamera() && position == 0) {
                    if (imagePickerAdapter.getCheckImages().size() >= selectCount) {
                        if (ConfigUtils.isShowLogger())
                            ConfigUtils.e("最多选择" + selectCount + "张图片");
                        ConfigUtils.showToast(getContext(), "最多选择" + selectCount + "张图片");
                    } else {
                        if (ConfigUtils.isShowLogger())
                            ConfigUtils.i("打开相机进行拍照");
                        iImagePickerPage.openCamera();
                    }
                    return;
                }

                Object itemData = parent.getItemAtPosition(position);
                if (itemData instanceof ImageModel) {
                    // 判断是否选择单张还是多张
                    if (selectCount > 1) {
                        selectMore(position, (ImageModel) itemData);
                    } else {
                        ImageModel imageModel = (ImageModel) itemData;
                        selectSingle(imageModel);
                    }
                }
            }
        });
    }

    /**
     * 处理相机照完之后的结果
     *
     * @param imageModel
     */
    public void handlerCameraResult(@NonNull ImageModel imageModel) {
        if (ConfigUtils.isShowLogger())
            ConfigUtils.i("拍照成功，处理结果中");
        if (imagePickerParams.getSelectCount() == 1) {
            // 如果是单张，判断是否需要裁剪或直接返回结果
            selectSingle(imageModel);
        } else {
            // 多张时，也直接判断是否需要裁剪然后返回
            List<ImageModel> checkImages = imagePickerAdapter.getCheckImages();
            checkImages.add(imageModel);
            if (imagePickerParams.isCrop()) {

            } else {
                if (ConfigUtils.isShowLogger())
                    ConfigUtils.i("拍照成功，直接返回已选择和拍照图片");
                iImagePickerPage.confirm(checkImages);
            }
        }
    }

    /**
     * 选择单张图片完成
     *
     * @param imageModel
     */
    private void selectSingle(@NonNull ImageModel imageModel) {
        if (imagePickerParams.isCrop()) {
            if (ConfigUtils.isShowLogger())
                ConfigUtils.i("单张图片裁剪");
        } else {
            ArrayList<ImageModel> selectResults = new ArrayList<>();
            selectResults.add(imageModel);

            if (ConfigUtils.isShowLogger())
                ConfigUtils.i("单张图片选择完成");
            iImagePickerPage.confirm(selectResults);
        }
    }

    /**
     * 选择多张图片时点击图片操作
     *
     * @param position
     */
    private void selectMore(int position, ImageModel imageModel) {
        boolean isSelected = imagePickerAdapter.addOrClearCheckedPosition(position);
        if (ConfigUtils.isShowLogger()) {
            ConfigUtils.i("图片选择：" + imageModel + " 是否选中：" + isSelected);
            ConfigUtils.i("已选择图片数：" + imagePickerAdapter.getCheckImages().size());
        }
        tvConfirmSelect.setText("(" + imagePickerAdapter.getCheckImages().size() + " / " + imagePickerParams.getSelectCount() + ") 确定");
    }

    protected void initListener() {
        tvCancelSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });

        tvConfirmSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirm();
            }
        });

        tvSelectMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowMenu();
            }
        });
    }

    protected void onCancel() {
        iImagePickerPage.cancel();
    }

    protected void onConfirm() {
        iImagePickerPage.confirm(imagePickerAdapter.getCheckImages());
    }

    private void onShowMenu() {
        imageMenuDialog.show();
    }

    public final void setImagePickerOperator(IImagePickerPage iImagePickerPage,
                                             ImagePickerParams imagePickerParams) {
        this.iImagePickerPage = iImagePickerPage;
        this.imagePickerParams = imagePickerParams;

        tvConfirmSelect.setText("(0 / " + imagePickerParams.getSelectCount() + ") 确定");
    }

    public void onLoadImageFinish(List<ImageModel> imageModels, List<FolderModel> folderModelList) {
        imagePickerAdapter.setImageModels(imageModels);
        imageMenuDialog.setMenuData(folderModelList);

        imagePickerAdapter.setMaxCount(imagePickerParams.getSelectCount());
        imagePickerAdapter.isOpenCamera(imagePickerParams.isShowCamera());
    }
}
