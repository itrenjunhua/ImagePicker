package com.renj.imagepicker.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.renj.imagepicker.listener.OnCropImageChange;
import com.renj.imagepicker.listener.OnResultCallBack;
import com.renj.imagepicker.utils.ImagePickerHelp;

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
    int selectCount; // 选择图片张数
    boolean isCrop; // 是否裁剪
    boolean isOvalCrop; // 是否裁剪成圆形图片
    float minScale; // 图片最小缩放倍数
    float maxScale; // 图片最大缩放倍数
    @FloatRange(from = 0, to = 1)
    float boundaryResistance; // 边界滑动阻力系数
    float cropBorderWidth; // 裁剪线条宽度
    int cropBorderColor; // 裁剪线条颜色
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

    /*********** 选择图片页面动态布局 ***********/
    @LayoutRes
    private int selectedLayoutId; // 选择图片页面布局资源id
    @LayoutRes
    private int itemCameraLayoutId; // 选择图片条目布局资源文件(点击打开相机条目)
    @LayoutRes
    private int itemImageLayoutId; // 选择图片条目布局资源文件(图片显示条目)

    /*********** 裁剪图片页面动态布局 ***********/
    @LayoutRes
    private int cropSingleLayoutId; // 裁剪单张图片页面布局资源 id
    @LayoutRes
    private int cropMoreLayoutId; // 裁剪单张图片页面布局资源 id


    private ImagePickerParams(Builder builder) {
        /*********** 图片选择和裁剪参数 ***********/
        this.width = builder.width;
        this.height = builder.height;
        this.selectCount = builder.selectCount;
        this.isCrop = builder.isCrop;
        this.isOvalCrop = builder.isOvalCrop;
        this.minScale = builder.minScale;
        this.maxScale = builder.maxScale;
        this.boundaryResistance = builder.boundaryResistance;
        this.cropBorderWidth = builder.cropBorderWidth;
        this.cropBorderColor = builder.cropBorderColor;
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

        /*********** 选择图片页面动态布局和回调 ***********/
        this.selectedLayoutId = builder.selectedLayoutId;
        this.itemCameraLayoutId = builder.itemCameraLayoutId;
        this.itemImageLayoutId = builder.itemImageLayoutId;

        /*********** 裁剪图片页面动态布局和回调 ***********/
        this.cropSingleLayoutId = builder.cropSingleLayoutId;
        this.cropMoreLayoutId = builder.cropMoreLayoutId;
        ImagePickerHelp.getInstance().setOnCropImageChange(builder.onCropImageChange);

        /*********** 结果回调 ***********/
        ImagePickerHelp.getInstance().setOnResultCallBack(builder.onResultCallBack);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getSelectCount() {
        return this.selectCount;
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

    public int getSelectedLayoutId() {
        return selectedLayoutId;
    }

    public int getItemCameraLayoutId() {
        return itemCameraLayoutId;
    }

    public int getItemImageLayoutId() {
        return itemImageLayoutId;
    }


    public int getCropSingleLayoutId() {
        return cropSingleLayoutId;
    }

    public int getCropMoreLayoutId() {
        return cropMoreLayoutId;
    }

    public static class Builder {
        /*********** 图片选择和裁剪参数 ***********/
        private int width; // 裁剪宽度
        private int height; // 裁剪高度
        private int selectCount; // 选择图片张数
        private boolean isCrop; // 是否裁剪
        private boolean isOvalCrop; // 是否裁剪成圆形图片
        private float minScale; // 图片最小缩放倍数
        private float maxScale; // 图片最大缩放倍数
        @FloatRange(from = 0, to = 1)
        private float boundaryResistance; // 边界滑动阻力系数
        private float cropBorderWidth; // 裁剪线条宽度
        private int cropBorderColor; // 裁剪线条颜色
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

        /*********** 选择图片页面动态布局和回调 ***********/
        @LayoutRes
        private int selectedLayoutId; // 选择图片页面布局资源id
        @LayoutRes
        private int itemCameraLayoutId; // 选择图片条目布局资源文件(点击打开相机条目)
        @LayoutRes
        private int itemImageLayoutId; // 选择图片条目布局资源文件(图片显示条目)

        /*********** 裁剪图片页面动态布局和回调 ***********/
        @LayoutRes
        private int cropSingleLayoutId; // 裁剪单张图片页面布局资源 id
        @LayoutRes
        private int cropMoreLayoutId; // 裁剪单张图片页面布局资源 id
        private OnCropImageChange onCropImageChange; // 图片发生裁剪时回调

        /*********** 结果回调 ***********/
        private OnResultCallBack onResultCallBack;

        public Builder() {
            /*********** 图片选择和裁剪参数 ***********/
            this.width = DefaultConfigData.WIDTH; // 裁剪宽度
            this.height = DefaultConfigData.HEIGHT; // 裁剪高度
            this.selectCount = DefaultConfigData.SELECT_COUNT; // 选择图片张数
            this.isCrop = DefaultConfigData.IS_CROP; // 是否裁剪
            this.isOvalCrop = DefaultConfigData.IS_OVAL_CROP; // 是否裁剪成圆形图片
            this.minScale = DefaultConfigData.MIN_SCALE; // 图片最小缩放倍数
            this.maxScale = DefaultConfigData.MAX_SCALE; // 图片最大缩放倍数
            this.boundaryResistance = DefaultConfigData.BOUNDARY_RESISTANCE; // 边界滑动阻力系数
            this.cropBorderWidth = DefaultConfigData.CROP_BORDER_WIDTH; // 裁剪线条宽度
            this.cropBorderColor = DefaultConfigData.CROP_BORDER_COLOR; // 裁剪线条颜色
            this.maskColor = DefaultConfigData.MASK_COLOR; // 遮罩层颜色
            this.isContinuityEnlarge = DefaultConfigData.IS_CONTINUITY_ENLARGE; // 是否双击连续放大
            this.isShowCamera = DefaultConfigData.IS_SHOW_CAMERA; // 是否显示打开相机按钮
            this.fileSuffix = null; // 后缀名
            this.cellLineCount = DefaultConfigData.CELL_LINE_COUNT; // 需要绘制的分割线条数 小于1时表示不绘制
            this.cellBorderWidth = DefaultConfigData.CROP_CELL_BORDER_WIDTH; // 分割线条宽度
            this.scalePointRadius = DefaultConfigData.CROP_SCALE_POINT_RADIUS; // 缩放点半径
            this.touchHandlerType = DefaultConfigData.TOUCH_HANDLER_TYPE; // 触摸处理类型 移动/缩放/移动+缩放/不做处理
            this.autoRatioScale = DefaultConfigData.AUTO_RATIO_SCALE; // 改变裁剪范围时,是否按照比例改变
            this.widthRatio = DefaultConfigData.SCALE_WIDTH_RATIO; // 改变裁剪范围的宽比例
            this.heightRadio = DefaultConfigData.SCALE_HEIGHT_RATIO; // 改变裁剪范围的高比例

            /*********** 选择图片页面动态布局和回调 ***********/
            this.selectedLayoutId = DefaultConfigData.SELECTED_IMAGE_LAYOUT;
            this.itemCameraLayoutId = DefaultConfigData.SELECTED_IMAGE_ITEM_CAMERA_LAYOUT;
            this.itemImageLayoutId = DefaultConfigData.SELECTED_IMAGE_ITEM_IMAGE_LAYOUT;

            /*********** 裁剪图片页面动态布局和回调 ***********/
            this.cropSingleLayoutId = DefaultConfigData.CROP_SINGLE_LAYOUT;
            this.cropMoreLayoutId = DefaultConfigData.CROP_MORE_LAYOUT;
            this.onCropImageChange = null;

            /*********** 结果回调 ***********/
            this.onResultCallBack = null;
        }

        /**
         * 设置裁剪宽度，单位 dp；默认 200dp
         *
         * @param width 裁剪宽
         * @return
         */
        public Builder width(int width) {
            this.width = width;
            return this;
        }

        /**
         * 设置裁剪高度，单位 dp；默认 200dp
         *
         * @param height 裁剪高度
         * @return
         */
        public Builder height(int height) {
            this.height = height;
            return this;
        }

        /**
         * 设置图片选择张数，默认1张
         *
         * @param selectCount 需要选择的图片张数<br/>
         *                    <b>注意：该值与结果监听 {@link OnResultCallBack} 对象的泛型相关：</b>
         *                    <br/>&nbsp;&nbsp;&nbsp;&nbsp;
         *                    <b>1.当选择或裁剪的只是单张图片(selectCount = 1)时，泛型应该为 {@link com.renj.imagepicker.model.ImageModel}</b>
         *                    <br/>&nbsp;&nbsp;&nbsp;&nbsp;
         *                    <b>2.当选择或裁剪的只是多张图片(selectCount > 1)时，泛型应该为 List<{@link com.renj.imagepicker.model.ImageModel}></{@link></b>
         * @return
         */
        public Builder selectCount(int selectCount) {
            this.selectCount = selectCount;
            return this;
        }

        /**
         * 设置是否需要裁剪
         *
         * @param isCrop true：需要；false：不需要
         * @return
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
         * @return
         */
        public Builder isOvalCrop(boolean isOvalCrop) {
            this.isOvalCrop = isOvalCrop;
            return this;
        }

        /**
         * 设置图片的最小缩放比例
         *
         * @param minScale 最小缩放比例
         * @return
         */
        public Builder minScale(float minScale) {
            this.minScale = minScale;
            return this;
        }

        /**
         * 设置图片的最大缩放比例
         *
         * @param maxScale 最大缩放比例
         * @return
         */
        public Builder maxScale(float maxScale) {
            this.maxScale = maxScale;
            return this;
        }

        /**
         * 设置边界滑动阻力系数，(达到边界时增加滑动阻力，图片实际移动距离和手指移动距离的比值[0-1]，值越大，图片实际移动距离和手指移动距离差值越小; 默认0.25)
         *
         * @param boundaryResistance 边界滑动阻力系数
         * @return
         */
        public Builder boundaryResistance(@FloatRange(from = 0, to = 1) float boundaryResistance) {
            this.boundaryResistance = boundaryResistance;
            return this;
        }

        /**
         * 设置裁剪边框宽度
         *
         * @param cropBorderWidth 裁剪边框宽度
         * @return
         */
        public Builder cropBorderWidth(float cropBorderWidth) {
            this.cropBorderWidth = cropBorderWidth;
            return this;
        }

        /**
         * 设置裁剪边框颜色
         *
         * @param cropBorderColor 裁剪边框颜色
         * @return
         */
        public Builder cropBorderColor(int cropBorderColor) {
            this.cropBorderColor = cropBorderColor;
            return this;
        }

        /**
         * 设置遮罩层颜色
         *
         * @param maskColor 遮罩层颜色
         * @return
         */
        public Builder maskColor(int maskColor) {
            this.maskColor = maskColor;
            return this;
        }

        /**
         * 设置是否双击连续放大
         *
         * @param isContinuityEnlarge true：是；false：否  当设置为 true 时，双击时若图片的缩放小于2时变为2，当图片的缩放倍数在[2,4)时变为4
         * @return
         */
        public Builder isContinuityEnlarge(boolean isContinuityEnlarge) {
            this.isContinuityEnlarge = isContinuityEnlarge;
            return this;
        }

        /**
         * 设置是否显示打开相机图片，默认显示
         *
         * @param isShowCamera true：显示 false：不显示
         * @return
         */
        public Builder isShowCamera(boolean isShowCamera) {
            this.isShowCamera = isShowCamera;
            return this;
        }

        /**
         * 指定文件后缀名
         *
         * @param fileSuffix 显示的文件后缀
         * @return
         */
        public Builder fileSuffix(String... fileSuffix) {
            this.fileSuffix = fileSuffix;
            return this;
        }

        /**
         * 设置需要绘制的分割线条数 小于1时表示不绘制
         *
         * @param cellLineCount 需要绘制的分割线条数 小于1时表示不绘制
         * @return
         */
        public Builder cellLineCount(int cellLineCount) {
            this.cellLineCount = cellLineCount;
            return this;
        }

        /**
         * 设置分割线条宽度
         *
         * @param cellBorderWidth 分割线条宽度
         * @return
         */
        public Builder cellBorderWidth(float cellBorderWidth) {
            this.cellBorderWidth = cellBorderWidth;
            return this;
        }

        /**
         * 设置缩放点半径
         *
         * @param scalePointRadius 缩放点半径
         * @return
         */
        public Builder scalePointRadius(float scalePointRadius) {
            this.scalePointRadius = scalePointRadius;
            return this;
        }

        /**
         * 设置触摸处理类型 移动/缩放/移动+缩放/不做处理
         *
         * @param touchHandlerType 触摸处理类型 移动/缩放/移动+缩放/不做处理
         *                         {@link CropConstants#TOUCH_OFFSET}
         *                         /{@link CropConstants#TOUCH_OFFSET}
         *                         /{@link CropConstants#TOUCH_OFFSET_AND_SCALE}
         *                         /{@link CropConstants#TOUCH_NONE}
         * @return
         */
        public Builder touchHandlerType(int touchHandlerType) {
            this.touchHandlerType = touchHandlerType;
            return this;
        }

        /**
         * 设置 改变裁剪范围时,是否按照比例改变
         *
         * @param autoRatioScale 改变裁剪范围时,是否按照比例改变 true：是
         * @return
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
         * @return
         */
        public Builder widthAndHeightRadio(int widthRatio, int heightRadio) {
            this.widthRatio = widthRatio;
            this.heightRadio = heightRadio;
            return this;
        }

        /**
         * 动态设置图片选择页面的布局。<br/>
         * <b>注意：请参照 默认布局文件 image_picker_layout.xml ，在默认布局文件中有 id 的控件为必须控件，
         * 在自定义的布局文件中必须存在，并且要保证控件类型和id与默认布局文件中的一致，否则抛出异常。</b>
         *
         * @param selectedLayoutId 布局文件资源id
         * @return
         */
        public Builder selectedLayoutId(@LayoutRes int selectedLayoutId) {
            this.selectedLayoutId = selectedLayoutId;
            return this;
        }

        /**
         * 动态设置选择图片条目布局资源文件(点击打开相机条目)。<br/>
         * <b>注意：请参照 默认布局文件 image_select_camera_item.xml ，在默认布局文件中有 id 的控件为必须控件，
         * 在自定义的布局文件中必须存在，并且要保证控件类型和id与默认布局文件中的一致，否则抛出异常。</b>
         *
         * @param itemCameraLayoutId 布局文件资源id
         * @return
         */
        public Builder selectItemCameraLayoutId(@LayoutRes int itemCameraLayoutId) {
            this.itemCameraLayoutId = itemCameraLayoutId;
            return this;
        }

        /**
         * 动态设置选择图片条目布局资源文件(图片显示条目)。<br/>
         * <b>注意：请参照 默认布局文件 image_select_item.xml ，在默认布局文件中有 id 的控件为必须控件，
         * 在自定义的布局文件中必须存在，并且要保证控件类型和id与默认布局文件中的一致，否则抛出异常。</b>
         *
         * @param itemImageLayoutId 布局文件资源id
         * @return
         */
        public Builder selectItemImageLayoutId(@LayoutRes int itemImageLayoutId) {
            this.itemImageLayoutId = itemImageLayoutId;
            return this;
        }

        /**
         * 动态设置单张图片裁剪页面的布局。<br/>
         * <b>注意：请参照 默认布局文件 image_crop_single_layout.xml ，在默认布局文件中有 id 的控件为必须控件，
         * 在自定义的布局文件中必须存在，并且要保证控件类型和id与默认布局文件中的一致，否则抛出异常。</b>
         *
         * @param cropSingleLayoutId 布局文件资源id(如果异常，使用默认布局文件 image_crop_single_layout.xml)
         * @return
         */
        public Builder cropSingleLayoutId(@LayoutRes int cropSingleLayoutId) {
            this.cropSingleLayoutId = cropSingleLayoutId;
            return this;
        }

        /**
         * 动态设置多张图片裁剪页面的布局。<br/>
         * <b>注意：请参照 默认布局文件 image_crop_more_layout.xml ，在默认布局文件中有 id 的控件为必须控件，
         * 在自定义的布局文件中必须存在，并且要保证控件类型和id与默认布局文件中的一致，否则抛出异常。</b>
         *
         * @param cropMoreLayoutId 布局文件资源id(如果异常，使用默认布局文件 image_crop_more_layout.xml)
         * @return
         */
        public Builder cropMoreLayoutId(@LayoutRes int cropMoreLayoutId) {
            this.cropMoreLayoutId = cropMoreLayoutId;
            return this;
        }

        /**
         * 设置图片裁剪改变监听。<br/>
         *
         * @param onCropImageChange 图片裁剪时回调
         * @return
         */
        public Builder onCropImageChange(@Nullable OnCropImageChange onCropImageChange) {
            this.onCropImageChange = onCropImageChange;
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
        dest.writeInt(this.selectCount);
        dest.writeByte(this.isCrop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOvalCrop ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.minScale);
        dest.writeFloat(this.maxScale);
        dest.writeFloat(this.boundaryResistance);
        dest.writeFloat(this.cropBorderWidth);
        dest.writeInt(this.cropBorderColor);
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
        dest.writeInt(this.selectedLayoutId);
        dest.writeInt(this.itemCameraLayoutId);
        dest.writeInt(this.itemImageLayoutId);
        dest.writeInt(this.cropSingleLayoutId);
        dest.writeInt(this.cropMoreLayoutId);
    }

    protected ImagePickerParams(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.selectCount = in.readInt();
        this.isCrop = in.readByte() != 0;
        this.isOvalCrop = in.readByte() != 0;
        this.minScale = in.readFloat();
        this.maxScale = in.readFloat();
        this.boundaryResistance = in.readFloat();
        this.cropBorderWidth = in.readFloat();
        this.cropBorderColor = in.readInt();
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
        this.selectedLayoutId = in.readInt();
        this.itemCameraLayoutId = in.readInt();
        this.itemImageLayoutId = in.readInt();
        this.cropSingleLayoutId = in.readInt();
        this.cropMoreLayoutId = in.readInt();
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
