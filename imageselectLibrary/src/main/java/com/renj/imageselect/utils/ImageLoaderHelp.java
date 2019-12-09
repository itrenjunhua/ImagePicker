package com.renj.imageselect.utils;

import android.widget.ImageView;

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

    void setImageLoaderModule(ImageLoaderModule imageLoaderModule) {
        this.imageLoaderModule = imageLoaderModule;
    }

    /**
     * 配置图片加载方法回调
     */
    public interface ImageLoaderModule {
        /**
         * 图片加载方法
         *
         * @param path      图片路径
         * @param imageView {@link ImageView} 控件
         */
        void loadImage(String path, ImageView imageView);
    }
}
