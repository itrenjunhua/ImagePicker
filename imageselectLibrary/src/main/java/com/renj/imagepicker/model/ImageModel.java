package com.renj.imagepicker.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-27   15:07
 * <p>
 * 描述：图片信息实体类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageModel implements Parcelable {
    // 图片路径
    public String path;
    // 图片名称
    public String name;
    // 创建时间
    public long time;

    public ImageModel(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public String toString() {
        return "{ path = [" + path + "], name = [" + name + "], time = [" + time + "] }";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeLong(this.time);
    }

    protected ImageModel(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
        this.time = in.readLong();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @NonNull
        @Override
        public ImageModel createFromParcel(Parcel source) {
            return new ImageModel(source);
        }

        @NonNull
        @Contract(pure = true)
        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };
}
