package com.renj.imagepicker;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import com.renj.imagepicker.listener.OnResultCallBack;
import com.renj.imagepicker.model.ImagePickerMoveType;
import com.renj.imagepicker.model.RImagePickerConfigData;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.model.ImagePickerTouchType;

import java.util.Arrays;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-01-03   13:59
 * <p>
 * 描述：图片选择裁剪参数配置
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePickerParams implements Parcelable {
    /*********** 图片选择和裁剪参数 ***********/
    int width; // 裁剪宽度
    int height; // 裁剪高度
    int maxCount; // 选择图片张数
    boolean isCrop; // 是否裁剪
    boolean isOvalCrop; // 是否裁剪成圆形图片
    float minScale; // 图片最小缩放倍数
    float maxScale; // 图片最大缩放倍数
    @FloatRange(from = 0, to = 1)
    float boundaryResistance; // 边界滑动阻力系数
    float cropBorderWidth; // 裁剪线条宽度
    int cropBorderColor; // 裁剪线条颜色
    int cropMoveBoundsType; // 裁剪移动边界模式  图片控件边界做边界/裁剪控件边界做边界
    int maskColor; // 遮罩层颜色
    boolean isContinuityEnlarge; // 是否双击连续放大
    boolean isShowCamera; // 是否显示打开相机按钮
    String[] fileSuffix; // 过滤后缀名
    int cellLineCount; // 需要绘制的分割线条数 小于1时表示不绘制
    float cellBorderWidth; // 分割线条宽度
    float scalePointRadius; // 缩放点半径
    int touchHandlerType; // 触摸处理类型 移动/缩放/移动+缩放/不做处理
    boolean autoRatioScale; // 改变裁剪范围时,是否按照比例改变
    int widthRatio; // 改变裁剪范围的宽比例
    int heightRadio; // 改变裁剪范围的高比例

    int cropViewLeftTopDecorativeDrawableRes; // 裁剪视图左上角装饰图片
    int cropViewRightTopDecorativeDrawableRes; // 裁剪视图右上角装饰图片
    int cropViewRightBottomDecorativeDrawableRes; // 裁剪视图右下角装饰图片
    int cropViewLeftBottomDecorativeDrawableRes; // 裁剪视图左下角装饰图片
    int cropViewDecorativeDrawableOffset; // 装饰图片绘制偏移值，防止装饰图片占用裁剪框内容

    /*********** 结果回调 ***********/
    private OnResultCallBack onResultCallBack;

    private ImagePickerParams(Builder builder) {
        // ********** 图片选择和裁剪参数 ***********
        this.width = builder.width;
        this.height = builder.height;
        this.maxCount = builder.maxCount;
        this.isCrop = builder.isCrop;
        this.isOvalCrop = builder.isOvalCrop;
        this.minScale = builder.minScale;
        this.maxScale = builder.maxScale;
        this.boundaryResistance = builder.boundaryResistance;
        this.cropBorderWidth = builder.cropBorderWidth;
        this.cropBorderColor = builder.cropBorderColor;
        this.cropMoveBoundsType = builder.cropMoveBoundsType;
        this.maskColor = builder.maskColor;
        this.isContinuityEnlarge = builder.isContinuityEnlarge;
        this.isShowCamera = builder.isShowCamera;
        this.fileSuffix = builder.fileSuffix;
        this.cellLineCount = builder.cellLineCount;
        this.cellBorderWidth = builder.cellBorderWidth;
        this.scalePointRadius = builder.scalePointRadius;
        this.touchHandlerType = builder.touchHandlerType;
        this.autoRatioScale = builder.autoRatioScale;
        this.widthRatio = builder.widthRatio;
        this.heightRadio = builder.heightRadio;
        this.cropViewLeftTopDecorativeDrawableRes = builder.cropViewLeftTopDecorativeDrawableRes;
        this.cropViewRightTopDecorativeDrawableRes = builder.cropViewRightTopDecorativeDrawableRes;
        this.cropViewRightBottomDecorativeDrawableRes = builder.cropViewRightBottomDecorativeDrawableRes;
        this.cropViewLeftBottomDecorativeDrawableRes = builder.cropViewLeftBottomDecorativeDrawableRes;
        this.cropViewDecorativeDrawableOffset = builder.cropViewDecorativeDrawableOffset;
        this.onResultCallBack = builder.onResultCallBack;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public boolean isCrop() {
        return this.isCrop;
    }

    public boolean isOvalCrop() {
        return this.isOvalCrop;
    }

    public float getMinScale() {
        return this.minScale;
    }

    public float getMaxScale() {
        return this.maxScale;
    }

    public float getBoundaryResistance() {
        return this.boundaryResistance;
    }

    public float getCropBorderWidth() {
        return this.cropBorderWidth;
    }

    public int getCropBorderColor() {
        return this.cropBorderColor;
    }

    public int getCropMoveBoundsType() {
        return cropMoveBoundsType;
    }

    public int getMaskColor() {
        return this.maskColor;
    }

    public boolean isContinuityEnlarge() {
        return this.isContinuityEnlarge;
    }

    public boolean isShowCamera() {
        return this.isShowCamera;
    }

    public List<String> getFileSuffix() {
        return (fileSuffix == null || fileSuffix.length <= 0) ? null : Arrays.asList(fileSuffix);
    }

    public int getCellLineCount() {
        return cellLineCount;
    }

    public float getCellBorderWidth() {
        return cellBorderWidth;
    }

    public float getScalePointRadius() {
        return scalePointRadius;
    }

    public int getTouchHandlerType() {
        return touchHandlerType;
    }

    public boolean isAutoRatioScale() {
        return autoRatioScale;
    }

    public int getWidthRatio() {
        return widthRatio;
    }

    public int getHeightRadio() {
        return heightRadio;
    }

    public int getCropViewLeftTopDecorativeDrawableRes() {
        return cropViewLeftTopDecorativeDrawableRes;
    }

    public int getCropViewRightTopDecorativeDrawableRes() {
        return cropViewRightTopDecorativeDrawableRes;
    }

    public int getCropViewRightBottomDecorativeDrawableRes() {
        return cropViewRightBottomDecorativeDrawableRes;
    }

    public int getCropViewLeftBottomDecorativeDrawableRes() {
        return cropViewLeftBottomDecorativeDrawableRes;
    }

    public int getCropViewDecorativeDrawableOffset() {
        return cropViewDecorativeDrawableOffset;
    }

    OnResultCallBack getOnResultCallBack() {
        return onResultCallBack;
    }

    public static class Builder {
        /*********** 图片选择和裁剪参数 ***********/
        private int width; // 裁剪宽度
        private int height; // 裁剪高度
        private int maxCount; // 选择图片张数
        private boolean isCrop; // 是否裁剪
        private boolean isOvalCrop; // 是否裁剪成圆形图片
        private float minScale; // 图片最小缩放倍数
        private float maxScale; // 图片最大缩放倍数
        @FloatRange(from = 0, to = 1)
        private float boundaryResistance; // 边界滑动阻力系数
        private float cropBorderWidth; // 裁剪线条宽度
        private int cropBorderColor; // 裁剪线条颜色
        private int cropMoveBoundsType; // 裁剪移动边界模式  图片控件边界做边界/裁剪控件边界做边界
        private int maskColor; // 遮罩层颜色
        private boolean isContinuityEnlarge; // 是否双击连续放大
        private boolean isShowCamera; // 是否显示打开相机按钮
        private String[] fileSuffix; // 后缀名
        private int cellLineCount; // 需要绘制的分割线条数 小于1时表示不绘制
        private float cellBorderWidth; // 分割线条宽度
        private float scalePointRadius; // 缩放点半径
        private int touchHandlerType; // 触摸处理类型 移动/缩放/移动+缩放/不做处理
        private boolean autoRatioScale; // 改变裁剪范围时,是否按照比例改变
        private int widthRatio; // 改变裁剪范围的宽比例
        private int heightRadio; // 改变裁剪范围的高比例

        private int cropViewLeftTopDecorativeDrawableRes; // 裁剪视图左上角装饰图片
        private int cropViewRightTopDecorativeDrawableRes; // 裁剪视图右上角装饰图片
        private int cropViewRightBottomDecorativeDrawableRes; // 裁剪视图右下角装饰图片
        private int cropViewLeftBottomDecorativeDrawableRes; // 裁剪视图左下角装饰图片
        private int cropViewDecorativeDrawableOffset; // 装饰图片绘制偏移值，防止装饰图片占用裁剪框内容

        /*********** 结果回调 ***********/
        private OnResultCallBack onResultCallBack;

        public Builder() {
            // *********** 图片选择和裁剪参数 ***********
            this.width = RImagePickerConfigData.WIDTH; // 裁剪宽度
            this.height = RImagePickerConfigData.HEIGHT; // 裁剪高度
            this.maxCount = RImagePickerConfigData.SELECT_COUNT; // 选择图片张数
            this.isCrop = RImagePickerConfigData.IS_CROP; // 是否裁剪
            this.isOvalCrop = RImagePickerConfigData.IS_OVAL_CROP; // 是否裁剪成圆形图片
            this.minScale = RImagePickerConfigData.MIN_SCALE; // 图片最小缩放倍数
            this.maxScale = RImagePickerConfigData.MAX_SCALE; // 图片最大缩放倍数
            this.boundaryResistance = RImagePickerConfigData.BOUNDARY_RESISTANCE; // 边界滑动阻力系数
            this.cropBorderWidth = RImagePickerConfigData.CROP_BORDER_WIDTH; // 裁剪线条宽度
            this.cropBorderColor = RImagePickerConfigData.CROP_BORDER_COLOR; // 裁剪线条颜色
            this.cropMoveBoundsType = RImagePickerConfigData.CROP_MOVE_BOUNDS_TYPE; // 裁剪移动边界模式
            this.maskColor = RImagePickerConfigData.MASK_COLOR; // 遮罩层颜色
            this.isContinuityEnlarge = RImagePickerConfigData.IS_CONTINUITY_ENLARGE; // 是否双击连续放大
            this.isShowCamera = RImagePickerConfigData.IS_SHOW_CAMERA; // 是否显示打开相机按钮
            this.fileSuffix = null; // 后缀名
            this.cellLineCount = RImagePickerConfigData.CELL_LINE_COUNT; // 需要绘制的分割线条数 小于1时表示不绘制
            this.cellBorderWidth = RImagePickerConfigData.CROP_CELL_BORDER_WIDTH; // 分割线条宽度
            this.scalePointRadius = RImagePickerConfigData.CROP_SCALE_POINT_RADIUS; // 缩放点半径
            this.touchHandlerType = RImagePickerConfigData.TOUCH_HANDLER_TYPE; // 触摸处理类型 移动/缩放/移动+缩放/不做处理
            this.autoRatioScale = RImagePickerConfigData.AUTO_RATIO_SCALE; // 改变裁剪范围时,是否按照比例改变
            this.widthRatio = RImagePickerConfigData.SCALE_WIDTH_RATIO; // 改变裁剪范围的宽比例
            this.heightRadio = RImagePickerConfigData.SCALE_HEIGHT_RATIO; // 改变裁剪范围的高比例

            this.cropViewLeftTopDecorativeDrawableRes = 0;
            this.cropViewRightTopDecorativeDrawableRes = 0;
            this.cropViewRightBottomDecorativeDrawableRes = 0;
            this.cropViewLeftBottomDecorativeDrawableRes = 0;

            // *********** 结果回调 ***********
            this.onResultCallBack = null;
        }

        /**
         * 设置裁剪宽度，单位 dp；默认 200dp
         *
         * @param width 裁剪宽
         * @return 链式调用对象
         */
        public Builder width(int width) {
            this.width = width;
            return this;
        }

        /**
         * 设置裁剪高度，单位 dp；默认 200dp
         *
         * @param height 裁剪高度
         * @return 链式调用对象
         */
        public Builder height(int height) {
            this.height = height;
            return this;
        }

        /**
         * 设置图片选择张数，默认1张
         *
         * @param maxCount 需要选择的图片张数<br/>
         *                 <b>注意：该值与结果监听 {@link OnResultCallBack} 对象的泛型相关：</b>
         *                 <br/>&nbsp;&nbsp;&nbsp;&nbsp;
         *                 <b>1.当选择或裁剪的只是单张图片(maxCount = 1)时，泛型应该为 {@link ImagePickerModel}</b>
         *                 <br/>&nbsp;&nbsp;&nbsp;&nbsp;
         *                 <b>2.当选择或裁剪的只是多张图片(maxCount > 1)时，泛型应该为 List<{@link ImagePickerModel}></{@link></b>
         * @return 链式调用对象
         */
        public Builder maxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        /**
         * 设置是否需要裁剪
         *
         * @param isCrop true：需要；false：不需要
         * @return 链式调用对象
         */
        public Builder isCrop(boolean isCrop) {
            this.isCrop = isCrop;
            return this;
        }

        /**
         * 设置是否需要裁剪成圆形<br/>
         * <b>注意：圆形的半径为裁剪宽度、裁剪高度较小值得一半</b>
         *
         * @param isOvalCrop true：需要；false：不需要
         * @return 链式调用对象
         */
        public Builder isOvalCrop(boolean isOvalCrop) {
            this.isOvalCrop = isOvalCrop;
            return this;
        }

        /**
         * 设置图片的最小缩放比例
         *
         * @param minScale 最小缩放比例
         * @return 链式调用对象
         */
        public Builder minScale(float minScale) {
            this.minScale = minScale;
            return this;
        }

        /**
         * 设置图片的最大缩放比例
         *
         * @param maxScale 最大缩放比例
         * @return 链式调用对象
         */
        public Builder maxScale(float maxScale) {
            this.maxScale = maxScale;
            return this;
        }

        /**
         * 设置边界滑动阻力系数，(达到边界时增加滑动阻力，图片实际移动距离和手指移动距离的比值[0-1]，值越大，图片实际移动距离和手指移动距离差值越小; 默认0.25)
         *
         * @param boundaryResistance 边界滑动阻力系数
         * @return 链式调用对象
         */
        public Builder boundaryResistance(@FloatRange(from = 0, to = 1) float boundaryResistance) {
            this.boundaryResistance = boundaryResistance;
            return this;
        }

        /**
         * 设置裁剪边框宽度
         *
         * @param cropBorderWidth 裁剪边框宽度
         * @return 链式调用对象
         */
        public Builder cropBorderWidth(float cropBorderWidth) {
            this.cropBorderWidth = cropBorderWidth;
            return this;
        }

        /**
         * 设置裁剪边框颜色
         *
         * @param cropBorderColor 裁剪边框颜色
         * @return 链式调用对象
         */
        public Builder cropBorderColor(int cropBorderColor) {
            this.cropBorderColor = cropBorderColor;
            return this;
        }

        /**
         * 设置裁剪移动边界模式
         *
         * @param cropMoveBoundsType 裁剪时图片拖动范围类型 显示图片的控件范围内移动，默认值/以裁剪框范围的边界作为图片移动的边界值
         *                           {@link ImagePickerMoveType#MOVE_IMAGE_VIEW}
         *                           /{@link ImagePickerMoveType#MOVE_CROP_VIEW}
         * @return 链式调用对象
         */
        public Builder cropMoveBoundsType(@ImagePickerMoveType int cropMoveBoundsType) {
            this.cropMoveBoundsType = cropMoveBoundsType;
            return this;
        }

        /**
         * 设置遮罩层颜色
         *
         * @param maskColor 遮罩层颜色
         * @return 链式调用对象
         */
        public Builder maskColor(int maskColor) {
            this.maskColor = maskColor;
            return this;
        }

        /**
         * 设置是否双击连续放大
         *
         * @param isContinuityEnlarge true：是；false：否  当设置为 true 时，双击时若图片的缩放小于2时变为2，当图片的缩放倍数在[2,4)时变为4
         * @return 链式调用对象
         */
        public Builder isContinuityEnlarge(boolean isContinuityEnlarge) {
            this.isContinuityEnlarge = isContinuityEnlarge;
            return this;
        }

        /**
         * 设置是否显示打开相机图片，默认显示
         *
         * @param isShowCamera true：显示 false：不显示
         * @return 链式调用对象
         */
        public Builder isShowCamera(boolean isShowCamera) {
            this.isShowCamera = isShowCamera;
            return this;
        }

        /**
         * 指定文件后缀名
         *
         * @param fileSuffix 显示的文件后缀
         * @return 链式调用对象
         */
        public Builder fileSuffix(String... fileSuffix) {
            this.fileSuffix = fileSuffix;
            return this;
        }

        /**
         * 设置需要绘制的分割线条数 小于1时表示不绘制
         *
         * @param cellLineCount 需要绘制的分割线条数 小于1时表示不绘制
         * @return 链式调用对象
         */
        public Builder cellLineCount(int cellLineCount) {
            this.cellLineCount = cellLineCount;
            return this;
        }

        /**
         * 设置分割线条宽度
         *
         * @param cellBorderWidth 分割线条宽度
         * @return 链式调用对象
         */
        public Builder cellBorderWidth(float cellBorderWidth) {
            this.cellBorderWidth = cellBorderWidth;
            return this;
        }

        /**
         * 设置缩放点半径
         *
         * @param scalePointRadius 缩放点半径
         * @return 链式调用对象
         */
        public Builder scalePointRadius(float scalePointRadius) {
            this.scalePointRadius = scalePointRadius;
            return this;
        }

        /**
         * 设置触摸处理类型 移动/缩放/移动+缩放/不做处理
         *
         * @param touchHandlerType 触摸处理类型 移动/缩放/移动+缩放/不做处理
         *                         {@link ImagePickerTouchType#TOUCH_OFFSET}
         *                         /{@link ImagePickerTouchType#TOUCH_OFFSET}
         *                         /{@link ImagePickerTouchType#TOUCH_OFFSET_AND_SCALE}
         *                         /{@link ImagePickerTouchType#TOUCH_NONE}
         * @return 链式调用对象
         */
        public Builder touchHandlerType(@ImagePickerTouchType int touchHandlerType) {
            this.touchHandlerType = touchHandlerType;
            return this;
        }

        /**
         * 设置 改变裁剪范围时,是否按照比例改变
         *
         * @param autoRatioScale 改变裁剪范围时,是否按照比例改变 true：是
         * @return 链式调用对象
         */
        public Builder autoRatioScale(boolean autoRatioScale) {
            this.autoRatioScale = autoRatioScale;
            return this;
        }

        /**
         * 设置改变裁剪范围的宽高比例
         *
         * @param widthRatio  改变裁剪范围的宽比例
         * @param heightRadio 改变裁剪范围的高比例
         * @return 链式调用对象
         */
        public Builder widthAndHeightRadio(int widthRatio, int heightRadio) {
            this.widthRatio = widthRatio;
            this.heightRadio = heightRadio;
            return this;
        }

        /**
         * 设置裁剪视图四个角的装饰图
         *
         * @param cropViewLeftTopDecorativeDrawable     裁剪视图左上角装饰图资源
         * @param cropViewRightTopDecorativeDrawable    裁剪视图右上角装饰图资源
         * @param cropViewRightBottomDecorativeDrawable 裁剪视图右下角装饰图资源
         * @param cropViewLeftBottomDecorativeDrawable  裁剪视图左下角装饰图资源
         * @return 链式调用对象
         */
        public Builder cropViewDecorativeDrawable(@DrawableRes int cropViewLeftTopDecorativeDrawable,
                                                  @DrawableRes int cropViewRightTopDecorativeDrawable,
                                                  @DrawableRes int cropViewRightBottomDecorativeDrawable,
                                                  @DrawableRes int cropViewLeftBottomDecorativeDrawable) {
            return cropViewDecorativeDrawable(cropViewLeftTopDecorativeDrawable, cropViewRightTopDecorativeDrawable,
                    cropViewRightBottomDecorativeDrawable, cropViewLeftBottomDecorativeDrawable, 4);
        }

        /**
         * 设置裁剪视图四个角的装饰图
         *
         * @param cropViewLeftTopDecorativeDrawable     裁剪视图左上角装饰图资源
         * @param cropViewRightTopDecorativeDrawable    裁剪视图右上角装饰图资源
         * @param cropViewRightBottomDecorativeDrawable 裁剪视图右下角装饰图资源
         * @param cropViewLeftBottomDecorativeDrawable  裁剪视图左下角装饰图资源
         * @param cropViewDecorativeDrawableOffset      装饰图片绘制偏移值，防止装饰图片占用裁剪框内容 单位：dp
         * @return 链式调用对象
         */
        public Builder cropViewDecorativeDrawable(@DrawableRes int cropViewLeftTopDecorativeDrawable,
                                                  @DrawableRes int cropViewRightTopDecorativeDrawable,
                                                  @DrawableRes int cropViewRightBottomDecorativeDrawable,
                                                  @DrawableRes int cropViewLeftBottomDecorativeDrawable,
                                                  int cropViewDecorativeDrawableOffset) {
            this.cropViewLeftTopDecorativeDrawableRes = cropViewLeftTopDecorativeDrawable;
            this.cropViewRightTopDecorativeDrawableRes = cropViewRightTopDecorativeDrawable;
            this.cropViewRightBottomDecorativeDrawableRes = cropViewRightBottomDecorativeDrawable;
            this.cropViewLeftBottomDecorativeDrawableRes = cropViewLeftBottomDecorativeDrawable;
            this.cropViewDecorativeDrawableOffset = cropViewDecorativeDrawableOffset;
            return this;
        }

        /**
         * 获取返回结果的回调<br/>
         * <b>{@link OnResultCallBack} 注意：当选择一张图片时，集合的大小为1</b>
         *
         * @param onResultCallBack 结果回调
         */
        public Builder onResult(@NonNull OnResultCallBack onResultCallBack) {
            this.onResultCallBack = onResultCallBack;
            return this;
        }

        /**
         * 构建 {@link ImagePickerParams} 对象
         *
         * @return {@link ImagePickerParams} 对象
         */
        public ImagePickerParams build() {
            return new ImagePickerParams(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.maxCount);
        dest.writeByte(this.isCrop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOvalCrop ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.minScale);
        dest.writeFloat(this.maxScale);
        dest.writeFloat(this.boundaryResistance);
        dest.writeFloat(this.cropBorderWidth);
        dest.writeInt(this.cropBorderColor);
        dest.writeInt(this.cropMoveBoundsType);
        dest.writeInt(this.maskColor);
        dest.writeByte(this.isContinuityEnlarge ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowCamera ? (byte) 1 : (byte) 0);
        dest.writeStringArray(this.fileSuffix);
        dest.writeInt(this.cellLineCount);
        dest.writeFloat(this.cellBorderWidth);
        dest.writeFloat(this.scalePointRadius);
        dest.writeInt(this.touchHandlerType);
        dest.writeByte(this.autoRatioScale ? (byte) 1 : (byte) 0);
        dest.writeInt(this.widthRatio);
        dest.writeInt(this.heightRadio);
        dest.writeInt(this.cropViewLeftTopDecorativeDrawableRes);
        dest.writeInt(this.cropViewRightTopDecorativeDrawableRes);
        dest.writeInt(this.cropViewRightBottomDecorativeDrawableRes);
        dest.writeInt(this.cropViewLeftBottomDecorativeDrawableRes);
        dest.writeInt(this.cropViewDecorativeDrawableOffset);
    }

    public void readFromParcel(Parcel source) {
        this.width = source.readInt();
        this.height = source.readInt();
        this.maxCount = source.readInt();
        this.isCrop = source.readByte() != 0;
        this.isOvalCrop = source.readByte() != 0;
        this.minScale = source.readFloat();
        this.maxScale = source.readFloat();
        this.boundaryResistance = source.readFloat();
        this.cropBorderWidth = source.readFloat();
        this.cropBorderColor = source.readInt();
        this.cropMoveBoundsType = source.readInt();
        this.maskColor = source.readInt();
        this.isContinuityEnlarge = source.readByte() != 0;
        this.isShowCamera = source.readByte() != 0;
        this.fileSuffix = source.createStringArray();
        this.cellLineCount = source.readInt();
        this.cellBorderWidth = source.readFloat();
        this.scalePointRadius = source.readFloat();
        this.touchHandlerType = source.readInt();
        this.autoRatioScale = source.readByte() != 0;
        this.widthRatio = source.readInt();
        this.heightRadio = source.readInt();
        this.cropViewLeftTopDecorativeDrawableRes = source.readInt();
        this.cropViewRightTopDecorativeDrawableRes = source.readInt();
        this.cropViewRightBottomDecorativeDrawableRes = source.readInt();
        this.cropViewLeftBottomDecorativeDrawableRes = source.readInt();
        this.cropViewDecorativeDrawableOffset = source.readInt();
    }

    protected ImagePickerParams(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.maxCount = in.readInt();
        this.isCrop = in.readByte() != 0;
        this.isOvalCrop = in.readByte() != 0;
        this.minScale = in.readFloat();
        this.maxScale = in.readFloat();
        this.boundaryResistance = in.readFloat();
        this.cropBorderWidth = in.readFloat();
        this.cropBorderColor = in.readInt();
        this.cropMoveBoundsType = in.readInt();
        this.maskColor = in.readInt();
        this.isContinuityEnlarge = in.readByte() != 0;
        this.isShowCamera = in.readByte() != 0;
        this.fileSuffix = in.createStringArray();
        this.cellLineCount = in.readInt();
        this.cellBorderWidth = in.readFloat();
        this.scalePointRadius = in.readFloat();
        this.touchHandlerType = in.readInt();
        this.autoRatioScale = in.readByte() != 0;
        this.widthRatio = in.readInt();
        this.heightRadio = in.readInt();
        this.cropViewLeftTopDecorativeDrawableRes = in.readInt();
        this.cropViewRightTopDecorativeDrawableRes = in.readInt();
        this.cropViewRightBottomDecorativeDrawableRes = in.readInt();
        this.cropViewLeftBottomDecorativeDrawableRes = in.readInt();
        this.cropViewDecorativeDrawableOffset = in.readInt();
    }

    public static final Parcelable.Creator<ImagePickerParams> CREATOR = new Parcelable.Creator<ImagePickerParams>() {
        @Override
        public ImagePickerParams createFromParcel(Parcel source) {
            return new ImagePickerParams(source);
        }

        @Override
        public ImagePickerParams[] newArray(int size) {
            return new ImagePickerParams[size];
        }
    };
}
