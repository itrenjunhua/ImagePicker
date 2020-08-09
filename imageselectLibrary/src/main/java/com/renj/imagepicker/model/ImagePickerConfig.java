package com.renj.imagepicker.model;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.renj.imagepicker.BuildConfig;

import java.io.File;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2019-12-09   11:59
 * <p>
 * 描述：配置参数
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePickerConfig {
    public File fileSavePath;

    public boolean showLogger;
    public String loggerTag;

    public boolean center;

    private ImagePickerConfig(Builder builder) {
        this.fileSavePath = builder.fileSavePath;
        this.showLogger = builder.showLogger;
        this.loggerTag = builder.loggerTag;
        this.center = builder.center;
    }

    public File getFileSavePath() {
        return fileSavePath;
    }

    public boolean isShowLogger() {
        return showLogger;
    }

    public String getLoggerTag() {
        return loggerTag;
    }

    public boolean isCenter() {
        return center;
    }

    public static class Builder {
        private File fileSavePath;

        private boolean showLogger;
        private String loggerTag;

        private boolean center;

        public Builder() {
            this.fileSavePath = getDefaultSaveDir();
            this.showLogger = BuildConfig.DEBUG;
            this.loggerTag = "ImageSelect-Logger";
            this.center = true;
        }

        /**
         * 裁剪、拍照图片保存路径
         *
         * @param fileSavePath
         */
        public Builder fileSavePath(File fileSavePath) {
            this.fileSavePath = fileSavePath;
            return this;
        }

        /**
         * 是否显示日志信息
         *
         * @param showLogger true：显示
         */
        public Builder showLogger(boolean showLogger) {
            this.showLogger = showLogger;
            return this;
        }

        /**
         * 提示文案是否居中显示
         *
         * @param center true:居中
         */
        public Builder center(boolean center) {
            this.center = center;
            return this;
        }

        /**
         * 日志信息的TAG，默认 "ImageSelect-Logger"
         *
         * @param loggerTag
         */
        public Builder loggerTag(String loggerTag) {
            this.loggerTag = loggerTag;
            return this;
        }

        /**
         * 获取默认保存路径
         *
         * @return
         */
        @Nullable
        private File getDefaultSaveDir() {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                File file = new File(Environment.getExternalStorageDirectory(), "image_select");
                if (file == null || !file.exists() || !file.isDirectory())
                    file.mkdirs();
                return file;
            }
            return null;
        }

        public ImagePickerConfig build() {
            return new ImagePickerConfig(this);
        }
    }
}
