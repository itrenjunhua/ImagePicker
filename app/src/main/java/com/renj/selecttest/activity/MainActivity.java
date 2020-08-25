package com.renj.selecttest.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.renj.selecttest.R;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bt_selected)
    Button btSelected;
    @BindView(R.id.bt_selected_my)
    Button btSelectedMy;
    @BindView(R.id.bt_crop_single)
    Button btCropSingle;
    @BindView(R.id.bt_crop_single_my)
    Button btCropSingleMy;
    @BindView(R.id.bt_crop_more)
    Button btCropMore;
    @BindView(R.id.bt_crop_more_my)
    Button btCropMoreMy;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        btSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
                startActivity(intent);
            }
        });

        btSelectedMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomImagePickerActivity.class);
                startActivity(intent);
            }
        });

        btCropSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CropSingleActivity.class);
                startActivity(intent);
            }
        });

        btCropSingleMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomCropSingleActivity.class);
                startActivity(intent);
            }
        });

        btCropMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CropMultiActivity.class);
                startActivity(intent);
            }
        });

        btCropMoreMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomCropMultiActivity.class);
                startActivity(intent);
            }
        });
    }
}
