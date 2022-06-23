package com.renj.pickertest.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
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

    public static void loadImageForFile(@NonNull String filePath, @NonNull ImageView imageView){
        Glide.with(imageView).load(filePath).into(imageView);
    }

    public static void loadImageForFile(@NonNull Activity activity, @NonNull String filePath, @NonNull ImageView imageView) {
        Glide.with(activity).load(filePath).into(imageView);
    }
}
