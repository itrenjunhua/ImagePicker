package com.renj.imageselect.weight;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
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
    // 增加一个变量减少重复的调用布局完成监听方法
    private boolean isOnce = true;
    // 移动临界值
    private int touchSlop;
    // 初始时的缩放比例，在自动缩放时使用到的
    private float initScal = 1.0f;
    // 是否自动缩放
    private boolean isAutoScal = false;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(this);
    }

    // 用于检测缩放的手势
    private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(),
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    if (getDrawable() == null)
                        return false;

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

    // 用于检测双击的事件，双击效果，当前缩放比例在 2 以下时，将比例设置为 2，当前缩放比例在2-4之间时，将比例设置为 4，其他情况将比例设置为初始比例
    private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (getDrawable() == null)
                return false;

            if (isAutoScal) return false;

            float currentScale = getCurrentScale();
            if (currentScale < 2) {
                isAutoScal = true;
                AutoScalTask autoScalTask = new AutoScalTask(2, (int) e.getX(), (int) e.getY());
                post(autoScalTask);
            } else if (currentScale >= 2 && currentScale < maxScale) {
                isAutoScal = true;
                AutoScalTask autoScalTask = new AutoScalTask(4, (int) e.getX(), (int) e.getY());
                post(autoScalTask);
            } else {
                isAutoScal = true;
                AutoScalTask autoScalTask = new AutoScalTask(initScal, (int) e.getX(), (int) e.getY());
                post(autoScalTask);
            }
            return true;
        }
    });

    private boolean isCanMove = true; // 是否能移动
    private int lastX, lastY, lastPointerCount;
    private boolean isCheckTopBottom, isCheckLeftRight; // 是否需要检查边界

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 事件转交
        if (gestureDetector.onTouchEvent(event))
            return true;
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
            lastX = downX;
            lastY = downY;
        }

        lastPointerCount = pointerCount;
        RectF imageRect = getImageRect();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (imageRect.width() > getWidth() || imageRect.height() > getHeight())
                    getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (imageRect.width() > getWidth() || imageRect.height() > getHeight())
                    getParent().requestDisallowInterceptTouchEvent(true);

                int dx = downX - lastX;
                int dy = downY - lastY;
                if (!isCanMove)
                    isCanMove = isCanMove(dx, dy);

                if (isCanMove) {
                    Drawable drawable = getDrawable();
                    if (drawable != null) {
                        // 当达到边界之后不再将事件交给父窗体
                        if (imageRect.left == 0 && dx > 0)
                            getParent().requestDisallowInterceptTouchEvent(false);

                        if (imageRect.right == getWidth() && dx < 0)
                            getParent().requestDisallowInterceptTouchEvent(false);

                        isCheckTopBottom = true;
                        isCheckLeftRight = true;

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
                lastX = downX;
                lastY = downY;
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

    /**
     * 自动方法和缩小的任务
     */
    class AutoScalTask implements Runnable {
        // 每次放大的倍数
        private final float BIGGER = 1.04f;
        // 每次缩小的倍数
        private final float SMALLER = 0.96f;
        private int centerX, centerY;
        // 目标比例
        private float targetScal;
        // 自动进行缩放时使用的比例
        private float tempScal;

        public AutoScalTask(float targetScal, int centerX, int centerY) {
            this.targetScal = targetScal;
            this.centerX = centerX;
            this.centerY = centerY;

            // 判断是缩小还是放大
            float currentScale = getCurrentScale();
            if (currentScale > targetScal)
                tempScal = SMALLER;
            else tempScal = BIGGER;
        }

        @Override
        public void run() {
            imageMatrix.postScale(tempScal, tempScal, centerX, centerY);
            scaleCheck();
            setImageMatrix(imageMatrix);

            float currentScale = getCurrentScale();
            if ((tempScal > 1 && currentScale < targetScal) || (tempScal < 1 && currentScale > targetScal)) {
                PhotoView.this.postDelayed(this, 15);
            } else {
                // 根据当前比例和目标比例计算出最终的的缩放比例才能到达目标比例
                float lastScale = targetScal / currentScale;
                imageMatrix.postScale(lastScale, lastScale, centerX, centerY);
                scaleCheck();
                setImageMatrix(imageMatrix);
                isAutoScal = false;
            }
        }
    }
}
