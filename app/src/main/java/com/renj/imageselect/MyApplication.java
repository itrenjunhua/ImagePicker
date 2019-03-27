package com.renj.imageselect;

import android.app.Application;
import android.widget.ImageView;

import com.renj.imageselect.utils.ImageLoaderManager;
import com.renj.imageselect.utils.ImageLoaderUtils;
import com.renj.imageselect.utils.ImageSelectUtils;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2018-07-24   18:06
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderManager.init(this);

        ImageSelectUtils.newInstance().configImageLoaderModule(new ImageLoaderUtils.ImageLoaderModule() {
            @Override
            public void loadImage(String path, ImageView imageView) {
                // 使用图片加载框架加载图片
                ImageLoaderManager.loadImageForFile(path, imageView);
            }
        });
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoaderManager.getDefaultImageLoaderModule().clearMemoryCache();
    }
}
