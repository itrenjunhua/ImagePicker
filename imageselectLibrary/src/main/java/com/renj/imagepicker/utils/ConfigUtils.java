package com.renj.imagepicker.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.renj.imagepicker.model.ImagePickerConfig;

import java.io.File;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-07-06   15:09
 * <p>
 * 描述：公共工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ConfigUtils {
    private static ImagePickerConfig imagePickerConfig;

    static void initParams(ImagePickerConfig imagePickerConfig) {
        ConfigUtils.imagePickerConfig = imagePickerConfig;
    }

    static File getSaveDir() {
        return imagePickerConfig.getFileSavePath();
    }

    public static boolean isShowLogger() {
        return imagePickerConfig.isShowLogger();
    }

    public static void i(String msg) {
        Log.i(imagePickerConfig.getLoggerTag(), getLogTitle() + msg);
    }

    public static void e(String msg) {
        Log.e(imagePickerConfig.getLoggerTag(), getLogTitle() + msg);
    }

    /**
     * 返回类名(根据是否设置了打印全类名返回响应的值)，方法名和日子打印所在行数
     *
     * @return (全)类名.方法名(所在行数):
     */
    private static String getLogTitle() {
        StackTraceElement elm = Thread.currentThread().getStackTrace()[4];
        String className = elm.getClassName();
        int dot = className.lastIndexOf('.');
        if (dot != -1) {
            className = className.substring(dot + 1);
        }
        return className + "." + elm.getMethodName() + "(" + elm.getLineNumber() + ")" + ": ";
    }

    /**
     * 显示Toast
     */
    public static void showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        if (imagePickerConfig.isCenter())
            toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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

    /**
     * 开启新线程执行任务
     *
     * @param runnable
     */
    public static void runOnMainThread(Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}
