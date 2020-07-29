package com.renj.imageselect.model;

import android.support.annotation.FloatRange;

import com.renj.imageselect.R;

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
    boolean IS_CROP = false;

    /**
     * 是否裁剪成圆形图片
     */
    boolean IS_OVAL_CROP = false;

    /**
     * 改变裁剪范围时,是否按照比例改变
     */
    boolean AUTO_RATIO_SCALE = true;
    /**
     * 改变裁剪范围时,宽比例
     */
    int SCALE_WIDTH_RATIO = 1;
    /**
     * 改变裁剪范围时,高比例
     */
    int SCALE_HEIGHT_RATIO = 1;

    /**
     * 需要绘制的分割线条数 小于1时表示不绘制
     */
    int CELL_LINE_COUNT = 2;

    /**
     * 裁剪控件触摸处理类型
     */
    int TOUCH_HANDLER_TYPE = CropConstants.TOUCH_OFFSET_AND_SCALE;

    /**
     * 图片最小缩放倍数
     */
    float MIN_SCALE = 0.8f;

    /**
     * 图片最大缩放倍数
     */
    float MAX_SCALE = 4f;

    /**
     * 边界滑动阻力系数(达到边界时增加滑动阻力，图片实际移动距离和手指移动距离的比值[0-1]，值越大，图片实际移动距离和手指移动距离差值越小; 默认0.25)
     */
    @FloatRange(from = 0, to = 1)
    float BOUNDARY_RESISTANCE = 0.25f;

    /**
     * 裁剪线条宽度
     */
    float CROP_BORDER_WIDTH = 1;

    /**
     * 裁剪线条边框颜色
     */
    int CROP_BORDER_COLOR = 0xFFFFFFFF;

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
    /**
     * 图片选择页面列数
     */
    int GRID_COL_NUMBERS = 3;
    /**
     * 默认选择图片布局资源文件
     */
    int SELECTED_IMAGE_LAYOUT = R.layout.image_select_layout;
    /**
     * 默认选择图片条目布局资源文件(点击打开相机条目)
     */
    int SELECTED_IMAGE_ITEM_CAMERA_LAYOUT = R.layout.image_select_camera_item;
    /**
     * 默认选择图片条目布局资源文件(图片显示条目)
     */
    int SELECTED_IMAGE_ITEM_IMAGE_LAYOUT = R.layout.image_select_item;
    /**
     * 默认裁剪单张图片布局资源文件
     */
    int CROP_SINGLE_LAYOUT = R.layout.image_crop_single_layout;
    /**
     * 默认裁剪多张图片布局资源文件
     */
    int CROP_MORE_LAYOUT = R.layout.image_crop_more_layout;
}
