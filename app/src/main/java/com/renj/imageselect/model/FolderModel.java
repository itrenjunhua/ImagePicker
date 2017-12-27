package com.renj.imageselect.model;

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

    public FolderModel(String name) {
        this.name = name;
    }

    public FolderModel(String name, List<ImageModel> folders) {
        this.name = name;
        this.folders = folders;
    }

    public void addImageModel(ImageModel imageModel) {
        if (folders == null) folders = new ArrayList<>();

        folders.add(imageModel);
    }
}
