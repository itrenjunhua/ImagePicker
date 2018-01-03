package com.renj.imageselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageSelectConfig;
import com.renj.imageselect.utils.ImageSelectUtil;
import com.renj.imageselect.utils.OnResultCallBack;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_SELECT = 0x88;
    private Button clickSelect;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickSelect = findViewById(R.id.click_select);
        imageView = findViewById(R.id.imageview);

        clickSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ImageSelectActivity.class);
//                startActivityForResult(intent, REQUEST_IMAGE_SELECT);

                // 单张图片
//                ImageSelectUtil.create()
//                        .openImageSelectPage(MainActivity.this)
//                        .onResult(new OnResultCallBack<ImageModel>() {
//                            @Override
//                            public void onResult(ImageModel selectResult) {
//                                Glide.with(MainActivity.this).load(selectResult.path).into(imageView);
//                            }
//                        });

                // 多张图片
                ImageSelectConfig imageSelectConfig = new ImageSelectConfig.Builder().width(400).build();
                ImageSelectUtil.create()
                        .clipConfig(imageSelectConfig)
                        .openImageSelectPage(MainActivity.this)
                        .onResult(new OnResultCallBack<List<ImageModel>>() {
                            @Override
                            public void onResult(List<ImageModel> selectResults) {
                                Toast.makeText(MainActivity.this, "一共选择了" + selectResults.size() + "张图片", Toast.LENGTH_SHORT).show();
                                Glide.with(MainActivity.this).load(selectResults.get(0).path).into(imageView);
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (RESULT_OK == resultCode && REQUEST_IMAGE_SELECT == requestCode && data != null) {
//            ImageModel imageModel = data.getParcelableExtra("result");
//            Glide.with(this).load(imageModel.path).into(imageView);
//        }
    }
}
