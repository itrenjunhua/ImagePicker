package com.renj.imagepicker.custom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * 描述：图片选择界面适配器
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return imagePickerModels == null ? (showCamera ? 1 : 0) : (showCamera ? (imagePickerModels.size() + 1) : imagePickerModels.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (CAMERA_VIEW == viewType) {
            ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.default_image_picker_camera_item, parent, false);
            return new ImagePickerCameraHolder(imageView);
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.default_image_picker_item, null);
            return new ImagePickerViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (CAMERA_VIEW == getItemViewType(position)) {
            ImagePickerAdapter.ImagePickerCameraHolder imagePickerCameraHolder = (ImagePickerCameraHolder) holder;
            imagePickerCameraHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(null, position);
                    }
                }
            });
        } else {
            ImagePickerAdapter.ImagePickerViewHolder imagePickerViewHolder = (ImagePickerViewHolder) holder;
            final ImagePickerModel imagePickerModel = imagePickerModels.get(showCamera ? position - 1 : position);
            imagePickerViewHolder.setData(imagePickerModel);
            imagePickerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(imagePickerModel, position);
                    }
                }
            });

            if (maxCount > 1) {
                imagePickerViewHolder.checkBox.setVisibility(View.VISIBLE);
                if (checkImages.contains(imagePickerModel))
                    imagePickerViewHolder.checkBox.setChecked(true);
                else
                    imagePickerViewHolder.checkBox.setChecked(false);
            } else {
                imagePickerViewHolder.checkBox.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0)
            return CAMERA_VIEW;
        return ITEM_VIEW;
    }

    static class ImagePickerCameraHolder extends RecyclerView.ViewHolder {

        public ImagePickerCameraHolder(@NonNull View view) {
            super(view);
        }
    }

    static class ImagePickerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBox;

        public ImagePickerViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_item);
            checkBox = view.findViewById(R.id.item_checkbox);
        }

        public void setData(@NonNull ImagePickerModel imagePickerModel) {
            ImagePickerLoaderUtils.loadImage(imagePickerModel.path, imageView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ImagePickerModel imagePickerModel, int position);
    }
}
