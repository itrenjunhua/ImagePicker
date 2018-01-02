package com.renj.imageselect.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.renj.imageselect.R;
import com.renj.imageselect.model.ImageModel;

import java.util.ArrayList;
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
    private int maxCount = 1;
    private List<ImageModel> checkImages = new ArrayList<>();

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

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public boolean addOrClearCheckedPosition(int position) {
        if (checkImages.size() >= maxCount && !checkImages.contains(imageModels.get(position))) {
            Toast.makeText(context, "最多选择" + maxCount + "张图片", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean result;
        if (checkImages.contains(imageModels.get(position))) {
            checkImages.remove(imageModels.get(position));
            result = false;
        } else {
            checkImages.add(imageModels.get(position));
            result = true;
        }
        notifyDataSetChanged();
        return result;
    }

    public List<ImageModel> getCheckImages(){
        return checkImages;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.image_select_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(imageModels.get(position));

        if (checkImages.contains(imageModels.get(position)))
            viewHolder.checkBox.setChecked(true);
        else
            viewHolder.checkBox.setChecked(false);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View view) {
            view.setTag(this);
            imageView = view.findViewById(R.id.iv_item);
            checkBox = view.findViewById(R.id.item_checkbox);
        }

        public void setData(@NonNull ImageModel imageModel) {
            Glide.with(context).load(imageModel.path).into(imageView);
        }
    }
}
