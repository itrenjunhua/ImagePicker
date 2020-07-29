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

import com.renj.imageselect.R;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.CommonUtils;
import com.renj.imageselect.utils.ImageLoaderHelp;

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
public class ImageCropView extends RelativeLayout {

    private PhotoView photoView;
    private CropView cropView;

    public ImageCropView(Context context) {
        this(context, null);
    }

    public ImageCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageCropView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View clipViewLayout = LayoutInflater.from(context).inflate(R.layout.image_crop_view, null);
        photoView = clipViewLayout.findViewById(R.id.photo_view);
        cropView = clipViewLayout.findViewById(R.id.crop_view);

        addView(clipViewLayout);
    }

    /**
     * 设置图片到控件
     *
     * @param path 图片路径
     */
    public void setImage(@NonNull String path) {
        ImageLoaderHelp.getInstance().loadImage(path, photoView);
    }

    /**
     * 设置图片到控件
     *
     * @param bitmap {@link Bitmap} 对象
     */
    public void setImage(@NonNull Bitmap bitmap) {
        photoView.setImageBitmap(bitmap);
    }

    /**
     * 裁剪图片返回 {@link ImageModel} 对象
     *
     * @return {@link ImageModel} 对象
     */
    public void cut(@NonNull final CutListener cutListener) {
        CommonUtils.runOnNewThread(new Runnable() {
            @Override
            public void run() {
                cropView.confirmCrop(new CropView.OnCropRangeListener() {
                    @Override
                    public void cropRange(CropView.CropShape cropShape, RectF cropRectF) {
                        ImageModel imageModel = photoView.cropBitmap(cropShape, cropRectF);
                        cutListener.cutFinish(imageModel);
                    }
                });
            }
        });
    }

    /**
     * 设置裁剪控件参数
     *
     * @param imageParamsConfig
     */
    public void setCropViewParams(@NonNull ImageParamsConfig imageParamsConfig) {
        cropView.setCropViewParams(imageParamsConfig);
        photoView.setCropViewParams(imageParamsConfig);
    }

    public interface CutListener{
        void cutFinish(ImageModel imageModel);
    }
}
