package com.renj.imagepicker.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.renj.imagepicker.model.ImageModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-29   14:18
 * <p>
 * 描述：相关操作工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageFileUtils {

    /**
     * 保存图片到文件
     *
     * @param name   文件名称
     * @param bitmap 需要保存的图片
     * @return ImageModel 对象
     */
    @Nullable
    public static ImageModel saveBitmap2File(@NonNull String name, @NonNull Bitmap bitmap) {
        File saveDir = ConfigUtils.getSaveDir();
        if (saveDir == null || bitmap == null) return null;

        try {
            File file = new File(saveDir, name);
            OutputStream outputStream = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                return new ImageModel(file.getAbsolutePath(), name, System.currentTimeMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (ConfigUtils.isShowLogger())
                ConfigUtils.e("保存裁剪图片失败 => " + e);
        }
        return null;
    }

    /**
     * 得到保存的文件名称，减少文件名的重复
     *
     * @return
     */
    @NonNull
    public static String getName() {
        return "crop_" + System.currentTimeMillis() + ".png";
    }

    /**
     * 获取使用相机时保存的照片路径
     *
     * @return
     */
    @NonNull
    public static File getCameraSavePath() {
        File saveDir = ConfigUtils.getSaveDir();
        if (saveDir == null) return null;

        String name = "camera_" + System.currentTimeMillis() + ".png";
        return new File(saveDir, name);
    }
}
