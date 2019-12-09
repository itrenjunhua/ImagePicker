package com.renj.selecttest;

import android.app.Application;
import android.widget.ImageView;

import com.renj.imageselect.model.ImageSelectParams;
import com.renj.imageselect.utils.ImageLoaderHelp;
import com.renj.imageselect.utils.ImageSelectUtils;
import com.renj.selecttest.utils.ImageLoaderManager;

import java.io.File;

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

//        ImageSelectUtils.getInstance().configImageSelectParams(
//                new ImageSelectParams.Builder()
//                        .loggerTag("MyCustomTag")
//                        .fileSavePath(new File(""))
//                        .showLogger(true)
//                        .center(true)
//                        .build());
        ImageSelectUtils.getInstance().configImageLoaderModule(new ImageLoaderHelp.ImageLoaderModule() {
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
