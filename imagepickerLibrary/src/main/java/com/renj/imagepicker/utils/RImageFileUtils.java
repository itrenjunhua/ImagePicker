package com.renj.imagepicker.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.renj.imagepicker.model.RImagePickerConfigData;
import com.renj.imagepicker.model.ImagePickerModel;

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
public class RImageFileUtils {
    private static File imageSavePath;

    public static void setImageSavePath(String savePath) {
        if (TextUtils.isEmpty(savePath)) {
            imageSavePath = getDefaultPath();
        } else {
            File file = new File(savePath);
            if (!file.exists() || !file.isDirectory()) {
                boolean mkdirs = file.mkdirs();
                if (mkdirs) {
                    imageSavePath = file;
                } else {
                    imageSavePath = getDefaultPath();
                }
            } else {
                imageSavePath = file;
            }
        }
    }

    public static void setImageSaveFile(File savePath) {
        if (savePath == null || !savePath.exists() || !savePath.isDirectory()) {
            imageSavePath = getDefaultPath();
        } else {
            imageSavePath = savePath;
        }
    }

    /**
     * 获取默认保存路径
     *
     * @return
     */
    @Nullable
    private static File getDefaultPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(), RImagePickerConfigData.IMAGE_PATH);
            if (!file.exists() || !file.isDirectory())
                file.mkdirs();
            return file;
        }
        return null;
    }

    /**
     * 保存图片到文件
     *
     * @param name   文件名称
     * @param bitmap 需要保存的图片
     * @return ImageModel 对象
     */
    @Nullable
    public static ImagePickerModel saveBitmap2File(@NonNull String name, @NonNull Bitmap bitmap) {
        if (imageSavePath == null || bitmap == null) return null;

        try {
            File file = new File(imageSavePath, name);
            OutputStream outputStream = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                return new ImagePickerModel(file.getAbsolutePath(), name, file.getTotalSpace(), System.currentTimeMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        if (imageSavePath == null) return null;

        String name = "camera_" + System.currentTimeMillis() + ".png";
        return new File(imageSavePath, name);
    }
}
