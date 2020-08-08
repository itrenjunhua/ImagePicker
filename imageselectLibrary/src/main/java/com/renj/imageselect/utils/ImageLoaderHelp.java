package com.renj.imageselect.utils;

import android.widget.ImageView;

import com.renj.imageselect.listener.ImageLoaderModule;
import com.renj.imageselect.listener.OnCropImageChange;
import com.renj.imageselect.listener.OnResultCallBack;
import com.renj.imageselect.listener.OnSelectedImageChange;

import org.jetbrains.annotations.Contract;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2018-07-24   16:41
 * <p>
 * 描述：图片加载工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageLoaderHelp {
    private volatile static ImageLoaderHelp instance = new ImageLoaderHelp();

    private ImageLoaderHelp() {
    }

    @Contract(pure = true)
    public static ImageLoaderHelp getInstance() {
        return instance;
    }

    /**
     * 加载图片方法
     *
     * @param path      图片路径
     * @param imageView {@link ImageView} 控件
     */
    public void loadImage(String path, ImageView imageView) {
        if (imageLoaderModule != null)
            imageLoaderModule.loadImage(path, imageView);
    }

    /*********************************************/

    private ImageLoaderModule imageLoaderModule;
    private OnSelectedImageChange onSelectedImageChange;  // 图片选择页面，图片选择发生变化时回调
    private OnCropImageChange onCropImageChange; // 图片发生裁剪时回调
    private OnResultCallBack onResultCallBack; // 结果回调

    void setImageLoaderModule(ImageLoaderModule imageLoaderModule) {
        this.imageLoaderModule = imageLoaderModule;
    }

    public OnSelectedImageChange getOnSelectedImageChange() {
        return onSelectedImageChange;
    }

    public void setOnSelectedImageChange(OnSelectedImageChange onSelectedImageChange) {
        this.onSelectedImageChange = onSelectedImageChange;
    }

    public OnCropImageChange getOnCropImageChange() {
        return onCropImageChange;
    }

    public void setOnCropImageChange(OnCropImageChange onCropImageChange) {
        this.onCropImageChange = onCropImageChange;
    }

    public OnResultCallBack getOnResultCallBack() {
        return onResultCallBack;
    }

    public void setOnResultCallBack(OnResultCallBack onResultCallBack) {
        this.onResultCallBack = onResultCallBack;
    }
}
