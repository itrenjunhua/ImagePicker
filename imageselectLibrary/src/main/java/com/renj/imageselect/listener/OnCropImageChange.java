package com.renj.imageselect.listener;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.renj.imageselect.model.ImageModel;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-04-24   17:06
 * <p>
 * 描述：图片裁剪时回调
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class OnCropImageChange {
    /**
     * 设置默认情况下显示的文字<br/>
     *
     * @param cropView   裁剪按钮控件
     * @param cancelView 取消按钮控件
     * @param clipCount  当前正在裁剪的张数，正常情况下为 1
     * @param totalCount 需要的裁剪张数
     */
    public void onDefault(@NonNull TextView cropView, @NonNull TextView cancelView, int clipCount, int totalCount) {

    }

    /**
     * 图片发生裁剪时回调(主要方便动态修改显示的文字)<br/>
     *
     * @param cropView     裁剪按钮控件
     * @param cancelView   取消按钮控件
     * @param imageModel   当前裁剪完成的图片
     * @param clipResultList     裁剪后的图片集合
     * @param isCircleClip 是否圆形裁剪
     * @param clipCount    当前已裁剪的张数
     * @param totalCount   共需要裁剪的张数
     */
    public void onClipChange(@NonNull TextView cropView, @NonNull TextView cancelView,
                             @NonNull ImageModel imageModel, @NonNull List<ImageModel> clipResultList,
                             boolean isCircleClip, int clipCount, int totalCount) {
    }
}
