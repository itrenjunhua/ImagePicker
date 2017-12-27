package com.renj.imageselect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.renj.imageselect.activity.ImageSelectActivity;
import com.renj.imageselect.weight.CropView;
import com.renj.imageselect.weight.PhotoView;

public class MainActivity extends AppCompatActivity {
    private Button clickCrop, clickSelect;
    private PhotoView photoView;
    private CropView cropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoView = findViewById(R.id.photo_view);
        cropView = findViewById(R.id.crop_view);
        clickCrop = findViewById(R.id.click_crop);
        clickSelect = findViewById(R.id.click_select);

        clickSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageSelectActivity.class);
                startActivity(intent);
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
}
