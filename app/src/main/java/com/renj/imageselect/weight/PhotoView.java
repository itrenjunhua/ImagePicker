package com.renj.imageselect.weight;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-21   9:32
 * <p>
 * 描述：可移动和缩放的 {@link ImageView} 控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class PhotoView extends AppCompatImageView implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {
    // 用于进行变换的 Matrix
    private Matrix imageMatrix = new Matrix();
    // 最小、最大缩放比例
    private float minScale = 0.25f, maxScale = 4f;
    // 增加一个变量减少重复的调用布局完成监听方法
    private boolean isOnce = true;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(this);
    }

    // 用于检测缩放的手势
    private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(),
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    // 当前缩放比例和手指缩放比例比较
                    float scaleFactor = detector.getScaleFactor();
                    float currentScale = getCurrentScale();
                    float dSpan = detector.getCurrentSpan() - detector.getPreviousSpan();
                    if ((currentScale > maxScale) && (dSpan > 0)|| (currentScale < minScale) && (dSpan < 0)) return false;

                    imageMatrix.postScale(scaleFactor, scaleFactor, detector.getCurrentSpanX(), detector.getCurrentSpanY());
                    setImageMatrix(imageMatrix);
                    return true;
                }
            });

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 事件转交
        return scaleGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onGlobalLayout() {
        if (isOnce) {
            Drawable drawable = getDrawable();
            if (drawable == null) return;

            int width = getWidth();
            int height = getHeight();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();

            // 如果图片宽和高大于控件的宽和高，就缩小图片
            // 如果图片宽和高小于控件的宽和高，就将图片放在控件中间
            float scal = 1.0f;
            if (intrinsicWidth > width && intrinsicHeight < height) {
                scal = width * 1.0f / intrinsicWidth;
            }

            if (intrinsicHeight > height && intrinsicWidth < width) {
                scal = height * 1.0f / intrinsicHeight;
            }

            if (intrinsicWidth > width && intrinsicHeight > height) {
                scal = Math.min(width * 1.0f / intrinsicWidth, height * 1.0f / intrinsicHeight);
            }

            imageMatrix.postScale(scal, scal, width / 2, height / 2);
            imageMatrix.postTranslate((width - intrinsicWidth) / 2, (height - intrinsicHeight) / 2);
            setImageMatrix(imageMatrix);

            isOnce = false;
        }
    }

    /**
     * 获取当前缩放比例
     *
     * @return
     */
    private float getCurrentScale() {
        float[] floats = new float[9];
        Matrix matrix = getImageMatrix();
        matrix.getValues(floats);
        return floats[Matrix.MSCALE_X];
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
