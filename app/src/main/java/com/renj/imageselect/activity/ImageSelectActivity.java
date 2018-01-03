package com.renj.imageselect.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.renj.imageselect.R;
import com.renj.imageselect.adapter.ImageMenuAdapter;
import com.renj.imageselect.adapter.ImageSelectAdapter;
import com.renj.imageselect.model.FolderModel;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageSelectConfig;
import com.renj.imageselect.utils.LoadSDImageUtils;
import com.renj.imageselect.utils.OnResultCallBack;
import com.renj.imageselect.weight.ImageClipView;
import com.renj.imageselect.weight.ImageClipMoreLayout;

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
public class ImageSelectActivity extends AppCompatActivity implements View.OnClickListener {
    // 图片选择页面
    private final int STATU_IMAGE_SELECT_PAGE = 0x01;
    // 裁剪单个图片页面
    private final int STATU_CLIP_SINGLE_PAGE = 0x02;
    // 裁剪多个图片页面
    private final int STATU_CLIP_MORE_PAGE = 0x03;
    // 当前页面
    private int currentStatu;

    /***** 页面基本控件 *****/
    private GridView gvImages;
    private ListView lvMenu;
    private DrawerLayout drawerLayout;
    private ViewStub vsClipSingle; // 裁剪单张图片时加载
    private ViewStub vsClipMore; // 裁剪多张图片时加载

    /***** 多选图片时的取消、确认(已选张数)等控件 *****/
    private LinearLayout llSelectView;
    private TextView tvCancelSelect, tvConfirmSelect;
    private RelativeLayout selectMoreTitle;

    /***** 裁剪单张图片时使用到的控件 *****/
    private TextView tvCancel, tvClip;
    private LinearLayout clipLayout;

    /***** 裁剪多张图片时使用到的控件 *****/
    private ImageClipView imageClipView;
    private ImageClipMoreLayout clipMoreLayout;

    private ImageSelectAdapter imageSelectAdapter; // 图片展示的适配器
    private ImageMenuAdapter imageMenuAdapter;     // 目录的适配器
    private ImageSelectConfig imageSelectConfig;   // 保存图片选择配置信息的对象

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_select_activity);

        // 获取配置参数
        imageSelectConfig = getIntent().getParcelableExtra("imageSelectConfig");

        /***** 页面基本控件 *****/
        gvImages = findViewById(R.id.gv_images);
        lvMenu = findViewById(R.id.lv_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        vsClipSingle = findViewById(R.id.vs_clip_single);
        vsClipSingle = findViewById(R.id.vs_clip_single);

        /***** 多选图片时的取消、确认(已选张数)等控件 *****/
        tvCancelSelect = findViewById(R.id.tv_cancel_select);
        tvConfirmSelect = findViewById(R.id.tv_confirm_select);
        llSelectView = findViewById(R.id.ll_select_view);

        /***** 裁剪单张图片时使用到的控件 *****/
        tvClip = findViewById(R.id.tv_clip);
        tvCancel = findViewById(R.id.tv_cancel);
        imageClipView = findViewById(R.id.image_clip_view);
        clipLayout = findViewById(R.id.clip_layout);

        /***** 裁剪多张图片时使用到的控件 *****/
        selectMoreTitle = findViewById(R.id.rl_select_more);
        clipMoreLayout = findViewById(R.id.image_clip_more);

        imageSelectAdapter = new ImageSelectAdapter(this);
        imageMenuAdapter = new ImageMenuAdapter(this);
        gvImages.setAdapter(imageSelectAdapter);
        lvMenu.setAdapter(imageMenuAdapter);

        // 配置数据解析
        configDataParse();
        // 显示图片选择界面
        pageStatuChange(STATU_IMAGE_SELECT_PAGE);
        // 设置各个条目监听
        setItemListener();

        // 根据系统版本申请权限并加载SD卡图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        } else {
            startLoadImage();
        }
    }

    private void configDataParse() {
        imageSelectAdapter.setMaxCount(9);
    }

    private void setItemListener() {
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

        // 图片点击监听
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemData = parent.getItemAtPosition(position);
                if (itemData instanceof ImageModel) {
                    ImageModel imageModel = (ImageModel) itemData;
                    imageSelectAdapter.addOrClearCheckedPosition(position);
                    tvConfirmSelect.setText("(" + imageSelectAdapter.getCheckImages().size() + " / 9) 确定");
                }
            }
        });

