package com.renj.selecttest.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-04-28   13:21
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class Utils {
    public static boolean isEmpty(String string) {
        if (TextUtils.isEmpty(string) || "null".equals(string))
            return true;
        return false;
    }

    @NonNull
    public static String getTextViewContent(@NonNull TextView textView) {
        return textView.getText().toString().trim();
    }

    public static int parseInteger(String intStr) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
