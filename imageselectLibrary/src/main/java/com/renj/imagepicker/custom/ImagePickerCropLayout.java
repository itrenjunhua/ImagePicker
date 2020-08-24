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

import com.renj.imagepicker.listener.IImageCropPage;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.ImagePickerParams;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-24   11:45
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public abstract class ImagePickerCropLayout extends FrameLayout {
    protected IImageCropPage imageCropPage;
    protected ImagePickerParams imagePickerParams;
    protected List<ImagePickerModel> imagePickerList;

    public ImagePickerCropLayout(@NonNull Context context) {
        this(context, null);
    }

    public ImagePickerCropLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePickerCropLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImagePickerCropLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View imageCropView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        this.addView(imageCropView);

        initView(imageCropView);
        initListener();
    }

    public final void setImageCropOperator(IImageCropPage imageCropPage,
                                           ImagePickerParams imagePickerParams,
                                           List<ImagePickerModel> imagePickerList) {
        this.imageCropPage = imageCropPage;
        this.imagePickerParams = imagePickerParams;
        this.imagePickerList = imagePickerList;
        onParamsInitFinish(imageCropPage, imagePickerParams, imagePickerList);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(View imageCropView);

    protected abstract void initListener();

    protected void onParamsInitFinish(IImageCropPage imageCropPage,
                                      ImagePickerParams imagePickerParams,
                                      List<ImagePickerModel> imagePickerList) {

    }
}
