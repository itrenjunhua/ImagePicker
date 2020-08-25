package com.renj.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.renj.imagepicker.custom.ImagePickerCropLayout;
import com.renj.imagepicker.custom.ImagePickerLayout;
import com.renj.imagepicker.listener.IImageCropPage;
import com.renj.imagepicker.listener.IImagePickerPage;
import com.renj.imagepicker.model.ImagePickerFolderModel;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.utils.RImageFileUtils;
import com.renj.imagepicker.utils.RLoadSDImageUtils;
import com.renj.imagepicker.weight.IPLoadingDialog;

import java.io.File;
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
public class ImagePickerActivity extends AppCompatActivity implements IImagePickerPage, IImageCropPage {
    // 图片选择页面
    private final int STATUS_IMAGE_SELECT_PAGE = 0x01;
    // 裁剪单个图片页面
    private final int STATUS_CROP_SINGLE_PAGE = 0x02;
    // 裁剪多个图片页面
    private final int STATUS_CROP_MORE_PAGE = 0x03;
    // 打开相机请求码
    private final int REQUEST_CAMERA = 0x04;
    // 请求权限码
    private final int REQUEST_PERMISSIONS = 0x05;

    private int currentPageStatus; // 当前页面
    private ImagePickerLayout imagePickerLayout; // 选择图片控件
    private ImagePickerCropLayout rImageCropSingleView; // 裁剪单张图片控件
    private ImagePickerCropLayout rImageCropMultiView; // 裁剪多张图片控件

    private List<ImagePickerModel> imagePickerList; // 选择页面选择的图片集合
    private ImagePickerParams imagePickerParams;   // 图片配置信息
    private File cameraSavePath; // 相机照片保存路径
    private IPLoadingDialog loadingDialog; // 加载对话框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker_activity);

        // 获取配置参数
        imagePickerParams = getIntent().getParcelableExtra("imageParamsConfig");
        if (imagePickerParams == null) {
            imagePickerParams = new ImagePickerParams
                    .Builder()
                    .build();
        }
        // 初始化选择图片界面
        initPickerImageView();
        // 显示图片选择界面
        pageStatusChange(STATUS_IMAGE_SELECT_PAGE);
        // 初始化加载框
        loadingDialog = new IPLoadingDialog(this);

