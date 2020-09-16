package com.renj.imagepicker.model;

import android.os.Parcel;
import android.os.Parcelable;

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
public class ImagePickerModel implements Parcelable {
    // 图片路径
    public String path;
    // 图片名称
    public String name;
    // 图片大小
    public long size;
    // 创建时间
    public long time;

    public ImagePickerModel(String path, String name, long size, long time) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeLong(this.size);
        dest.writeLong(this.time);
    }

    protected ImagePickerModel(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
        this.size = in.readLong();
        this.time = in.readLong();
    }

    public static final Parcelable.Creator<ImagePickerModel> CREATOR = new Parcelable.Creator<ImagePickerModel>() {
        @Override
        public ImagePickerModel createFromParcel(Parcel source) {
            return new ImagePickerModel(source);
        }

        @Override
        public ImagePickerModel[] newArray(int size) {
            return new ImagePickerModel[size];
        }
    };
}
