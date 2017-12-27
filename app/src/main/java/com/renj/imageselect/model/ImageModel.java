package com.renj.imageselect.model;

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
public class ImageModel {
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
}
