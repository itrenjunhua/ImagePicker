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

import com.renj.imagepicker.activity.IImagePickerPage;
import com.renj.imagepicker.model.FolderModel;
import com.renj.imagepicker.model.ImageModel;
import com.renj.imagepicker.model.ImagePickerParams;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-24   09:22
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public abstract class RImagePickerView extends FrameLayout {
    protected IImagePickerPage iImagePickerPage;
    protected ImagePickerParams imagePickerParams;

    public RImagePickerView(@NonNull Context context) {
        this(context, null);
    }

    public RImagePickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RImagePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RImagePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View imagePickerView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        this.addView(imagePickerView);

        initView(imagePickerView);
        initListener();
    }

    public final void setImagePickerOperator(IImagePickerPage iImagePickerPage,
                                             ImagePickerParams imagePickerParams) {
        this.iImagePickerPage = iImagePickerPage;
        this.imagePickerParams = imagePickerParams;
        onParamsInitFinish(iImagePickerPage, imagePickerParams);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(View imagePickerView);

    protected abstract void initListener();

    public abstract void onLoadImageFinish(List<ImageModel> imageModels, List<FolderModel> folderModelList);

    protected void onParamsInitFinish(IImagePickerPage iImagePickerPage, ImagePickerParams imagePickerParams) {

    }

    /**
     * 处理相机照完之后的结果
     *
     * @param imageModel
     */
    public void handlerCameraResult(@NonNull ImageModel imageModel) {

    }
}
