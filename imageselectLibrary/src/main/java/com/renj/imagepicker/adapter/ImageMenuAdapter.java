package com.renj.imagepicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.model.FolderModel;
import com.renj.imagepicker.utils.ConfigUtils;
import com.renj.imagepicker.utils.ImagePickerHelp;

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
public class ImageMenuAdapter extends BaseAdapter {
    private Context context;
    private List<FolderModel> folderModels;
    private int selectPosition = 0;

    public ImageMenuAdapter(Context context) {
        this.context = context;
    }

    public ImageMenuAdapter(Context context, List<FolderModel> folderModels) {
        this.context = context;
        this.folderModels = folderModels;
    }

    /**
     * 设置数据
     */
    public void setFolderModels(List<FolderModel> folderModels) {
        this.folderModels = folderModels;
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
        return folderModels == null ? 0 : folderModels.size();
    }

    @Override
    public Object getItem(int position) {
        return folderModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_menu_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(folderModels.get(position), position);
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

        public void setData(FolderModel folderModel, int position) {
            if (ConfigUtils.isShowLogger())
                ConfigUtils.i("目录：" + folderModel.name + " => 图片数：" + folderModel.totalCount);
            textView.setText(folderModel.name + "(" + folderModel.totalCount + ")");
            ImagePickerHelp.getInstance().loadImage(folderModel.folders.get(0).path, imageView);

            if (position == selectPosition) {
                rbMenu.setChecked(true);
                rbMenu.setVisibility(View.VISIBLE);
            } else {
                rbMenu.setVisibility(View.GONE);
            }
        }
    }
}
