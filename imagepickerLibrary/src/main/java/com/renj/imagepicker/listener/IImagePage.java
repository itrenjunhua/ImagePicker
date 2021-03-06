package com.renj.imagepicker.listener;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-24   11:54
 * <p>
 * 描述：图片选择/裁剪页面公共操作
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface IImagePage {

    void cancel();

    void showLoading();

    void showLoading(String loadingText);

    void closeLoading();
}
