package com.renj.imagepicker.activity;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-24   11:54
 * <p>
 * 描述：
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
