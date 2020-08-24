package com.renj.imagepicker.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.custom.adapter.ImagePickerMenuAdapter;
import com.renj.imagepicker.model.ImagePickerFolderModel;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-01-29   14:13
 * <p>
 * 描述：图片选择目录弹出框
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImagePickerMenuDialog extends Dialog {
    private ListView lvMenu;
    private ImagePickerMenuAdapter imagePickerMenuAdapter;

    private List<ImagePickerFolderModel> imagePickerFolderModels;
    private MenuClickListener menuClickListener;

    public void setMenuClickListener(MenuClickListener menuClickListener) {
        this.menuClickListener = menuClickListener;
    }

    public ImagePickerMenuDialog(@NonNull Context context) {
        super(context, R.style.image_picker_alert_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_image_menu_view);

        lvMenu = findViewById(R.id.lv_menu);

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        window.setAttributes(attributes);
        window.setWindowAnimations(R.style.image_picker_dialog_anim);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);

        imagePickerMenuAdapter = new ImagePickerMenuAdapter(getContext());
        lvMenu.setAdapter(imagePickerMenuAdapter);

        setMenuData(imagePickerFolderModels);

        // 目录条目监听
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemData = parent.getItemAtPosition(position);
                if (itemData instanceof ImagePickerFolderModel) {
                    ImagePickerFolderModel imagePickerFolderModel = (ImagePickerFolderModel) itemData;
                    imagePickerMenuAdapter.setSelectPosition(position);
                    if (menuClickListener != null)
                        menuClickListener.menuClick(imagePickerFolderModel);
                    dismiss();
                }
            }
        });
    }

    /**
     * 设置目录数据
     */
    public void setMenuData(List<ImagePickerFolderModel> imagePickerFolderModels) {
        this.imagePickerFolderModels = imagePickerFolderModels;
        if (imagePickerMenuAdapter != null)
            imagePickerMenuAdapter.setImagePickerFolderModels(imagePickerFolderModels);
    }

    /**
     * 目录条目点击监听
     */
    public interface MenuClickListener {
        void menuClick(ImagePickerFolderModel imagePickerFolderModel);
    }
}
