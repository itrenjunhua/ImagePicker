package com.renj.imagepicker.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

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
    private RecyclerView rvMenu;
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

        rvMenu = findViewById(R.id.rv_menu);

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        window.setAttributes(attributes);
        window.setWindowAnimations(R.style.image_picker_dialog_anim);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);

        imagePickerMenuAdapter = new ImagePickerMenuAdapter(getContext());
        rvMenu.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvMenu.setAdapter(imagePickerMenuAdapter);

        setMenuData(imagePickerFolderModels);

        // 目录条目监听
        imagePickerMenuAdapter.setOnItemClickListener(new ImagePickerMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImagePickerFolderModel imagePickerFolderModel, int position) {
                imagePickerMenuAdapter.setSelectPosition(position);
                if (menuClickListener != null)
                    menuClickListener.menuClick(imagePickerFolderModel);
                dismiss();
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
