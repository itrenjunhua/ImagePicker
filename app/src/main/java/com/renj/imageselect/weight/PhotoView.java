package com.renj.imageselect.weight;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
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
    // 初始时的缩放比例
    private float initScal;
    // 增加一个变量减少重复的调用布局完成监听方法
    private boolean isOnce = true;
    // 移动临界值
    private int touchSlop = ViewConfiguration.getTouchSlop();

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
                    if ((currentScale > maxScale) && (dSpan > 0) || (currentScale < minScale) && (dSpan < 0))
                        return false;

                    imageMatrix.postScale(scaleFactor, scaleFactor, detector.getCurrentSpanX(), detector.getCurrentSpanY());
                    scaleCheck();
                    setImageMatrix(imageMatrix);
                    return true;
                }
            });

    boolean isCanMove = true;
    int moveX, moveY, lastPointerCount;
    boolean isCheckTopBottom, isCheckLeftRight;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 事件转交
        scaleGestureDetector.onTouchEvent(event);

        int downX = 0, downY = 0, pointerCount;
        pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            downX += event.getX(i);
            downY += event.getY(i);
        }
        downX = downX / pointerCount;
        downY = downY / pointerCount;

        if (pointerCount != lastPointerCount) {
            isCanMove = false;
            moveX = downX;
            moveY = downY;
        }

        lastPointerCount = pointerCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int dx = downX - moveX;
                int dy = downY - moveY;
                if (!isCanMove)
                    isCanMove = isCanMove(dx, dy);

                if (isCanMove) {
                    Drawable drawable = getDrawable();
                    if (drawable != null) {
                        isCheckTopBottom = true;
                        isCheckLeftRight = true;

                        RectF imageRect = getImageRect();
                        // 图片宽度 <= 控件宽度
                        if (imageRect.width() <= getWidth()) {
                            isCheckLeftRight = false;
                            dx = 0;
                        }
                        // 图片高度 <= 控件高度
                        if (imageRect.height() <= getHeight()) {
                            isCheckTopBottom = false;
                            dy = 0;
                        }

                        imageMatrix.postTranslate(dx, dy);
                        translateCheck();
                        setImageMatrix(imageMatrix);
                    }
                }
                moveX = downX;
                moveY = downY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;
                if (isCanMove && getDrawable() != null) {
                    translateCheck();
                    setImageMatrix(imageMatrix);
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 平移时检查边界。执行该方法时图片的宽和高一定会大于控件的宽和高
     */
    private void translateCheck() {
        float dx = 0, dy = 0;
        int width = getWidth();
        int height = getHeight();
        RectF imageRect = getImageRect();

        // 如果 左边界 > 0
        if (imageRect.left > 0 && isCheckLeftRight)
            dx = -imageRect.left;
        // 如果 右边界 < 控件宽度
        if (imageRect.right < width && isCheckLeftRight)
            dx = width - imageRect.right;
        // 如果 上边界 > 0
        if (imageRect.top > 0 && isCheckTopBottom)
            dy = -imageRect.top;
        // 如果 下边界 < 控件高度
        if (imageRect.bottom < height && isCheckTopBottom)
            dy = height - imageRect.bottom;

        imageMatrix.postTranslate(dx, dy);
    }

    /**
     * 检查是需要发生移动操作(移动距离是否 > 发生移动的最小距离)
     */
    private boolean isCanMove(int dx, int dy) {
        return Math.sqrt((dx * dx) + (dy * dy)) >= touchSlop;
    }

    /**
     * 缩放时检查边界。图片大小大于控件大小时不让边界留白；图片大小小于控件大小时居中
     */
    private void scaleCheck() {
        RectF imageRect = getImageRect();

        int width = getWidth();
        int height = getHeight();
        float dx = 0, dy = 0;
        // 图片宽 > 控件宽
        if (imageRect.width() > width) {
            if (imageRect.left > 0)
                dx = -imageRect.left;
            if (imageRect.right < width)
                dx = width - imageRect.right;
        }
        // 图片高 > 控件高
        if (imageRect.height() > height) {
            if (imageRect.top > 0)
                dy = -imageRect.top;
            if (imageRect.bottom < height)
                dy = height - imageRect.bottom;
        }

        // 图片宽 < 控件宽
        if (imageRect.width() < width) {
            dx = width * 0.5f - imageRect.width() * 0.5f - imageRect.left;
        }
        // 图片高 < 控件高
        if (imageRect.height() < height) {
            dy = height * 0.5f - imageRect.height() * 0.5f - imageRect.top;
        }

        imageMatrix.postTranslate(dx, dy);
    }

    /**
     * 布局完成时，根据图片大小进行图片的缩放和位置摆放
     */
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

            initScal = scal;
            // 注意先进行平移操作在进行缩放操作，否则可能导致缩放后的图片不能居中显示
            imageMatrix.postTranslate((width - intrinsicWidth) * 0.5f, (height - intrinsicHeight) * 0.5f);
            imageMatrix.postScale(scal, scal, width * 0.5f, height * 0.5f);
            setImageMatrix(imageMatrix);

            isOnce = false;
        }
    }

    /**
     * 获取当前图片显示的范围
     *
     * @return
     */
    private RectF getImageRect() {
        RectF rectF = new RectF();
        Matrix imageMatrix = getImageMatrix();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            imageMatrix.mapRect(rectF);
        }
        return rectF;
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

    /**
     * 获取最小缩放比例
     *
     * @return
     */
    public float getMinScale() {
        return minScale;
    }

    /**
     * 设置最小缩放比例
     *
     * @param minScale
     */
    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }

    /**
     * 获取最大缩放比例
     *
     * @return
     */
    public float getMaxScale() {
        return maxScale;
    }

    /**
     * 设置最大缩放比例
     *
     * @param maxScale
     */
    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
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
