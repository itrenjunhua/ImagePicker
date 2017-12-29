package com.renj.imageselect.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.renj.imageselect.R;
import com.renj.imageselect.adapter.ImageMenuAdapter;
import com.renj.imageselect.adapter.ImageSelectAdapter;
import com.renj.imageselect.model.FolderModel;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.utils.LoadSDImageUtil;
import com.renj.imageselect.weight.ImageClipLayout;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-27   16:42
 * <p>
 * 描述：图片选择界面
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageSelectActivity extends AppCompatActivity {
    private GridView gvImages;
    private ListView lvMenu;
    private DrawerLayout drawerLayout;
    private LinearLayout clipLayout;
    private TextView tvCancel, tvClip;
    private ImageClipLayout imageClipLayout;

    private ImageSelectAdapter imageSelectAdapter;
    private ImageMenuAdapter imageMenuAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity_select);
        gvImages = findViewById(R.id.gv_images);
        lvMenu = findViewById(R.id.lv_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        clipLayout = findViewById(R.id.clip_layout);
        tvCancel = findViewById(R.id.tv_cancel);
        tvClip = findViewById(R.id.tv_clip);
        imageClipLayout = findViewById(R.id.image_clip_layout);

        imageSelectAdapter = new ImageSelectAdapter(this);
        imageMenuAdapter = new ImageMenuAdapter(this);
        gvImages.setAdapter(imageSelectAdapter);
        lvMenu.setAdapter(imageMenuAdapter);

        setListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        } else {
            startLoadImage();
        }
    }

    private void setListener() {
        // 目录条目监听
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemData = parent.getItemAtPosition(position);
                if (itemData instanceof FolderModel) {
                    FolderModel folderModel = (FolderModel) itemData;
                    imageSelectAdapter.setImageModels(folderModel.folders);
                }
                drawerLayout.closeDrawers();
            }
        });

        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemData = parent.getItemAtPosition(position);
                if (itemData instanceof ImageModel) {
                    ImageModel imageModel = (ImageModel) itemData;
                    gvImages.setVisibility(View.GONE);
                    clipLayout.setVisibility(View.VISIBLE);
                    imageClipLayout.setImage(imageModel.path);
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageModel imageModel = imageClipLayout.cut();
                imageClipLayout.setImage(imageModel.path);
            }
        });
    }

    /**
     * 开始从SD卡中加载图片
     */
    private void startLoadImage() {
        LoadSDImageUtil.loadImageForSdCaard(this, new LoadSDImageUtil.LoadImageForSdCardFinishListener() {
            @Override
            public void finish(List<ImageModel> imageModels, List<FolderModel> folderModels) {
                imageSelectAdapter.setImageModels(imageModels);
                imageMenuAdapter.setFolderModels(folderModels);
            }
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
            }
        } else {
            startLoadImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 12 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLoadImage();
        }
    }
}
