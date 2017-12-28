package com.renj.imageselect.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
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
import android.widget.ListView;

import com.renj.imageselect.R;
import com.renj.imageselect.adapter.ImageMenuAdapter;
import com.renj.imageselect.adapter.ImageSelectAdapter;
import com.renj.imageselect.model.FolderModel;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.select.LoadSDImageUtil;

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
    private ImageSelectAdapter imageSelectAdapter;
    private ImageMenuAdapter imageMenuAdapter;
    private ImageCropDialog imageCropDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        gvImages = findViewById(R.id.gv_images);
        lvMenu = findViewById(R.id.lv_menu);
        drawerLayout = findViewById(R.id.drawer_layout);

        imageSelectAdapter = new ImageSelectAdapter(this);
        imageMenuAdapter = new ImageMenuAdapter(this);
        gvImages.setAdapter(imageSelectAdapter);
        lvMenu.setAdapter(imageMenuAdapter);

        imageCropDialog = new ImageCropDialog(this);

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
                    //Intent intent = new Intent(ImageSelectActivity.this,ImageCropDialog.class);
                    //startActivity(intent);
                    imageCropDialog.setImageViewBitmap(BitmapFactory.decodeFile(imageModel.path));
                    imageCropDialog.show();
                }
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
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
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
