package com.renj.imagepicker.weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-01-02   15:29
 * <p>
 * 描述：不可滑动的ViewPager
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class IPNoScrollViewPager extends ViewPager {
    public IPNoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public IPNoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
