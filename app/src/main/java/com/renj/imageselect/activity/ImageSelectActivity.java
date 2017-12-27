package com.renj.imageselect.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.renj.imageselect.R;
import com.renj.imageselect.adapter.ImageSelectAdapter;
import com.renj.imageselect.model.FolderModel;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.select.ImageSelectUtil;

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
    private ImageSelectAdapter imageSelectAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        gvImages = findViewById(R.id.gv_images);

        imageSelectAdapter = new ImageSelectAdapter(this);
        gvImages.setAdapter(imageSelectAdapter);

        requestPermissions();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
            }
        } else {
            ImageSelectUtil.loadImageForSdCaard(this, new ImageSelectUtil.LoadImageForSdCardFinishListener() {
                @Override
                public void finish(List<ImageModel> imageModels, List<FolderModel> folderModels) {
                    imageSelectAdapter.setImageModels(imageModels);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 12 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ImageSelectUtil.loadImageForSdCaard(this, new ImageSelectUtil.LoadImageForSdCardFinishListener() {
                @Override
                public void finish(List<ImageModel> imageModels, List<FolderModel> folderModels) {
                    imageSelectAdapter.setImageModels(imageModels);
                }
            });
        }
    }
}
