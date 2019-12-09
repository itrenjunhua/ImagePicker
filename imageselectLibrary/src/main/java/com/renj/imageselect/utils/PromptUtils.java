package com.renj.imageselect.utils;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.renj.imageselect.BuildConfig;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-07-06   15:09
 * <p>
 * 描述：提示信息工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class PromptUtils {
    /**
     * Log日子的 Tag，默认 ImageSelect Logger
     */
    private static String TAG = "ImageSelect Logger";
    /**
     * 是否打印全部类名(类的全路径名)，默认false
     */
    private static boolean IS_FULL_CLASSNAME;
    /**
     * 是否 debug 版本，true 是调试版本；false 是正式版本
     */
    private static boolean isDebug = BuildConfig.DEBUG;

    /**
     * 设置是否打印类的全路径名
     *
     * @param isFullClassName true：打印类的全路径名
     */
    public static void isFullClassName(boolean isFullClassName) {
        PromptUtils.IS_FULL_CLASSNAME = isFullClassName;
    }

    /**
     * 是否需要显示日志信息，默认为debug版本下显示，release版本不显示
     *
     * @param showLogger true：一直显示
     */
    public static void isShowLogger(boolean showLogger) {
        PromptUtils.isDebug = showLogger;
    }

    /**
     * 设置Log的Tag，默认 {@link #TAG}
     *
     * @param tag
     */
    public static void setLoggerTAG(String tag) {
        PromptUtils.TAG = tag;
    }


    public static void v(String msg) {
        if (isDebug) {
            Log.v(TAG, getLogTitle() + msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, getLogTitle() + msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, getLogTitle() + msg);
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            Log.w(TAG, getLogTitle() + msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, getLogTitle() + msg);
        }
    }

    /**
     * 返回类名(根据是否设置了打印全类名返回响应的值)，方法名和日子打印所在行数
     *
     * @return (全)类名.方法名(所在行数):
     */
    private static String getLogTitle() {
        StackTraceElement elm = Thread.currentThread().getStackTrace()[4];
        String className = elm.getClassName();
        if (!IS_FULL_CLASSNAME) {
            int dot = className.lastIndexOf('.');
            if (dot != -1) {
                className = className.substring(dot + 1);
            }
        }
        return className + "." + elm.getMethodName() + "(" + elm.getLineNumber() + ")" + ": ";
    }

    /**
     * 显示Toast
     */
    public static void showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
