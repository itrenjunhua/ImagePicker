package com.renj.imageselect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageSelectConfig;
import com.renj.imageselect.utils.ImageSelectUtil;
import com.renj.imageselect.utils.OnResultCallBack;

public class MainActivity extends AppCompatActivity {
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
                // 单张图片
                ImageSelectConfig imageSelectConfig = new ImageSelectConfig
                        .Builder()
                        .width(200)
                        .height(300)
                        .clipLineWidth(3)
                        .selectCount(1)
                        .isClip(true)
                        .isCircleClip(true)
                        .build();
                ImageSelectUtil.create()
                        .clipConfig(imageSelectConfig)
                        .openImageSelectPage(MainActivity.this)
                        .onResult(new OnResultCallBack<ImageModel>() {
                            @Override
                            public void onResult(ImageModel selectResult) {
                                Glide.with(MainActivity.this).load(selectResult.path).into(imageView);
                            }
                        });

                // 多张图片
                ImageSelectConfig imageSelectConfig1 = new ImageSelectConfig
                        .Builder()
                        .width(400)
                        .height(500)
                        .clipLineWidth(3)
                        .selectCount(3)
                        .isClip(false)
                        .isCircleClip(true)
                        .build();
//                ImageSelectUtil.create()
//                        .clipConfig(imageSelectConfig1)
//                        .openImageSelectPage(MainActivity.this)
//                        .onResult(new OnResultCallBack<List<ImageModel>>() {
//                            @Override
//                            public void onResult(List<ImageModel> selectResults) {
//                                Toast.makeText(MainActivity.this, "一共选择了" + selectResults.size() + "张图片", Toast.LENGTH_SHORT).show();
//                                Glide.with(MainActivity.this).load(selectResults.get(0).path).into(imageView);
//                            }
//                        });
            }
        });
    }
}
