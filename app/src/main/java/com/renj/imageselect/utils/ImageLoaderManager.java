package com.renj.imageselect.utils;

import android.app.Activity;
import android.app.Application;
import android.media.Image;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.renj.glide.GlideLoaderModule;
import com.renj.imageloaderlibrary.config.ImageLoadConfig;
import com.renj.imageloaderlibrary.config.ImageLoadLibrary;
import com.renj.imageloaderlibrary.config.ImageModuleConfig;
import com.renj.imageloaderlibrary.loader.IImageLoaderModule;
import com.renj.imageloaderlibrary.loader.ImageLoaderModule;
import com.renj.imageselect.MainActivity;
import com.renj.imageselect.R;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2018-05-07   16:29
 * <p>
 * 描述：图片加载管理类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageLoaderManager {
    /**
     * 初始化自定义图片加载框架
     *
     * @param application {@link Application} 对象
     */
    public static void init(@NonNull Application application) {
        ImageLoaderModule.initImageLoaderModule(
                new ImageModuleConfig.Builder(application)
                        .defaultImageLoadModule(ImageLoadLibrary.GLIDE_LIBRARY, new GlideLoaderModule())
                        .build());
    }

    /**
     * 获取图片加载Module {@link IImageLoaderModule} 的子类对象
     *
     * @return 返回 {@link IImageLoaderModule} 子类对象
     */
    public static IImageLoaderModule getDefaultImageLoaderModule() {
        return ImageLoaderModule.getDefaultImageLoaderModule();
    }

    public static void loadImageForFile(@NonNull String filePath, @NonNull ImageView imageView){
        ImageLoadConfig config = new ImageLoadConfig.Builder()
                .filePath(filePath)
                .target(imageView)
                .build();
        load(ImageLoaderManager.getDefaultImageLoaderModule(), config);
    }

    public static void loadImageForFile(@NonNull Activity activity, @NonNull String filePath, @NonNull ImageView imageView) {
        ImageLoadConfig config = new ImageLoadConfig.Builder()
                .activity(activity)
                .filePath(filePath)
                .target(imageView)
                .build();
        load(ImageLoaderManager.getDefaultImageLoaderModule(), config);
    }

    public static void loadCircleImageForFile(@NonNull Activity activity, @NonNull String filePath, @NonNull ImageView imageView) {
        ImageLoadConfig config = new ImageLoadConfig.Builder()
                .activity(activity)
                .filePath(filePath)
                .asBitmap()
                .asCircle()
                .loadingImageId(R.mipmap.ic_launcher_round)
                .target(imageView)
                .build();
        load(ImageLoaderManager.getDefaultImageLoaderModule(), config);
    }


    private static void load(IImageLoaderModule imageLoaderModule, ImageLoadConfig config) {
        imageLoaderModule.loadImage(config);
    }
}
