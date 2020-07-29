package com.renj.imageselect.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.renj.imageselect.model.CropConstants;
import com.renj.imageselect.model.DefaultConfigData;
import com.renj.imageselect.model.ImageParamsConfig;

import java.util.logging.Logger;

import static android.graphics.Canvas.ALL_SAVE_FLAG;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-07-28   09:49
 * <p>
 * 描述：用于指定裁剪形状的控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class CropView extends View {
    // 用户绘制背景的画笔
    private Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 用于绘制边框的画笔
    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 用于绘制单元格的画笔
    private Paint cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 遮罩层颜色
    private int maskColor = DefaultConfigData.MASK_COLOR;
    // 边框颜色
    private int borderColor = DefaultConfigData.CROP_BORDER_COLOR;
    // 边框宽度
    private float borderWidth = dp2Px(DefaultConfigData.CROP_BORDER_WIDTH);
    // 裁剪宽度
    private int cropWidth = dp2Px(DefaultConfigData.WIDTH);
    // 裁剪高度
    private int cropHeight = dp2Px(DefaultConfigData.WIDTH);
    // 裁剪范围
    private RectF cropArea = new RectF();
    // 裁剪形状
    private CropShape cropShape = DefaultConfigData.IS_OVAL_CROP ? CropShape.CROP_OVAL : CropShape.CROP_RECT;
    // 控件的范围
    private RectF viewRectF = new RectF();
    // 用于计算的RectF
    private RectF calculateRectF = new RectF();
    // 需要绘制的分割线条数 小于1时表示不绘制
    private int cellLineCount = DefaultConfigData.CELL_LINE_COUNT;
    // 触摸处理类型 移动/缩放/移动+缩放/不做处理
    private int touchHandlerType = DefaultConfigData.TOUCH_HANDLER_TYPE;
    // 改变裁剪范围时,是否按照比例改变
    private boolean autoRatioScale = DefaultConfigData.AUTO_RATIO_SCALE;
    // 改变裁剪范围的宽高比例
    private int widthRatio = DefaultConfigData.SCALE_WIDTH_RATIO, heightRadio = DefaultConfigData.SCALE_HEIGHT_RATIO;

    // 按钮按下的位置
    private int downX, downY;
    // 事件的区域
    private int eventArea = CropViewHelp.CROP_VIEW_EVENT_AREA_OTHER;
    // 裁剪范围是否改变了
    private boolean cropAreaChange = false;

    public CropView(Context context) {
        this(context, null);
    }

    public CropView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        bgPaint.setStyle(Paint.Style.FILL);
        borderPaint.setStyle(Paint.Style.STROKE);
        cellPaint.setStyle(Paint.Style.STROKE);

        setPaintInfo();
    }

    // 设置画笔颜色/宽度等信息
    private void setPaintInfo() {
        bgPaint.setColor(maskColor);

        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        cellPaint.setColor(borderColor);
        cellPaint.setStrokeWidth(borderWidth / 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewRectF.set(0, 0, w, h);
        int left = (w - cropWidth) / 2;
        int top = (h - cropHeight) / 2;
        cropArea.set(left, top, left + cropWidth, top + cropHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 检查配置的数据
        checkConfigValue();

        int saveLayer = canvas.saveLayer(viewRectF, null, ALL_SAVE_FLAG);

        canvas.drawRect(viewRectF, bgPaint);
        bgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        if (cropShape == CropShape.CROP_OVAL) {
            canvas.drawOval(cropArea, bgPaint);
            if (borderWidth > 0) {
                canvas.drawOval(cropArea, borderPaint);
                if (autoRatioScale)
                    canvas.drawRect(cropArea, borderPaint);
            }
        } else {
            canvas.drawRect(cropArea, bgPaint);
            if (borderWidth > 0)
                canvas.drawRect(cropArea, borderPaint);
        }

        if (cellLineCount > 0) {
            float cellWidth = cropArea.width() / (cellLineCount + 1);
            float cellHeight = cropArea.height() / (cellLineCount + 1);

            for (int i = 0; i < cellLineCount; i++) {
                // 水平方向线
                float y = cropArea.top + (i + 1) * cellHeight;
                canvas.drawLine(cropArea.left, y, cropArea.right, y, cellPaint);
                // 垂直方向线
                float x = cropArea.left + (i + 1) * cellWidth;
                canvas.drawLine(x, cropArea.top, x, cropArea.bottom, cellPaint);
            }
        }

        bgPaint.setXfermode(null);
        canvas.restoreToCount(saveLayer);
    }

    /**
     * 设置裁剪控件参数
     *
     * @param imageParamsConfig
     */
    public void setCropViewParams(@NonNull ImageParamsConfig imageParamsConfig) {
        this.maskColor = imageParamsConfig.getMaskColor();
        this.cropWidth = dp2Px(imageParamsConfig.getWidth());
        this.cropHeight = dp2Px(imageParamsConfig.getHeight());
        this.borderColor = imageParamsConfig.getClipBorderColor();
        this.borderWidth = dp2Px(imageParamsConfig.getClipBorderWidth());
        this.cropShape = imageParamsConfig.isCircleClip() ? CropShape.CROP_OVAL : CropShape.CROP_RECT;

        setPaintInfo();
        float left = (viewRectF.width() - cropWidth) / 2;
        float top = (viewRectF.hashCode() - cropHeight) / 2;
        cropArea.set(left, top, left + cropWidth, top + cropHeight);
        postInvalidate();
    }

    /**
     * 检查配置的数据
     */
    private void checkConfigValue() {
        if (this.cropWidth > getMeasuredWidth()) this.cropWidth = getMeasuredWidth();
        if (this.cropHeight > getMeasuredHeight()) this.cropHeight = getMeasuredHeight();
        if (this.borderWidth < 0) this.borderWidth = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                eventArea = CropViewHelp.getEventAreaByDown(downX, downY, cropArea);
                if (eventArea == CropViewHelp.CROP_VIEW_EVENT_AREA_OTHER) {
                    return super.onTouchEvent(event);
                } else {
                    calculateRectF.set(cropArea);
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                float offsetX = moveX - downX;
                float offsetY = moveY - downY;
                if ((touchHandlerType == CropConstants.TOUCH_OFFSET || touchHandlerType == CropConstants.TOUCH_OFFSET_AND_SCALE)
                        && (eventArea == CropViewHelp.CROP_VIEW_EVENT_AREA_CENTER)) {
                    // 边界判断
                    CropViewHelp.moveBorderHandler(offsetX, offsetY, viewRectF.width(), viewRectF.height(), cropArea);
                    downX = moveX;
                    downY = moveY;
                    invalidate();
                    cropAreaChange = true;
                    return true;
                } else if ((touchHandlerType == CropConstants.TOUCH_SCALE || touchHandlerType == CropConstants.TOUCH_OFFSET_AND_SCALE)
                        && (CropViewHelp.calculateCropArea(eventArea, cropArea, calculateRectF,
                        viewRectF.width(), viewRectF.height(), offsetX, offsetY, autoRatioScale, widthRatio, heightRadio))) {
                    cropArea.set(calculateRectF);
                    downX = moveX;
                    downY = moveY;
                    invalidate();
                    cropAreaChange = true;
                    return true;
                }
                return super.onTouchEvent(event);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (cropAreaChange && onCropAreaChangeListener != null) {
                    onCropAreaChangeListener.onCropAreaChange(cropArea);
                }
                cropAreaChange = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    private OnCropAreaChangeListener onCropAreaChangeListener;

    public void setOnCropAreaChangeListener(OnCropAreaChangeListener onCropAreaChangeListener) {
        this.onCropAreaChangeListener = onCropAreaChangeListener;
    }

    public void confirmCrop(@NonNull OnCropRangeListener onCropRangeListener) {
        if (onCropRangeListener != null) {
            onCropRangeListener.cropRange(cropShape, cropArea);
        }
    }

    public interface OnCropAreaChangeListener {
        void onCropAreaChange(RectF cropArea);
    }

    /**
     * 裁剪监听
     */
    public interface OnCropRangeListener {
        void cropRange(CropShape cropShape, RectF cropRectF);
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
    public enum CropShape {
        /**
         * 矩形
         */
        CROP_RECT,
        /**
         * 圆形/椭圆
         */
        CROP_OVAL
    }
}
