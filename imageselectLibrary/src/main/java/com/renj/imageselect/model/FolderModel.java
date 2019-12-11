package com.renj.imageselect.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-27   15:19
 * <p>
 * 描述：按文件夹保存图片的model
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class FolderModel {
    // 文件夹名称
    public String name;
    // 文件夹下的所有图片集合
    public List<ImageModel> folders;
    // 当前文件夹下图片的个数
    public int totalCount;

    public FolderModel(@NonNull String name) {
        this.name = name;
    }

    public FolderModel(@NonNull String name, @NonNull List<ImageModel> folders) {
        this.name = name;
        this.folders = folders;
        this.totalCount = folders == null ? 0 : folders.size();
    }

    public void addImageModel(@NonNull ImageModel imageModel) {
        if (folders == null) folders = new ArrayList<>();

        folders.add(imageModel);
        this.totalCount = folders.size();
    }

    @Override
    public String toString() {
        return "FolderModel{" +
                "name='" + name + '\'' +
                ", folders=" + folders +
                ", totalCount=" + totalCount +
                '}';
    }
}
