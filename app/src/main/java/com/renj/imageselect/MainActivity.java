package com.renj.imageselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.renj.imageselect.activity.ImageSelectActivity;
import com.renj.imageselect.model.ImageModel;

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
                Intent intent = new Intent(MainActivity.this, ImageSelectActivity.class);
                startActivityForResult(intent, REQUEST_IMAGE_SELECT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REQUEST_IMAGE_SELECT == requestCode && data != null) {
            ImageModel imageModel = data.getParcelableExtra("result");
            Glide.with(this).load(imageModel.path).into(imageView);
        }
    }
}
