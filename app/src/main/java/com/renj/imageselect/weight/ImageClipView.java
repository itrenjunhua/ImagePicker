package com.renj.imageselect.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.renj.imageselect.R;
import com.renj.imageselect.model.ImageModel;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-28   17:07
 * <p>
 * 描述：图片裁剪控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageClipView extends RelativeLayout {

    private PhotoView photoView;
    private ClipView clipView;

    public ImageClipView(Context context) {
        this(context, null);
    }

    public ImageClipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageClipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View clipViewLayout = LayoutInflater.from(context).inflate(R.layout.image_clip_view, null);
        photoView = clipViewLayout.findViewById(R.id.photo_view);
        clipView = clipViewLayout.findViewById(R.id.clip_view);

        addView(clipViewLayout);
    }

    /**
     * 设置图片到控件
     *
     * @param path 图片路径
     */
    public void setImage(@NonNull String path) {
        Glide.with(getContext()).load(path).into(photoView);
    }

    /**
     * 设置图片到控件
     *
     * @param bitmap {@link Bitmap} 对象
     */
    public void setImage(@NonNull Bitmap bitmap) {
        photoView.setImageBitmap(bitmap);
    }

    ImageModel imageModel;

    /**
     * 裁剪图片返回 {@link ImageModel} 对象
     *
     * @return {@link ImageModel} 对象
     */
    public ImageModel cut() {
        clipView.confirmCrop(new ClipView.OnCropRangeListener() {
            @Override
            public void cropRange(ClipView.CropShape cropShape, RectF cropRectF) {
                imageModel = photoView.clipBitmap(cropShape, cropRectF);
            }
        });
        return imageModel;
    }

    /**
     * 设置遮罩层颜色<br/>
     * <b>注意：如果需要使设置生效，需要调用 {@link #makeEffective()} 方法，{@link #makeEffective()} 方法只需当所有设置完成之后执行一次即可</b>
     *
     * @param maskColor 遮罩层颜色
     * @return
     */
    public ImageClipView setMaskColor(int maskColor) {
        clipView.setMaskColor(maskColor);
        return this;
    }

    /**
     * 设置边框颜色<br/>
     * <b>注意：如果需要使设置生效，需要调用 {@link #makeEffective()} 方法，{@link #makeEffective()} 方法只需当所有设置完成之后执行一次即可</b>
     *
     * @param borderColor 边框颜色
     * @return
     */
    public ImageClipView setBorderColor(int borderColor) {
        clipView.setBorderColor(borderColor);
        return this;
    }

    /**
     * 设置边框宽度 单位 dp<br/>
     * <b>注意：如果需要使设置生效，需要调用 {@link #makeEffective()} 方法，{@link #makeEffective()} 方法只需当所有设置完成之后执行一次即可</b>
     *
     * @param borderWidth 边框颜色
     * @return
     */
    public ImageClipView setBorderWidth(float borderWidth) {
        clipView.setBorderWidth(borderWidth);
        return this;
    }

    /**
     * 设置裁剪范围的宽度 单位 dp<br/>
     * <b>注意：如果需要使设置生效，需要调用 {@link #makeEffective()} 方法，{@link #makeEffective()} 方法只需当所有设置完成之后执行一次即可</b>
     *
     * @param cropWidth 裁剪宽度 单位 dp
     * @return
     */
    public ImageClipView setCropWidth(int cropWidth) {
        clipView.setCropWidth(cropWidth);
        return this;
    }

    /**
     * 设置裁剪范围的高度 单位 dp<br/>
     * <b>注意：如果需要使设置生效，需要调用 {@link #makeEffective()} 方法，{@link #makeEffective()} 方法只需当所有设置完成之后执行一次即可</b>
     *
     * @param cropHeight 裁剪高度 单位 dp
     * @return
     */
    public ImageClipView setCropHeight(int cropHeight) {
        clipView.setCropHeight(cropHeight);
        return this;
    }

    /**
     * 设置裁剪的形状，当形状为 圆形时，半径 = 宽或者高的最小值 / 2<br/>
     * <b>注意：如果需要使设置生效，需要调用 {@link #makeEffective()} 方法，{@link #makeEffective()} 方法只需当所有设置完成之后执行一次即可</b>
     *
     * @param cropShape 裁剪形状 0：圆形 非0：矩形
     * @return
     */
    public ImageClipView setCropShape(int cropShape) {
        clipView.setCropShape(0 == cropShape ? ClipView.CropShape.CROP_CIRCLE : ClipView.CropShape.CROP_RECT);
        return this;
    }

    /**
     * 使设置的各种属性生效
     */
    public void makeEffective() {
        clipView.makeEffective();
    }
}
