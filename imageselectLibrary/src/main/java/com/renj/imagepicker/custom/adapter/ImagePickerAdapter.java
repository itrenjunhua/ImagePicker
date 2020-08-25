package com.renj.imagepicker.custom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.custom.UIUtils;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.utils.ImagePickerLoaderUtils;

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
public class ImagePickerAdapter extends BaseAdapter {
    private final int ITEM_VIEW = 0;
    private final int CAMERA_VIEW = 1;
    private Context context;
    private List<ImagePickerModel> imagePickerModels;
    private int maxCount = 1;
    // 被选择的多张图片
    private List<ImagePickerModel> checkImages = new ArrayList<>();
    private boolean showCamera;

    public ImagePickerAdapter(Context context) {
        this.context = context;
    }

    public ImagePickerAdapter(@NonNull Context context, @NonNull List<ImagePickerModel> imagePickerModels) {
        this.context = context;
        this.imagePickerModels = imagePickerModels;
    }

    /**
     * 设置所有图片数据
     *
     * @param imagePickerModels
     */
    public void setImagePickerModels(@NonNull List<ImagePickerModel> imagePickerModels) {
        this.imagePickerModels = imagePickerModels;
        notifyDataSetChanged();
    }

    /**
     * 设置最大张数
     *
     * @param maxCount
     */
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * 多选时增加或移除图片到选中集合
     *
     * @param position 选中的位置
     * @return 增加或移除图片  true：增加；false：移除
     */
    public boolean addOrClearCheckedPosition(int position) {
        int index = showCamera ? position - 1 : position;
        if (checkImages.size() >= maxCount && !checkImages.contains(imagePickerModels.get(index))) {
            UIUtils.showToast(context, "最多选择" + maxCount + "张图片");
            return false;
        }

        boolean result;
        if (checkImages.contains(imagePickerModels.get(index))) {
            checkImages.remove(imagePickerModels.get(index));
            result = false;
        } else {
            checkImages.add(imagePickerModels.get(index));
            result = true;
        }
        notifyDataSetChanged();
        return result;
    }

    /**
     * 获取选中的图片
     *
     * @return 选中的图片集合
     */
    public List<ImagePickerModel> getCheckImages() {
        return checkImages;
    }

    /**
     * 是否需要显示相机
     *
     * @param showCamera
     */
    public void isOpenCamera(boolean showCamera) {
        this.showCamera = showCamera;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imagePickerModels == null ? (showCamera ? 1 : 0) : (showCamera ? (imagePickerModels.size() + 1) : imagePickerModels.size());
    }

    @Override
    public Object getItem(int position) {
        return imagePickerModels.get(showCamera ? position - 1 : position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0)
            return CAMERA_VIEW;
        return ITEM_VIEW;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (CAMERA_VIEW == getItemViewType(position)) {
            ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.default_image_picker_camera_item, parent, false);
            return imageView;
        } else {
            ViewHolder viewHolder;
            if (convertView == null || convertView instanceof ImageView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.default_image_picker_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.setData(imagePickerModels.get(showCamera ? position - 1 : position));

            if (maxCount > 1) {
                viewHolder.checkBox.setVisibility(View.VISIBLE);
                if (checkImages.contains(imagePickerModels.get(showCamera ? position - 1 : position)))
                    viewHolder.checkBox.setChecked(true);
                else
                    viewHolder.checkBox.setChecked(false);
            } else {
                viewHolder.checkBox.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View view) {
            view.setTag(this);
            imageView = view.findViewById(R.id.iv_item);
            checkBox = view.findViewById(R.id.item_checkbox);
        }

        public void setData(@NonNull ImagePickerModel imagePickerModel) {
            ImagePickerLoaderUtils.loadImage(imagePickerModel.path, imageView);
        }
    }
}