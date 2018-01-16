package com.renj.imageselect.model;

import android.os.Parcel;
import android.os.Parcelable;

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

    public float getClipLineWidth() {
        return this.configModel.clipLineWidth;
    }

    public int getClipLineColor() {
        return this.configModel.clipLineColor;
    }

    public int getMaskColorColor() {
        return this.configModel.maskColorColor;
    }

    public boolean isContinuityEnlarge() {
        return this.configModel.isContinuityEnlarge;
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

    public static final Parcelable.Creator<ImageSelectConfig> CREATOR = new Parcelable.Creator<ImageSelectConfig>() {
        @Override
        public ImageSelectConfig createFromParcel(Parcel source) {
            return new ImageSelectConfig(source);
        }

        @Override
        public ImageSelectConfig[] newArray(int size) {
            return new ImageSelectConfig[size];
        }
    };

    private static class ConfigModel implements Parcelable {
        int width = 200; // 裁剪宽度
        int height = 200; // 裁剪高度
        int selectCount = 1; // 选择图片张数
        boolean isClip = false; // 是否裁剪
        boolean isCircleClip = false; // 是否裁剪成圆形图片
        float minScale = 0.8f; // 图片最小缩放倍数
        float maxScale = 4f; // 图片最大缩放倍数
        float clipLineWidth = 1; // 裁剪线条宽度
        int clipLineColor = 0xFFFFFFFF; // 裁剪线条颜色
        int maskColorColor = 0xAA000000; // 遮罩层颜色
        boolean isContinuityEnlarge = false; // 是否双击连续放大

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
            clipLineWidth = in.readInt();
            clipLineColor = in.readInt();
            maskColorColor = in.readInt();
            isContinuityEnlarge = in.readByte() != 0;
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
            dest.writeFloat(clipLineWidth);
            dest.writeInt(clipLineColor);
            dest.writeInt(maskColorColor);
            dest.writeByte((byte) (isContinuityEnlarge ? 1 : 0));
        }
    }

    public static class Builder {
        ConfigModel configModel = new ConfigModel();

        public Builder() {
        }

        public Builder width(int width) {
            configModel.width = width;
            return this;
        }

        public Builder height(int height) {
            configModel.height = height;
            return this;
        }

        public Builder selectCount(int selectCount) {
            configModel.selectCount = selectCount;
            return this;
        }

        public Builder isClip(boolean isClip) {
            configModel.isClip = isClip;
            return this;
        }

        public Builder isCircleClip(boolean isCircleClip) {
            configModel.isCircleClip = isCircleClip;
            return this;
        }

        public Builder minScale(float minScale) {
            configModel.minScale = minScale;
            return this;
        }

        public Builder maxScale(float maxScale) {
            configModel.maxScale = maxScale;
            return this;
        }

        public Builder clipBorderWidth(float clipLineWidth) {
            configModel.clipLineWidth = clipLineWidth;
            return this;
        }

        public Builder clipBorderColor(int clipLineColor) {
            configModel.clipLineColor = clipLineColor;
            return this;
        }

        public Builder maskColorColor(int maskColorColor) {
            configModel.maskColorColor = maskColorColor;
            return this;
        }

        public Builder isContinuityEnlarge(boolean isContinuityEnlarge) {
            configModel.isContinuityEnlarge = isContinuityEnlarge;
            return this;
        }

        public ImageSelectConfig build() {
            return new ImageSelectConfig(configModel);
        }
    }
}
