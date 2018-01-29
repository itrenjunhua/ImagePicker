package com.renj.imageselect.weight;

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

import com.renj.imageselect.R;
import com.renj.imageselect.adapter.ImageMenuAdapter;
import com.renj.imageselect.model.FolderModel;

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
public class ImageMenuDialog extends Dialog {
    private ListView lvMenu;
    private ImageMenuAdapter imageMenuAdapter;

    private List<FolderModel> folderModels;
    private MenuClickListener menuClickListener;

    public void setMenuClickListener(MenuClickListener menuClickListener) {
        this.menuClickListener = menuClickListener;
    }

    public ImageMenuDialog(@NonNull Context context) {
        super(context, R.style.alert_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_menu_view);

        lvMenu = findViewById(R.id.lv_menu);

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        window.setAttributes(attributes);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);

        imageMenuAdapter = new ImageMenuAdapter(getContext());
        lvMenu.setAdapter(imageMenuAdapter);

        setMenuData(folderModels);

        // 目录条目监听
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemData = parent.getItemAtPosition(position);
                if (itemData instanceof FolderModel) {
                    FolderModel folderModel = (FolderModel) itemData;
                    imageMenuAdapter.setSelectPosition(position);
                    if (menuClickListener != null)
                        menuClickListener.menuClick(folderModel);
                    dismiss();
                }
            }
        });
    }

    /**
     * 设置目录数据
     */
    public void setMenuData(List<FolderModel> folderModels) {
        this.folderModels = folderModels;
        if (imageMenuAdapter != null)
            imageMenuAdapter.setFolderModels(folderModels);
    }

    /**
     * 目录条目点击监听
     */
    public interface MenuClickListener {
        void menuClick(FolderModel folderModel);
    }
}
