package com.renj.imageselect.utils;

import android.content.Context;

import com.renj.imageselect.activity.ImageSelectActivity;
import com.renj.imageselect.listener.ImageLoaderModule;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.model.ImageSelectParams;

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
        CommonUtils.initParams(new ImageSelectParams.Builder().build());
    }

    @Contract(pure = true)
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
     * @param imageLoaderModule {@link ImageLoaderModule} 对象
     */
    public void configImageLoaderModule(ImageLoaderModule imageLoaderModule) {
        ImageLoaderHelp.getInstance().setImageLoaderModule(imageLoaderModule);
    }

    /**
     * 开始图片选择裁剪
     *
     * @return
     */
    public void start(Context context, ImageParamsConfig paramsConfig) {
        ImageSelectActivity.open(context, paramsConfig);
    }
}
