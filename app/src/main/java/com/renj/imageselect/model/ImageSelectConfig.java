package com.renj.imageselect.model;

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
public class ImageSelectConfig {
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

    public float getAspectRatio() {
        return this.configModel.aspectRatio;
    }

    public int getAspectRatioBase() {
        return this.configModel.aspectRatioBase;
    }

    public int getClipLineWidth() {
        return this.configModel.clipLineWidth;
    }

    public int getClipLineColor() {
        return this.configModel.clipLineColor;
    }

    public int getMaskColorColor() {
        return this.configModel.maskColorColor;
    }

    private static class ConfigModel {
        int width; // 裁剪宽度
        int height; // 裁剪高度
        int selectCount; // 选择图片张数
        boolean isClip; // 是否裁剪
        boolean isCircleClip; // 是否裁剪成圆形图片
        float minScale; // 图片最小缩放倍数
        float maxScale; // 图片最大缩放倍数
        float aspectRatio; // 宽高比例
        int aspectRatioBase; // 宽高比以宽还是高为1，0表示以宽为 1，非0表示以高为1，这里的宽或者高以设置的 width 或 height 属性为准，没设置宽高使用默认的
        int clipLineWidth; // 裁剪线条宽度
        int clipLineColor; // 裁剪线条颜色
        int maskColorColor; // 遮罩层颜色
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

        public Builder aspectRatio(float aspectRatio) {
            configModel.aspectRatio = aspectRatio;
            return this;
        }

        public Builder aspectRatioBase(int aspectRatioBase) {
            configModel.aspectRatioBase = aspectRatioBase;
            return this;
        }

        public Builder clipLineWidth(int clipLineWidth) {
            configModel.clipLineWidth = clipLineWidth;
            return this;
        }

        public Builder clipLineColor(int clipLineColor) {
            configModel.clipLineColor = clipLineColor;
            return this;
        }

        public Builder maskColorColor(int maskColorColor) {
            configModel.maskColorColor = maskColorColor;
            return this;
        }

        public ImageSelectConfig build() {
            return new ImageSelectConfig(configModel);
        }
    }
}
