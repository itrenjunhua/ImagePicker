package com.renj.imageselect.utils;

import com.renj.imageselect.activity.ImageSelectActivity;
import com.renj.imageselect.model.ImageSelectParams;

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
        CommonUtils.initParams(new ImageSelectParams.Builder().build());
    }

    public static ImageSelectUtils getInstance() {
        return instance;
    }

    /**
     * 配置全局参数
     *
     * @param imageSelectParams {@link ImageSelectParams} 对象，配置全局参数
     */
    public void configImageSelectParams(ImageSelectParams imageSelectParams) {
        if (imageSelectParams == null)
            CommonUtils.initParams(new ImageSelectParams.Builder().build());
        CommonUtils.initParams(imageSelectParams);
    }

    /**
     * 配置图片加载方法，图片选择框架使用配置的框架加载图片，<br/>
     * <b>建议在Application中调用，注意：必须在UI线程中调用</b>
     *
     * @param imageLoaderModule {@link ImageLoaderHelp.ImageLoaderModule} 对象
     */
    public void configImageLoaderModule(ImageLoaderHelp.ImageLoaderModule imageLoaderModule) {
        ImageLoaderHelp.getInstance().setImageLoaderModule(imageLoaderModule);
    }

    /**
     * 图片选择裁剪框架入口方法，调用该方法之前，<b>必须先调用{@link #configImageLoaderModule(ImageLoaderHelp.ImageLoaderModule)} 方法配置图片加载方法</b>
     *
     * @return
     */
    public ImageSelectActivity.ImageSelectObservable create() {
        return ImageSelectActivity.create(true);
    }
}
