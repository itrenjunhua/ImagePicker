package com.renj.imageselect.utils;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-29   16:51
 * <p>
 * 描述：选择或裁剪后的结果回调 <br/>
 * <b>注意泛型：</b>
 * <br/>&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>1.当需要选择或裁剪的只是单张图片时，泛型应该为 {@link com.renj.imageselect.model.ImageModel}</b>
 * <br/>&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>2.当需要选择或裁剪的只是多张图片时，泛型应该为 List<{@link com.renj.imageselect.model.ImageModel}></{@link></b>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface OnResultCallBack<T> {
    void onResult(T selectResult);
}
