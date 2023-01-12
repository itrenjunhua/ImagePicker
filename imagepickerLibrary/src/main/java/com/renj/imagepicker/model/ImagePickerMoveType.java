package com.renj.imagepicker.model;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2023-01-12   15:09
 * <p>
 * 描述：裁剪时图片拖动范围类型，默认范围的边界为图片控件范围
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public @interface ImagePickerMoveType {
    /**
     * 显示图片的控件范围内移动，默认值
     */
    int MOVE_IMAGE_VIEW = 0;
    /**
     * 以裁剪框范围的边界作为图片移动的边界值
     */
    int MOVE_CROP_VIEW = 1;
}
