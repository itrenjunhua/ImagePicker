package com.renj.pickertest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.weight.IPSquareImageView;
import com.renj.pickertest.R;
import com.renj.pickertest.utils.ImageLoaderManager;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2019-04-26   14:06
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageShowAdapter extends RecyclerView.Adapter<ImageShowAdapter.ImageShowHolder> {
    private Context context;
    private List<ImagePickerModel> imagePickerModels;

    public ImageShowAdapter(Context context) {
        this.context = context;
    }

    public void setItemDataList(List<ImagePickerModel> imagePickerModels) {
        this.imagePickerModels = imagePickerModels;
        notifyDataSetChanged();
    }

    @Override
    public ImageShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.image_show_item, parent, false);
        return new ImageShowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageShowHolder holder, int position) {
        ImagePickerModel imagePickerModel = imagePickerModels.get(position);
        ImageLoaderManager.loadImageForFile(imagePickerModel.path, holder.ivItem);
    }

    @Override
    public int getItemCount() {
        return imagePickerModels == null ? 0 : imagePickerModels.size();
    }

    static class ImageShowHolder extends RecyclerView.ViewHolder {
        IPSquareImageView ivItem;

        public ImageShowHolder(View view) {
            super(view);
            ivItem = view.findViewById(R.id.iv_item);
        }
    }
}