        // 根据系统版本申请权限并加载SD卡图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        } else {
            startLoadImage();
        }
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        } else {
            startLoadImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLoadImage();
        }
    }

    /**
     * 开始从SD卡中加载图片
     */
    private void startLoadImage() {
        loadingDialog.show();
        RLoadSDImageUtils.loadImageForSdCard(this, imagePickerParams, new RLoadSDImageUtils.LoadImageForSdCardFinishListener() {
            @Override
            public void finish(final List<ImagePickerModel> imagePickerModels, final List<ImagePickerFolderModel> imagePickerFolderModels) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imagePickerLayout.onLoadImageFinish(imagePickerModels, imagePickerFolderModels);
                        loadingDialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 初始化选择图片界面
     */
    private void initPickerImageView() {
        ViewStub vsSelect = findViewById(R.id.vs_image_picker);
        ViewGroup viewGroup = (ViewGroup) vsSelect.inflate();
        this.imagePickerLayout = RImagePickerHelp.getImagePickerViewModule().onCreateImagePickerView(this);
        viewGroup.addView(this.imagePickerLayout);

        this.imagePickerLayout.setImagePickerOperator(this, imagePickerParams);
    }

    /**
     * 初始化裁剪单张图片页面
     */
    private void initCropSinglePage() {
        ViewStub vsCropSingle = findViewById(R.id.vs_crop_single);
        ViewGroup viewGroup = (ViewGroup) vsCropSingle.inflate();
        rImageCropSingleView = RImagePickerHelp.getImagePickerViewModule().onCreateImagePickerCropSingleView(this);
        viewGroup.addView(this.rImageCropSingleView);

        this.rImageCropSingleView.setImageCropOperator(this, imagePickerParams, imagePickerList);
    }

    /**
     * 初始化裁剪多张图片页面
     */
    private void initCropMultiPage() {
        ViewStub vsCropMulti = findViewById(R.id.vs_crop_more);
        ViewGroup viewGroup = (ViewGroup) vsCropMulti.inflate();
        rImageCropMultiView = RImagePickerHelp.getImagePickerViewModule().onCreateImagePickerCropMultiView(this);
        viewGroup.addView(this.rImageCropMultiView);

        this.rImageCropMultiView.setImageCropOperator(this, imagePickerParams, imagePickerList);
    }

    /**
     * 根据参数显示不同页面
     *
     * @param page
     */
    private void pageStatusChange(int page) {
        if (rImageCropSingleView == null && rImageCropMultiView == null) return;
        if (currentPageStatus != page) {
            if (STATUS_IMAGE_SELECT_PAGE == page) {
                if (rImageCropSingleView != null)
                    rImageCropSingleView.setVisibility(View.GONE);
                if (rImageCropMultiView != null)
                    rImageCropMultiView.setVisibility(View.GONE);
                imagePickerLayout.setVisibility(View.VISIBLE);
            } else if (STATUS_CROP_SINGLE_PAGE == page) {
                imagePickerLayout.setVisibility(View.GONE);
                if (rImageCropMultiView != null)
                    rImageCropMultiView.setVisibility(View.GONE);
                if (rImageCropSingleView != null)
                    rImageCropSingleView.setVisibility(View.VISIBLE);
            } else {
                imagePickerLayout.setVisibility(View.GONE);
                if (rImageCropSingleView != null)
                    rImageCropSingleView.setVisibility(View.GONE);
                if (rImageCropMultiView != null) {
                    rImageCropMultiView.setVisibility(View.VISIBLE);
                }
            }
        }
        currentPageStatus = page;
    }

    /**
     * 打开相机
     */
    @Override
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 启动系统相机
        cameraSavePath = RImageFileUtils.getCameraSavePath();
        if (cameraSavePath == null) return;
        Uri photoUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, cameraSavePath.getAbsolutePath());
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            photoUri = Uri.fromFile(cameraSavePath); // 传递路径
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); // 更改系统默认存储路径
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) { // 如果返回数据
            Intent intent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(cameraSavePath);
            intent1.setData(uri);
            sendBroadcast(intent1);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了

            ImagePickerModel imagePickerModel = new ImagePickerModel(cameraSavePath.getAbsolutePath(),
                    cameraSavePath.getName(), cameraSavePath.lastModified());
            imagePickerLayout.handlerCameraResult(imagePickerModel);
        }
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new IPLoadingDialog(this);
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void showLoading(String loadingText) {
        if (loadingDialog == null)
            loadingDialog = new IPLoadingDialog(this);
        loadingDialog.setLoadingText(loadingText);
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void cancel() {
        finish();
    }

    @Override
    public void confirmPickerFinish(List<ImagePickerModel> imagePickerList) {
        this.imagePickerList = imagePickerList;

        if (imagePickerParams.isCrop()) {
            if (imagePickerParams.getSelectCount() > 1) {
                initCropMultiPage();
                pageStatusChange(STATUS_CROP_MORE_PAGE);
            } else {
                initCropSinglePage();
                pageStatusChange(STATUS_CROP_SINGLE_PAGE);
            }
        } else {
            if (RImagePickerHelp.getOnResultCallBack() != null)
                RImagePickerHelp.getOnResultCallBack().onResult(imagePickerList);
            finish();
        }
    }

    @Override
    public void confirmCropFinish(List<ImagePickerModel> imagePickerList) {
        if (RImagePickerHelp.getOnResultCallBack() != null)
            RImagePickerHelp.getOnResultCallBack().onResult(imagePickerList);
        finish();
    }

    public static void open(Context context, ImagePickerParams imagePickerParams) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra("imageParamsConfig", imagePickerParams);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
