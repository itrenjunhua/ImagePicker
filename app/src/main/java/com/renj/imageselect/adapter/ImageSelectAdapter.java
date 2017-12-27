package com.renj.imageselect.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.renj.imageselect.R;
import com.renj.imageselect.model.ImageModel;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-27   16:45
 * <p>
 * 描述：图片选择界面GridView的适配器
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageSelectAdapter extends BaseAdapter {
    private Context context;
    private List<ImageModel> imageModels;

    public ImageSelectAdapter(Context context) {
        this.context = context;
    }

    public ImageSelectAdapter(@NonNull Context context, @NonNull List<ImageModel> imageModels) {
        this.context = context;
        this.imageModels = imageModels;
    }

    public void setImageModels(@NonNull List<ImageModel> imageModels) {
        this.imageModels = imageModels;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageModels == null ? 0 : imageModels.size();
    }

    @Override
    public Object getItem(int position) {
        return imageModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image_select, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(imageModels.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View view) {
            view.setTag(this);
            imageView = view.findViewById(R.id.iv_item);
        }

        public void setData(@NonNull ImageModel imageModel) {
            Glide.with(context).load(imageModel.path).into(imageView);
        }
    }
}
