package com.renj.imageselect.utils;

import com.renj.imageselect.activity.ImageSelectActivity;

import org.jetbrains.annotations.Contract;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-29   16:32
 * <p>
 * 描述：图片选择框操作类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageSelectUtils {
    private volatile static ImageSelectUtils instance = new ImageSelectUtils();

    private ImageSelectUtils() {
    }

    @Contract(pure = true)
    public static ImageSelectUtils newInstance() {
        return instance;
    }

    /**
     * 配置图片加载方法，图片选择框架使用配置的框架加载图片，<br/>
     * <b>建议在Application中调用，注意：必须在UI线程中调用</b>
     *
     * @param imageLoaderModule {@link com.renj.imageselect.utils.ImageLoaderUtils.ImageLoaderModule} 对象
     */
    public void configImageLoaderModule(ImageLoaderUtils.ImageLoaderModule imageLoaderModule) {
        ImageLoaderUtils.newInstance().setImageLoaderModule(imageLoaderModule);
    }

    /**
     * 图片选择裁剪框架入口方法，调用该方法之前，<b>必须先调用{@link #configImageLoaderModule(ImageLoaderUtils.ImageLoaderModule)} 方法配置图片加载方法</b>
     *
     * @return
     */
    public ImageSelectActivity.ImageSelectObservable create() {
        return ImageSelectActivity.create();
    }
}