//        tvCancelSelect.setOnClickListener(this);
//        tvConfirmSelect.setOnClickListener(this);
//        tvCancel.setOnClickListener(this);
//        tvClip.setOnClickListener(this);
    }

    /**
     * 开始从SD卡中加载图片
     */
    private void startLoadImage() {
        LoadSDImageUtils.loadImageForSdCaard(this, new LoadSDImageUtils.LoadImageForSdCardFinishListener() {
            @Override
            public void finish(List<ImageModel> imageModels, List<FolderModel> folderModels) {
                imageSelectAdapter.setImageModels(imageModels);
                imageMenuAdapter.setFolderModels(folderModels);
            }
        });
    }

    /**
     * 根据参数显示不同页面
     *
     * @param page
     */
    private void pageStatuChange(int page) {
        if (clipLayout == null && clipMoreLayout == null) return;
        if (currentStatu != page) {
            if (STATU_IMAGE_SELECT_PAGE == page) {
                if (clipLayout != null)
                    clipLayout.setVisibility(View.GONE);
                if (clipMoreLayout != null)
                    clipMoreLayout.setVisibility(View.GONE);
                llSelectView.setVisibility(View.VISIBLE);
            } else if (STATU_CLIP_SINGLE_PAGE == page) {
                llSelectView.setVisibility(View.GONE);
                if (clipMoreLayout != null)
                    clipMoreLayout.setVisibility(View.GONE);
                if (clipLayout != null)
                    clipLayout.setVisibility(View.VISIBLE);
            } else {
                llSelectView.setVisibility(View.GONE);
                if (clipLayout != null)
                    clipLayout.setVisibility(View.GONE);
                if (clipMoreLayout != null) {
                    clipMoreLayout.setVisibility(View.VISIBLE);
                    clipMoreLayout.setOnImageClipMoreListener(new ImageClipMoreLayout.OnImageClipMoreListener() {
                        @Override
                        public void cancel() {
                            ImageSelectActivity.this.finish();
                        }

                        @Override
                        public void finish(List<ImageModel> clipResult) {
                            if (create().onResultCallBack != null)
                                create().onResultCallBack.onResult(clipResult);
                            ImageSelectActivity.this.finish();
                        }
                    });
                }
            }
        }
        currentStatu = page;
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
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

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.tv_cancel:
            case R.id.tv_cancel_select:
                finish();
                break;
            case R.id.tv_confirm_select:
                List<ImageModel> checkImages = imageSelectAdapter.getCheckImages();
                if (checkImages.size() <= 0) {
                    Toast.makeText(ImageSelectActivity.this, "没有选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                clipMoreLayout.setImageData(checkImages);
                pageStatuChange(STATU_CLIP_MORE_PAGE);
                break;
            case R.id.tv_clip:
                ImageModel imageModel = imageClipView.cut();
                if (create().onResultCallBack != null)
                    create().onResultCallBack.onResult(imageModel);
                finish();
                break;
            default:
                break;
        }
    }

    private static ImageSelectObservable imageSelectObservable;

    public static ImageSelectObservable create() {
        if (imageSelectObservable == null)
            imageSelectObservable = new ImageSelectObservable();
        return imageSelectObservable;
    }

    public static class ImageSelectObservable {
        OnResultCallBack onResultCallBack;
        ImageSelectConfig imageSelectConfig;

        ImageSelectObservable() {
        }

        public ImageSelectObservable clipConfig(@NonNull ImageSelectConfig imageSelectConfig) {
            this.imageSelectConfig = imageSelectConfig;
            return this;
        }

        public ImageSelectObservable openImageSelectPage(@NonNull Context context) {
            Intent intent = new Intent(context, ImageSelectActivity.class);
            intent.putExtra("imageSelectConfig", imageSelectConfig);
            context.startActivity(intent);
            return this;
        }

        public void onResult(@NonNull OnResultCallBack onResultCallBack) {
            this.onResultCallBack = onResultCallBack;
        }
    }
}
