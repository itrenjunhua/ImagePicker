package com.renj.imagepicker.listener;

import android.widget.ImageView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-08   18:43
 * <p>
 * 描述：配置图片加载方法回调
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface ImagePickerLoaderModule {
    /**
     * 图片加载方法
     *
     * @param path      图片路径
     * @param imageView {@link ImageView} 控件
     */
    void loadImage(String path, ImageView imageView);
}
