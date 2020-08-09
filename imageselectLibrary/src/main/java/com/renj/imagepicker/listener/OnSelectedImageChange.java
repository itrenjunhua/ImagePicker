package com.renj.imagepicker.listener;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.renj.imagepicker.model.ImageModel;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-04-22   16:01
 * <p>
 * 描述：图片选择页面，图片选择发生变化时回调<br/>
 * <b>注意：只有在选择多张图片时才会回调，单张图片并不会回调</b>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public abstract class OnSelectedImageChange {
    /**
     * 设置默认情况下显示的文字<br/>
     * <b>注意：该方法只有在选择多张图片时才会回调，单张图片并不会回调</b>
     *
     * @param confirmView   确认控件
     * @param cancelView    取消控件
     * @param selectedCount 当前已选择的张数，正常情况下为 0
     * @param totalCount    需要选择的张数/预期选择的张数
     */
    public void onDefault(@NonNull TextView confirmView, @NonNull TextView cancelView, int selectedCount, int totalCount) {

    }

    /**
     * 图片选择页面，图片选择发生变化时回调(主要方便动态修改显示的文字)<br/>
     * <b>注意：该方法只有在选择多张图片时才会回调，单张图片并不会回调</b>
     *
     * @param confirmView   确认控件
     * @param cancelView    取消控件
     * @param imageModel    当前选中的图片
     * @param isSelected    是否选中，true：选中图片；false：取消选中
     * @param selectedList  所有已选中的图片集合
     * @param selectedCount 当前已选择的张数
     * @param totalCount    需要选择的张数/预期选择的张数
     */
    public void onSelectedChange(@NonNull TextView confirmView, @NonNull TextView cancelView,
                                 @NonNull ImageModel imageModel, boolean isSelected,
                                 @NonNull List<ImageModel> selectedList, int selectedCount, int totalCount) {
    }
}
