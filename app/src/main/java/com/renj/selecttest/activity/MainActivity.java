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
    @BindView(R.id.bt_clip_single)
    Button btClipSingle;
    @BindView(R.id.bt_clip_single_my)
    Button btClipSingleMy;
    @BindView(R.id.bt_clip_more)
    Button btClipMore;
    @BindView(R.id.bt_clip_more_my)
    Button btClipMoreMy;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        btSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectedActivity.class);
                startActivity(intent);
            }
        });

        btSelectedMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectedMyActivity.class);
                startActivity(intent);
            }
        });

        btClipSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClipSingleActivity.class);
                startActivity(intent);
            }
        });

        btClipSingleMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClipSingleMyActivity.class);
                startActivity(intent);
            }
        });

        btClipMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClipMoreActivity.class);
                startActivity(intent);
            }
        });

        btClipMoreMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClipMoreMyActivity.class);
                startActivity(intent);
            }
        });
    }
}
