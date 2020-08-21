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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.activity.IImagePickerOperator;
import com.renj.imagepicker.adapter.ImagePickerAdapter;
import com.renj.imagepicker.model.DefaultConfigData;
import com.renj.imagepicker.model.ImageModel;
import com.renj.imagepicker.model.ImagePickerParams;
import com.renj.imagepicker.weight.ImageMenuDialog;

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
public class ImagePickerView extends FrameLayout implements IImagePickerOperator.PageCallPickerView {

    private GridView gvImages;
    private TextView tvSelectMenu;
    private TextView tvCancelSelect;
    private TextView tvConfirmSelect;

    private IImagePickerOperator.PickerViewCallPage iImagePickerOperator;
    private ImagePickerParams imagePickerParams;
    private ImagePickerAdapter imagePickerAdapter; // 图片展示的适配器
    private ImageMenuDialog imageMenuDialog; // 图片目录选择Dialog

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
        return R.layout.image_picker_layout;
    }

    protected void initView(View imagePickerView) {
        gvImages = imagePickerView.findViewById(R.id.gv_images);
        tvSelectMenu = imagePickerView.findViewById(R.id.tv_select_menu);
        tvCancelSelect = imagePickerView.findViewById(R.id.tv_cancel_select);
        tvConfirmSelect = imagePickerView.findViewById(R.id.tv_confirm_select);

        imagePickerAdapter = new ImagePickerAdapter(getContext(), DefaultConfigData.SELECTED_IMAGE_ITEM_CAMERA_LAYOUT,
                DefaultConfigData.SELECTED_IMAGE_ITEM_IMAGE_LAYOUT);
        gvImages.setAdapter(imagePickerAdapter);

        imagePickerAdapter.setMaxCount(9);
        imagePickerAdapter.isOpenCamera(false);


        imageMenuDialog = new ImageMenuDialog(getContext());
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
        cancel();
    }

    protected void onConfirm() {
        confirm();
    }

    protected void onShowMenu() {
        showMenu();
    }

    @Override
    public void onImagePickerChange(int currentPickerCount, int maxPickerCount, List<ImageModel> pickerImageList) {
        tvConfirmSelect.setText("(" + pickerImageList.size() + " / " + maxPickerCount + ") 确定");
    }


    private void cancel() {
        iImagePickerOperator.cancel();
    }

    private void confirm() {
        iImagePickerOperator.confirm();
    }

    private void showMenu() {
        iImagePickerOperator.showMenu();
    }

    public final void setImagePickerOperator(IImagePickerOperator.PickerViewCallPage iImagePickerOperator,
                                             ImagePickerParams imagePickerParams) {
        this.iImagePickerOperator = iImagePickerOperator;
        this.imagePickerParams = imagePickerParams;
    }
}
