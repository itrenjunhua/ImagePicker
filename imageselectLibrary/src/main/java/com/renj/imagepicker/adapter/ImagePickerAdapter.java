package com.renj.imagepicker.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.model.ImageModel;
import com.renj.imagepicker.utils.ImagePickerHelp;
import com.renj.imagepicker.utils.ConfigUtils;

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
    private List<ImageModel> imageModels;
    private int maxCount = 1;
    // 被选择的多张图片
    private List<ImageModel> checkImages = new ArrayList<>();
    private boolean showCamera;
    @LayoutRes
    private int itemCameraLayoutId;
    @LayoutRes
    private int itemImageLayoutId;

    public ImagePickerAdapter(Context context, @LayoutRes int itemCameraLayoutId, @LayoutRes int itemImageLayoutId) {
        this.context = context;
        this.itemCameraLayoutId = itemCameraLayoutId;
        this.itemImageLayoutId = itemImageLayoutId;
    }

    public ImagePickerAdapter(@NonNull Context context, @NonNull List<ImageModel> imageModels) {
        this.context = context;
        this.imageModels = imageModels;
    }

    /**
     * 设置所有图片数据
     *
     * @param imageModels
     */
    public void setImageModels(@NonNull List<ImageModel> imageModels) {
        this.imageModels = imageModels;
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
        if (checkImages.size() >= maxCount && !checkImages.contains(imageModels.get(index))) {
            if (ConfigUtils.isShowLogger())
                ConfigUtils.e("最多选择" + maxCount + "张图片");
            ConfigUtils.showToast(context, "最多选择" + maxCount + "张图片");
            return false;
        }

        boolean result;
        if (checkImages.contains(imageModels.get(index))) {
            checkImages.remove(imageModels.get(index));
            result = false;
        } else {
            checkImages.add(imageModels.get(index));
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
    public List<ImageModel> getCheckImages() {
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
        return imageModels == null ? (showCamera ? 1 : 0) : (showCamera ? (imageModels.size() + 1) : imageModels.size());
    }

    @Override
    public Object getItem(int position) {
        return imageModels.get(showCamera ? position - 1 : position);
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
            ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(itemCameraLayoutId, parent, false);
            return imageView;
        } else {
            ViewHolder viewHolder;
            if (convertView == null || convertView instanceof ImageView) {
                convertView = LayoutInflater.from(context).inflate(itemImageLayoutId, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.setData(imageModels.get(showCamera ? position - 1 : position));

            if (maxCount > 1) {
                viewHolder.checkBox.setVisibility(View.VISIBLE);
                if (checkImages.contains(imageModels.get(showCamera ? position - 1 : position)))
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

        public void setData(@NonNull ImageModel imageModel) {
            ImagePickerHelp.getInstance().loadImage(imageModel.path, imageView);
        }
    }
}
