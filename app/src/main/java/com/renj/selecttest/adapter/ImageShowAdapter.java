package com.renj.selecttest.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.weight.AutoImageView;
import com.renj.selecttest.R;
import com.renj.selecttest.utils.ImageLoaderManager;

import butterknife.BindView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-04-26   14:06
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageShowAdapter extends BaseListAdapter<ImageModel> {
    public ImageShowAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    protected BaseListViewHolder getViewHolder(Context context, ViewGroup parent, int position) {
        return new ImageShowHolder(context, parent);
    }

    static class ImageShowHolder extends BaseListViewHolder<ImageModel> {
        @BindView(R.id.iv_item)
        AutoImageView ivItem;

        public ImageShowHolder(Context context, ViewGroup parent) {
            super(context, parent);
        }

        @Override
        public int getItemViewLayoutId() {
            return R.layout.image_show_item;
        }

        @Override
        public void setData(int position, ImageModel data) {
            ImageLoaderManager.loadImageForFile(data.path, ivItem);
        }
    }
}
