package com.renj.imageselect.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import com.renj.imageselect.listener.OnResultCallBack;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-01-03   13:59
 * <p>
 * 描述：裁剪参数配置
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageSelectConfig implements Parcelable {
    int width; // 裁剪宽度
    int height; // 裁剪高度
    int selectCount; // 选择图片张数
    boolean isClip; // 是否裁剪
    boolean isCircleClip; // 是否裁剪成圆形图片
    float minScale; // 图片最小缩放倍数
    float maxScale; // 图片最大缩放倍数
    @FloatRange(from = 0, to = 1)
    float boundaryResistance; // 边界滑动阻力系数
    float clipBorderWidth; // 裁剪线条宽度
    int clipBorderColor; // 裁剪线条颜色
    int maskColor; // 遮罩层颜色
    boolean isContinuityEnlarge; // 是否双击连续放大
    boolean isShowCamera; // 是否显示打开相机按钮

    private ImageSelectConfig(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.selectCount = builder.selectCount;
        this.isClip = builder.isClip;
        this.isCircleClip = builder.isCircleClip;
        this.minScale = builder.minScale;
        this.maxScale = builder.maxScale;
        this.boundaryResistance = builder.boundaryResistance;
        this.clipBorderWidth = builder.clipBorderWidth;
        this.clipBorderColor = builder.clipBorderColor;
        this.maskColor = builder.maskColor;
        this.isContinuityEnlarge = builder.isContinuityEnlarge;
        this.isShowCamera = builder.isShowCamera;
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

    public boolean isClip() {
        return this.isClip;
    }

    public boolean isCircleClip() {
        return this.isCircleClip;
    }

    public float getMinScale() {
        return this.minScale;
    }

    public float getMaxScale() {
        return this.maxScale;
    }

    public float getBoundaryResistance(){
        return this.boundaryResistance;
    }

    public float getClipBorderWidth() {
        return this.clipBorderWidth;
    }

    public int getClipBorderColor() {
        return this.clipBorderColor;
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


    public static class Builder {
        int width = DefaultConfigData.WIDTH; // 裁剪宽度
        int height = DefaultConfigData.HEIGHT; // 裁剪高度
        int selectCount = DefaultConfigData.SELECT_COUNT; // 选择图片张数
        boolean isClip = DefaultConfigData.IS_CLIP; // 是否裁剪
        boolean isCircleClip = DefaultConfigData.IS_CIRCLE_CLIP; // 是否裁剪成圆形图片
        float minScale = DefaultConfigData.MIN_SCALE; // 图片最小缩放倍数
        float maxScale = DefaultConfigData.MAX_SCALE; // 图片最大缩放倍数
        @FloatRange(from = 0, to = 1)
        float boundaryResistance = DefaultConfigData.BOUNDARY_RESISTANCE; // 边界滑动阻力系数
        float clipBorderWidth = DefaultConfigData.CLIP_BORDER_WIDTH; // 裁剪线条宽度
        int clipBorderColor = DefaultConfigData.CLIP_BORDER_COLOR; // 裁剪线条颜色
        int maskColor = DefaultConfigData.MASK_COLOR; // 遮罩层颜色
        boolean isContinuityEnlarge = DefaultConfigData.IS_CONTINUITY_ENLARGE; // 是否双击连续放大
        boolean isShowCamera = DefaultConfigData.IS_SHOW_CAMERA; // 是否显示打开相机按钮

        public Builder() {
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
         *                    <b>1.当选择或裁剪的只是单张图片(selectCount = 1)时，泛型应该为 {@link com.renj.imageselect.model.ImageModel}</b>
         *                    <br/>&nbsp;&nbsp;&nbsp;&nbsp;
         *                    <b>2.当选择或裁剪的只是多张图片(selectCount > 1)时，泛型应该为 List<{@link com.renj.imageselect.model.ImageModel}></{@link></b>
         * @return
         */
        public Builder selectCount(int selectCount) {
            this.selectCount = selectCount;
            return this;
        }

        /**
         * 设置是否需要裁剪
         *
         * @param isClip true：需要；false：不需要
         * @return
         */
        public Builder isClip(boolean isClip) {
            this.isClip = isClip;
            return this;
        }

        /**
         * 设置是否需要裁剪成圆形<br/>
         * <b>注意：圆形的半径为裁剪宽度、裁剪高度较小值得一半</b>
         *
         * @param isCircleClip true：需要；false：不需要
         * @return
         */
        public Builder isCircleClip(boolean isCircleClip) {
            this.isCircleClip = isCircleClip;
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
         * @param clipBorderWidth 裁剪边框宽度
         * @return
         */
        public Builder clipBorderWidth(float clipBorderWidth) {
            this.clipBorderWidth = clipBorderWidth;
            return this;
        }

        /**
         * 设置裁剪边框颜色
         *
         * @param clipBorderColor 裁剪边框颜色
         * @return
         */
        public Builder clipBorderColor(int clipBorderColor) {
            this.clipBorderColor = clipBorderColor;
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
         * 构建 {@link ImageSelectConfig} 对象
         *
         * @return {@link ImageSelectConfig} 对象
         */
        public ImageSelectConfig build() {
            return new ImageSelectConfig(this);
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
        dest.writeByte(this.isClip ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCircleClip ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.minScale);
        dest.writeFloat(this.maxScale);
        dest.writeFloat(this.boundaryResistance);
        dest.writeFloat(this.clipBorderWidth);
        dest.writeInt(this.clipBorderColor);
        dest.writeInt(this.maskColor);
        dest.writeByte(this.isContinuityEnlarge ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowCamera ? (byte) 1 : (byte) 0);
    }

    protected ImageSelectConfig(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.selectCount = in.readInt();
        this.isClip = in.readByte() != 0;
        this.isCircleClip = in.readByte() != 0;
        this.minScale = in.readFloat();
        this.maxScale = in.readFloat();
        this.boundaryResistance = in.readFloat();
        this.clipBorderWidth = in.readFloat();
        this.clipBorderColor = in.readInt();
        this.maskColor = in.readInt();
        this.isContinuityEnlarge = in.readByte() != 0;
        this.isShowCamera = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ImageSelectConfig> CREATOR = new Parcelable.Creator<ImageSelectConfig>() {
        @NonNull
        @Override
        public ImageSelectConfig createFromParcel(Parcel source) {
            return new ImageSelectConfig(source);
        }

        @NonNull
        @org.jetbrains.annotations.Contract(pure = true)
        @Override
        public ImageSelectConfig[] newArray(int size) {
            return new ImageSelectConfig[size];
        }
    };
}
