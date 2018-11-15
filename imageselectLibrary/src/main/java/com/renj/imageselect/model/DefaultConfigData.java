package com.renj.imageselect.model;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-01-24   16:51
 * <p>
 * 描述：默认配置参数数据
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface DefaultConfigData {

    /**
     * 裁剪宽度
     */
    int WIDTH = 200;

    /**
     * 裁剪高度
     */
    int HEIGHT = 200;

    /**
     * 选择图片张数
     */
    int SELECT_COUNT = 1;

    /**
     * 是否裁剪
     */
    boolean IS_CLIP = false;

    /**
     * 是否裁剪成圆形图片
     */
    boolean IS_CIRCLE_CLIP = false;

    /**
     * 图片最小缩放倍数
     */
    float MIN_SCALE = 0.8f;

    /**
     * 图片最大缩放倍数
     */
    float MAX_SCALE = 4f;

    /**
     * 裁剪线条宽度
     */
    float CLIP_BORDER_WIDTH = 1;

    /**
     * 裁剪线条边框颜色
     */
    int CLIP_BORDER_COLOR = 0xFFFFFFFF;

    /**
     * 遮罩层颜色
     */
    int MASK_COLOR = 0x80000000;

    /**
     * 是否双击连续放大，当设置为 true 时，双击时若图片的缩放小于2时变为2，当图片的缩放倍数在[2,4)时变为4
     */
    boolean IS_CONTINUITY_ENLARGE = false;

    /**
     * 是否显示打开相机按钮
     */
    boolean IS_SHOW_CAMERA = true;
}