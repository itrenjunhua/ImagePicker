package com.renj.imageselect.listener;

import com.renj.imageselect.model.ImageModel;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-29   16:51
 * <p>
 * 描述：选择或裁剪后的结果回调 <br/>
 * <b>注意：</b>
 * <br/>&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>当选择一张图片时，集合的大小为1</b>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface OnResultCallBack {
    /**
     * 选择或裁剪之后的回调函数
     *
     * @param resultList 当选择一张图片时，集合的大小为1
     */
    void onResult(List<ImageModel> resultList);
}
