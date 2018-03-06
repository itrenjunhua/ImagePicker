package com.renj.imageselect.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.renj.imageselect.utils.OnResultCallBack;

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
    private ConfigModel configModel = new ConfigModel();

    private ImageSelectConfig(ConfigModel configModel) {
        this.configModel = configModel;
    }

    public int getWidth() {
        return this.configModel.width;
    }

    public int getHeight() {
        return this.configModel.height;
    }

    public int getSelectCount() {
        return this.configModel.selectCount;
    }

    public boolean isClip() {
        return this.configModel.isClip;
    }

    public boolean isCircleClip() {
        return this.configModel.isCircleClip;
    }

    public float getMinScale() {
        return this.configModel.minScale;
    }

    public float getMaxScale() {
        return this.configModel.maxScale;
    }

    public float getClipBorderWidth() {
        return this.configModel.clipBorderWidth;
    }

    public int getClipBorderColor() {
        return this.configModel.clipBorderColor;
    }

    public int getMaskColor() {
        return this.configModel.maskColor;
    }

    public boolean isContinuityEnlarge() {
        return this.configModel.isContinuityEnlarge;
    }

    public boolean isShowCamera() {
        return this.configModel.isShowCamera;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.configModel, flags);
    }

    protected ImageSelectConfig(Parcel in) {
        this.configModel = in.readParcelable(ConfigModel.class.getClassLoader());
    }

    public static final Creator<ImageSelectConfig> CREATOR = new Creator<ImageSelectConfig>() {
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

    private static class ConfigModel implements Parcelable {
        int width = DefaultConfigData.WIDTH; // 裁剪宽度
        int height = DefaultConfigData.HEIGHT; // 裁剪高度
        int selectCount = DefaultConfigData.SELECT_COUNT; // 选择图片张数
        boolean isClip = DefaultConfigData.IS_CLIP; // 是否裁剪
        boolean isCircleClip = DefaultConfigData.IS_CIRCLE_CLIP; // 是否裁剪成圆形图片
        float minScale = DefaultConfigData.MIN_SCALE; // 图片最小缩放倍数
        float maxScale = DefaultConfigData.MAX_SCALE; // 图片最大缩放倍数
        float clipBorderWidth = DefaultConfigData.CLIP_BORDER_WIDTH; // 裁剪线条宽度
        int clipBorderColor = DefaultConfigData.CLIP_BORDER_COLOR; // 裁剪线条颜色
        int maskColor = DefaultConfigData.MASK_COLOR; // 遮罩层颜色
        boolean isContinuityEnlarge = DefaultConfigData.IS_CONTINUITY_ENLARGE; // 是否双击连续放大
        boolean isShowCamera = DefaultConfigData.IS_SHOW_CAMERA; // 是否显示打开相机按钮

        public ConfigModel() {

        }

        protected ConfigModel(Parcel in) {
            width = in.readInt();
            height = in.readInt();
            selectCount = in.readInt();
            isClip = in.readByte() != 0;
            isCircleClip = in.readByte() != 0;
            minScale = in.readFloat();
            maxScale = in.readFloat();
            clipBorderWidth = in.readFloat();
            clipBorderColor = in.readInt();
            maskColor = in.readInt();
            isContinuityEnlarge = in.readByte() != 0;
            isShowCamera = in.readByte() != 0;
        }

        public static final Creator<ConfigModel> CREATOR = new Creator<ConfigModel>() {
            @Override
            public ConfigModel createFromParcel(Parcel in) {
                return new ConfigModel(in);
            }

            @Override
            public ConfigModel[] newArray(int size) {
                return new ConfigModel[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(width);
            dest.writeInt(height);
            dest.writeInt(selectCount);
            dest.writeByte((byte) (isClip ? 1 : 0));
            dest.writeByte((byte) (isCircleClip ? 1 : 0));
            dest.writeFloat(minScale);
            dest.writeFloat(maxScale);
            dest.writeFloat(clipBorderWidth);
            dest.writeInt(clipBorderColor);
            dest.writeInt(maskColor);
            dest.writeByte((byte) (isContinuityEnlarge ? 1 : 0));
            dest.writeByte((byte) (isShowCamera ? 1 : 0));
        }
    }

    public static class Builder {
        ConfigModel configModel = new ConfigModel();

        public Builder() {
        }

        /**
         * 设置裁剪宽度，单位 dp；默认 200dp
         *
         * @param width 裁剪宽
         * @return
         */
        public Builder width(int width) {
            configModel.width = width;
            return this;
        }

        /**
         * 设置裁剪高度，单位 dp；默认 200dp
         *
         * @param height 裁剪高度
         * @return
         */
        public Builder height(int height) {
            configModel.height = height;
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
            configModel.selectCount = selectCount;
            return this;
        }

        /**
         * 设置是否需要裁剪
         *
         * @param isClip true：需要；false：不需要
         * @return
         */
        public Builder isClip(boolean isClip) {
            configModel.isClip = isClip;
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
            configModel.isCircleClip = isCircleClip;
            return this;
        }

        /**
         * 设置图片的最小缩放比例
         *
         * @param minScale 最小缩放比例
         * @return
         */
        public Builder minScale(float minScale) {
            configModel.minScale = minScale;
            return this;
        }

        /**
         * 设置图片的最大缩放比例
         *
         * @param maxScale 最大缩放比例
         * @return
         */
        public Builder maxScale(float maxScale) {
            configModel.maxScale = maxScale;
            return this;
        }

        /**
         * 设置裁剪边框宽度
         *
         * @param clipBorderWidth 裁剪边框宽度
         * @return
         */
        public Builder clipBorderWidth(float clipBorderWidth) {
            configModel.clipBorderWidth = clipBorderWidth;
            return this;
        }

        /**
         * 设置裁剪边框颜色
         *
         * @param clipBorderColor 裁剪边框颜色
         * @return
         */
        public Builder clipBorderColor(int clipBorderColor) {
            configModel.clipBorderColor = clipBorderColor;
            return this;
        }

        /**
         * 设置遮罩层颜色
         *
         * @param maskColor 遮罩层颜色
         * @return
         */
        public Builder maskColor(int maskColor) {
            configModel.maskColor = maskColor;
            return this;
        }

        /**
         * 设置是否双击连续放大
         *
         * @param isContinuityEnlarge true：是；false：否  当设置为 true 时，双击时若图片的缩放小于2时变为2，当图片的缩放倍数在[2,4)时变为4
         * @return
         */
        public Builder isContinuityEnlarge(boolean isContinuityEnlarge) {
            configModel.isContinuityEnlarge = isContinuityEnlarge;
            return this;
        }

        /**
         * 设置是否显示打开相机图片，默认显示
         *
         * @param isShowCamera true：显示 false：不显示
         * @return
         */
        public Builder isShowCamera(boolean isShowCamera) {
            configModel.isShowCamera = isShowCamera;
            return this;
        }

        /**
         * 构建 {@link ImageSelectConfig} 对象
         *
         * @return {@link ImageSelectConfig} 对象
         */
        public ImageSelectConfig build() {
            return new ImageSelectConfig(configModel);
        }
    }
}
