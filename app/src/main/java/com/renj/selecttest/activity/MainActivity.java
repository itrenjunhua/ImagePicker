package com.renj.selecttest.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.renj.imageselect.listener.OnClipImageChange;
import com.renj.imageselect.listener.OnResultCallBack;
import com.renj.imageselect.listener.OnSelectedImageChange;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.ImageSelectUtils;
import com.renj.selecttest.R;

import java.util.List;

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

    // 多张图片
    private void moreImages(int imageNumInt) {
        ImageParamsConfig imageParamsConfig1 = new ImageParamsConfig
                .Builder()
                .width(600)
                .height(800)
                .clipBorderWidth(0.5f)
                .selectCount(imageNumInt)
                .isShowCamera(true)
                .maskColor(Color.parseColor("#80000000"))
                .clipBorderColor(Color.parseColor("#ffffff"))
                .isClip(true)
                .isCircleClip(true)
                .maxScale(6f)
                .minScale(0.8f)
                .isContinuityEnlarge(false)
                .build();
        ImageSelectUtils.newInstance().create()
                .selectedLayoutId(R.layout.my_selected_iamge_layout)
                .onSelectedImageChange(new OnSelectedImageChange() {
                    @Override
                    public void onDefault(@NonNull TextView confirmView, @NonNull TextView cancelView, int selectedCount, int totalCount) {
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }

                    @Override
                    public void onSelectedChange(@NonNull TextView confirmView, @NonNull TextView cancelView, @NonNull ImageModel imageModel, boolean isSelected,
                                                 @NonNull List<ImageModel> selectedList, int selectedCount, int totalCount) {
                        Toast.makeText(MainActivity.this, isSelected + " : " + selectedCount, Toast.LENGTH_SHORT).show();
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }
                })
                .onClipImageChange(new OnClipImageChange() {
                    @Override
                    public void onClipChange(@NonNull TextView clipView, @NonNull TextView cancelView,
                                             @NonNull ImageModel imageModel, @NonNull List<ImageModel> clipResultList,
                                             boolean isCircleClip, int clipCount, int totalCount) {
                        Log.i("MainActivity", "clipView = [" + clipView + "], cancelView = [" + cancelView + "], imageModel = [" + imageModel + "], clipResultList = [" + clipResultList + "], isCircleClip = [" + isCircleClip + "], clipCount = [" + clipCount + "], totalCount = [" + totalCount + "]");
                    }
                })
                .imageParamsConfig(imageParamsConfig1)
                .openImageSelectPage(MainActivity.this)
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                    }
                });
    }

    // 单张图片
    private void singleImage() {
        ImageParamsConfig imageParamsConfig = new ImageParamsConfig
                .Builder()
                .width(200)
                .height(300)
                .clipBorderWidth(3)
                .selectCount(1)
                .isShowCamera(true)
                .maskColor(Color.parseColor("#88000000"))
                .clipBorderColor(Color.parseColor("#ff0000"))
                .isClip(true)
                .isCircleClip(false)
                .build();
        ImageSelectUtils.newInstance().create()
                .imageParamsConfig(imageParamsConfig)
                .openImageSelectPage(MainActivity.this)
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                    }
                });
    }
}
