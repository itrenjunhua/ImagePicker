package com.renj.imageselect.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.renj.imageselect.model.ImageSelectConfig;

import static android.graphics.Canvas.ALL_SAVE_FLAG;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-25   16:13
 * <p>
 * 描述：用于指定裁剪形状的控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ClipView extends View {
    // 默认遮罩层颜色
    private final int DEFAULT_MASK_COLOR = 0x80000000;
    // 默认边框颜色
    private final int DEFAULT_BORDER_COLOR = 0xFFFFFFFF;
    // 默认边框宽度 1dp
    private final int DEFAULT_BORDER_WIDTH = 1;
    // 默认裁剪宽高相等，都为 200dp，默认矩形
    private final int DEFAULT_SIZE = 200;
    // 默认裁剪形状 矩形
    private ClipShape DEFAULT_CLIP_SHAPE = ClipShape.CLIP_RECT;

    // 用户绘制背景的画笔
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 用于绘制边框的画笔
    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 遮罩层颜色
    private int maskColor = DEFAULT_MASK_COLOR;
    // 边框颜色
    private int borderColor = DEFAULT_BORDER_COLOR;
    // 边框宽度
    private float borderWidth = dp2Px(DEFAULT_BORDER_WIDTH);
    // 裁剪宽度
    private int clipWidth = dp2Px(DEFAULT_SIZE);
    // 裁剪高度
    private int clipHeight = dp2Px(DEFAULT_SIZE);
    // 裁剪范围
    private RectF clipRect = new RectF();
    // 裁剪形状
    private ClipShape clipShape = DEFAULT_CLIP_SHAPE;
    // 控件的宽、高
    private int width, height;
    // 控件的范围
    private RectF rectF;

    public void confirmClip(@NonNull OnClipRangeListener onClipRangeListener) {
        if (onClipRangeListener != null) {
            onClipRangeListener.clipRange(clipShape, clipRect);
        }
    }

    public ClipView(Context context) {
        this(context, null);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    // 初始化设置裁剪控件基本属性
    private void init() {
        paint.setStyle(Paint.Style.FILL);

        borderPaint.setStyle(Paint.Style.STROKE);

        clipShape = ClipShape.CLIP_RECT;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        rectF = new RectF(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 检查配置的数据
        checkConfigValue();

        paint.setColor(maskColor);

        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);

        int saveLayer = canvas.saveLayer(rectF, null, ALL_SAVE_FLAG);

        canvas.drawRect(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        int left = (width - clipWidth) / 2;
        int top = (height - clipHeight) / 2;
        clipRect = new RectF(left, top, left + clipWidth, top + clipHeight);

        if (ClipShape.CLIP_RECT == clipShape) { // 矩形
            canvas.drawRect(clipRect, paint);
            canvas.drawRect(clipRect, borderPaint);
        } else { // 圆形
            float centerX = (clipRect.left + clipRect.right) / 2;
            float centerY = (clipRect.top + clipRect.bottom) / 2;
            int radius = (Math.min(clipWidth, clipHeight)) / 2;
            canvas.drawCircle(centerX, centerY, radius, paint);
            canvas.drawCircle(centerX, centerY, radius, borderPaint);
        }
        paint.setXfermode(null);

        canvas.restoreToCount(saveLayer);
    }

    /**
     * 设置裁剪控件参数
     *
     * @param imageSelectConfig
     */
    public void setClipViewParams(@NonNull ImageSelectConfig imageSelectConfig) {
        this.maskColor = imageSelectConfig.getMaskColorColor();
        this.clipWidth = dp2Px(imageSelectConfig.getWidth());
        this.clipHeight = dp2Px(imageSelectConfig.getHeight());
        this.borderColor = imageSelectConfig.getClipBorderColor();
        this.borderWidth = dp2Px(imageSelectConfig.getClipBorderWidth());
        this.clipShape = imageSelectConfig.isCircleClip() ? ClipShape.CLIP_CIRCLE : ClipShape.CLIP_RECT;

        postInvalidate();
    }

    /**
     * 检查配置的数据
     */
    private void checkConfigValue() {
        if(this.clipWidth > getMeasuredWidth()) this.clipWidth = getMeasuredWidth();
        if(this.clipHeight > getMeasuredHeight()) this.clipHeight = getMeasuredHeight();
        if(this.borderWidth < 0) this.borderWidth = 0;
    }

    /**
     * dp转换成px
     *
     * @param dp
     * @return
     */
    private int dp2Px(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5);
    }

    /**
     * 裁剪形状枚举
     */
    public enum ClipShape {
        /**
         * 矩形
         */
        CLIP_RECT,
        /**
         * 圆形
         */
        CLIP_CIRCLE
    }

    /**
     * 裁剪监听
     */
    public interface OnClipRangeListener {
        void clipRange(ClipShape clipShape, RectF clipRectF);
    }
}
