package com.renj.imageselect.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.renj.imageselect.model.ImageModel;

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
public class Utils {

    /**
     * 保存图片到文件
     *
     * @param name   文件名称
     * @param bitmap 需要保存的图片
     * @return ImageModel 对象
     */
    @Nullable
    public static ImageModel saveBitmap2File(@NonNull String name, @NonNull Bitmap bitmap) {
        File saveDir = getSaveDir();
        if (saveDir == null || bitmap == null) return null;

        try {
            File file = new File(saveDir, name);
            OutputStream outputStream = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                return new ImageModel(file.getAbsolutePath(), name, System.currentTimeMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("保存裁剪图片失败 => " + e);
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
        return "clip_" + System.currentTimeMillis() + ".png";
    }

    /**
     * 获取保存路径
     *
     * @return
     */
    @Nullable
    private static File getSaveDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(), "image_select");
            if (file == null || !file.exists() || !file.isDirectory())
                file.mkdirs();
            return file;
        }
        return null;
    }

    /**
     * 获取使用相机时保存的照片路径
     *
     * @return
     */
    @NonNull
    public static File getCameraSavePath() {
        String name = "camera_" + System.currentTimeMillis() + ".png";
        return new File(getSaveDir(), name);
    }

    /**
     * 开启新线程执行任务
     *
     * @param runnable
     */
    public static void runOnNewThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
