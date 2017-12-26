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
public class CropView extends View {
    // 默认遮罩层颜色
    private final int DEFAULT_MASK_COLOR = 0xaa000000;
    // 默认边框颜色
    private final int DEFAULT_BORDER_COLOR = 0xFFFFFFFF;
    // 默认边框宽度 1dp
    private final int DEFAULT_BORDER_WIDTH = 1;
    // 默认裁剪宽高相等，都为 200dp，默认矩形
    private final int DEFAULT_SIZE = 200;
    // 默认裁剪形状 矩形
    private CropShape DEFAULT_CROP_SHAPE = CropShape.CROP_RECT;

    // 用户绘制背景的画笔
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 用于绘制边框的画笔
    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 遮罩层颜色
    private int maskColor = DEFAULT_MASK_COLOR;
    // 边框颜色
    private int borderColor = DEFAULT_BORDER_COLOR;
    // 边框宽度
    private int borderWidth = DEFAULT_BORDER_WIDTH;
    // 裁剪宽度
    private int cropWidth = dp2Px(DEFAULT_SIZE);
    // 裁剪高度
    private int cropHeight = dp2Px(DEFAULT_SIZE);
    // 裁剪范围
    private RectF cropRect = new RectF();
    // 裁剪形状
    private CropShape cropShape = DEFAULT_CROP_SHAPE;
    // 控件的宽、高
    private int width, height;
    // 控件的范围
    private RectF rectF;

    public void confirmCrop(@NonNull  OnCropRangeListener onCropRangeListener) {
        if (onCropRangeListener != null) {
            onCropRangeListener.cropRange(cropShape, cropRect);
        }
    }

    public CropView(Context context) {
        this(context, null);
    }

    public CropView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint.setColor(maskColor);
        paint.setStyle(Paint.Style.FILL);

        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(dp2Px(borderWidth));
        borderPaint.setStyle(Paint.Style.STROKE);

        cropShape = CropShape.CROP_CIRCLE;
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

        int saveLayer = canvas.saveLayer(rectF, null, ALL_SAVE_FLAG);

        canvas.drawRect(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        int left = (width - cropWidth) / 2;
        int top = (height - cropHeight) / 2;
        cropRect = new RectF(left, top, left + cropWidth, top + cropHeight);

        if (CropShape.CROP_RECT == cropShape) { // 矩形
            canvas.drawRect(cropRect, paint);
            canvas.drawRect(cropRect, borderPaint);
        } else { // 圆形
            float centerX = (cropRect.left + cropRect.right) / 2;
            float centerY = (cropRect.top + cropRect.bottom) / 2;
            int radius = (Math.min(cropWidth, cropHeight)) / 2;
            canvas.drawCircle(centerX, centerY, radius, paint);
            canvas.drawCircle(centerX, centerY, radius, borderPaint);
        }
        paint.setXfermode(null);

        canvas.restoreToCount(saveLayer);
    }

    /**
     * dp转换成px
     *
     * @param dp
     * @return
     */
    private int dp2Px(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5);
    }

    /**
     * 裁剪形状枚举
     */
    public enum CropShape {
        CROP_RECT, CROP_CIRCLE
    }

    /**
     * 裁剪监听
     */
    public interface OnCropRangeListener {
        void cropRange(CropShape cropShape, RectF cropRectF);
    }
}
