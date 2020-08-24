package com.renj.selecttest;

import android.app.Application;
import android.widget.ImageView;

import com.renj.imagepicker.listener.ImagePickerLoaderModule;
import com.renj.imagepicker.ImagePickerUtils;
import com.renj.selecttest.utils.ImageLoaderManager;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
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

//        ImagePickerConfig pickerConfig = new ImagePickerConfig.Builder()
//                .loggerTag("MyCustomTag")
//                .fileSavePath(new File(""))
//                .showLogger(true)
//                .center(true)
//                .build();
//        ImagePickerUtils.init(pickerConfig, new ImageLoaderModule() {
//            @Override
//            public void loadImage(String path, ImageView imageView) {
//                // 使用图片加载框架加载图片
//                ImageLoaderManager.loadImageForFile(path, imageView);
//            }
//        });

        ImagePickerUtils.init(new ImagePickerLoaderModule() {
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
