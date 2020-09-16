package com.renj.imagepicker.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-09-16   14:51
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class PageStyleUtils {
    public static void setStatusBarDark(Activity activity, boolean dark) {
        Window window = activity.getWindow();
        View decor = window.getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        ViewGroup contentView = activity.findViewById(android.R.id.content);
        if (contentView == null) {
            return;
        }
        if (contentView.getChildCount() > 0) {
            View pageView = contentView.getChildAt(0);
            if (pageView != null) {
                pageView.setFitsSystemWindows(true);
            }
        }
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(color);
        }
    }
}
