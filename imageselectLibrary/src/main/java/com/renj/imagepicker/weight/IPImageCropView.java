package com.renj.imagepicker.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.renj.imagepicker.ImagePickerParams;
import com.renj.imagepicker.R;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.utils.ImagePickerLoaderUtils;

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
public class IPImageCropView extends RelativeLayout {

    private IPPhotoView photoView;
    private IPCropDrawView cropDrawView;

    public IPImageCropView(Context context) {
        this(context, null);
    }

    public IPImageCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IPImageCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IPImageCropView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View clipViewLayout = LayoutInflater.from(context).inflate(R.layout.image_picker_image_crop_view, null);
        photoView = clipViewLayout.findViewById(R.id.photo_view);
        cropDrawView = clipViewLayout.findViewById(R.id.crop_view);

        addView(clipViewLayout);
    }

    /**
     * 设置图片到控件
     *
     * @param path 图片路径
     */
    public void setImage(@NonNull String path) {
        ImagePickerLoaderUtils.loadImage(path, photoView);
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
     * 裁剪图片返回 {@link ImagePickerModel} 对象
     *
     * @return {@link ImagePickerModel} 对象
     */
    public void cut(@NonNull final CutListener cutListener) {
        runOnNewThread(new Runnable() {
            @Override
            public void run() {
                cropDrawView.confirmCrop(new IPCropDrawView.OnCropRangeListener() {
                    @Override
                    public void cropRange(IPCropDrawView.CropShape cropShape, RectF cropRectF) {
                        final ImagePickerModel imagePickerModel = photoView.cropBitmap(cropShape, cropRectF);
                        runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                cutListener.cutFinish(imagePickerModel);
                            }
                        });
                    }
                });
            }
        });
    }

    private void runOnNewThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void runOnMainThread(Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }

    /**
     * 设置裁剪控件参数
     *
     * @param imagePickerParams
     */
    public void setCropViewParams(@NonNull ImagePickerParams imagePickerParams) {
        cropDrawView.setCropViewParams(imagePickerParams);
        photoView.setCropViewParams(imagePickerParams);
    }

    public interface CutListener {
        void cutFinish(ImagePickerModel imagePickerModel);
    }
}
