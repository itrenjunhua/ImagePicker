package com.renj.imageselect;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.renj.imageselect.model.FolderModel;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.select.ImageSelectUtil;
import com.renj.imageselect.weight.CropView;
import com.renj.imageselect.weight.PhotoView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button clickCrop, clickSelect;
    private PhotoView photoView;
    private CropView cropView;

    int index = 0;
    private int[] images = {R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, R.mipmap.a1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoView = findViewById(R.id.photo_view);
        cropView = findViewById(R.id.crop_view);
        clickCrop = findViewById(R.id.click_crop);
        clickSelect = findViewById(R.id.click_select);

        requestPermissions();

        clickSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index >= 3)
                    index = 0;

                photoView.setImageDrawable(getResources().getDrawable(images[index]));

                index += 1;
            }
        });

        clickCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropView.confirmCrop(new CropView.OnCropRangeListener() {
                    @Override
                    public void cropRange(CropView.CropShape cropShape, RectF cropRectF) {
                        Log.i("MainActivity", "cropShape = [" + cropShape + "], cropRectF = [" + cropRectF + "]");
                        Bitmap bitmap = photoView.cropBitmap(cropShape, cropRectF);
                        photoView.setImageBitmap(bitmap);
                    }
                });
            }
        });
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
        }else{
            ImageSelectUtil.loadImageForSdCaard(this, new ImageSelectUtil.LoadImageForSdCardFinishListener() {
                @Override
                public void finish(List<ImageModel> imageModels, List<FolderModel> folderModels) {
                    Log.i("MainActivity", "所有图片：" + imageModels.size() + ";分类图片：" + folderModels.size());
                    for (FolderModel folderModel : folderModels) {
                        Log.i("MainActivity", "------- " + folderModel.name);
                    }
//                    for (ImageModel imageModel : imageModels) {
//                        Log.i("MainActivity", imageModel.toString());
//                    }
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
                    Log.i("MainActivity", "所有图片：" + imageModels.size() + ";分类图片：" + folderModels.size());
//                    for (ImageModel imageModel : imageModels) {
//                        Log.i("MainActivity", imageModel.toString());
//                    }
                }
            });
        }
    }
}
