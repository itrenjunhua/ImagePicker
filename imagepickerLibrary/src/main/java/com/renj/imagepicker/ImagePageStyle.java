package com.renj.imagepicker;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-09-16   15:06
 * <p>
 * 描述：页面样式
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePageStyle implements Parcelable {
    private final int statusBarColor; // 状态栏颜色
    private final boolean statusBarDark; // 状态栏文字时候深色

    private ImagePageStyle(Builder builder) {
        this.statusBarColor = builder.statusBarColor;
        this.statusBarDark = builder.statusBarDark;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public boolean isStatusBarDark() {
        return statusBarDark;
    }

    public static class Builder {
        private int statusBarColor;
        private boolean statusBarDark;

        public Builder() {
            this.statusBarColor = Color.parseColor("#000000");
            this.statusBarDark = false;
        }

        /**
         * 状态栏颜色
         */
        public Builder statusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        /**
         * 状态栏文字时候深色
         */
        public Builder statusBarDark(boolean statusBarDark) {
            this.statusBarDark = statusBarDark;
            return this;
        }


        public ImagePageStyle build() {
            return new ImagePageStyle(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.statusBarColor);
        dest.writeByte(this.statusBarDark ? (byte) 1 : (byte) 0);
    }

    protected ImagePageStyle(Parcel in) {
        this.statusBarColor = in.readInt();
        this.statusBarDark = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ImagePageStyle> CREATOR = new Parcelable.Creator<ImagePageStyle>() {
        @Override
        public ImagePageStyle createFromParcel(Parcel source) {
            return new ImagePageStyle(source);
        }

        @Override
        public ImagePageStyle[] newArray(int size) {
            return new ImagePageStyle[size];
        }
    };
}
