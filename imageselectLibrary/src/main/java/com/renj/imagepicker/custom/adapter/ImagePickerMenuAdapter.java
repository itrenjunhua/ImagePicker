package com.renj.imagepicker.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.model.ImagePickerFolderModel;
import com.renj.imagepicker.utils.ImagePickerLoaderUtils;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-27   17:40
 * <p>
 * 描述：图片选择目录Adapter
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePickerMenuAdapter extends BaseAdapter {
    private Context context;
    private List<ImagePickerFolderModel> imagePickerFolderModels;
    private int selectPosition = 0;

    public ImagePickerMenuAdapter(Context context) {
        this.context = context;
    }

    public ImagePickerMenuAdapter(Context context, List<ImagePickerFolderModel> imagePickerFolderModels) {
        this.context = context;
        this.imagePickerFolderModels = imagePickerFolderModels;
    }

    /**
     * 设置数据
     */
    public void setImagePickerFolderModels(List<ImagePickerFolderModel> imagePickerFolderModels) {
        this.imagePickerFolderModels = imagePickerFolderModels;
        notifyDataSetChanged();
    }

    /**
     * 设置选中位置
     */
    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imagePickerFolderModels == null ? 0 : imagePickerFolderModels.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePickerFolderModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.default_image_menu_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(imagePickerFolderModels.get(position), position);
        return convertView;
    }


    class ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private RadioButton rbMenu;

        public ViewHolder(View view) {
            view.setTag(this);

            imageView = view.findViewById(R.id.iv_enum_show);
            textView = view.findViewById(R.id.tv_enmu_name);
            rbMenu = view.findViewById(R.id.rb_menu);
        }

        public void setData(ImagePickerFolderModel imagePickerFolderModel, int position) {
            textView.setText("(" + imagePickerFolderModel.totalCount + ")" + imagePickerFolderModel.name);
            ImagePickerLoaderUtils.loadImage(imagePickerFolderModel.folders.get(0).path, imageView);

            if (position == selectPosition) {
                rbMenu.setChecked(true);
                rbMenu.setVisibility(View.VISIBLE);
            } else {
                rbMenu.setVisibility(View.GONE);
            }
        }
    }
}
