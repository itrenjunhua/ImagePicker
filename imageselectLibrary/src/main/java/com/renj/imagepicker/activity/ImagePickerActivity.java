package com.renj.imagepicker.activity;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.custom.DefaultImagePickerView;
import com.renj.imagepicker.custom.ImagePickerView;
import com.renj.imagepicker.model.FolderModel;
import com.renj.imagepicker.model.ImageModel;
import com.renj.imagepicker.model.ImagePickerParams;
import com.renj.imagepicker.utils.ConfigUtils;
import com.renj.imagepicker.utils.ImageFileUtils;
import com.renj.imagepicker.utils.ImagePickerHelp;
import com.renj.imagepicker.utils.LoadSDImageUtils;
import com.renj.imagepicker.weight.ImageCropMoreLayout;
import com.renj.imagepicker.weight.ImageCropView;
import com.renj.imagepicker.weight.LoadingDialog;

import java.io.File;
import java.util.ArrayList;
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
public class ImagePickerActivity extends AppCompatActivity implements View.OnClickListener, IImagePickerPage {
    // 图片选择页面
    private final int STATUS_IMAGE_SELECT_PAGE = 0x01;
    // 裁剪单个图片页面
    private final int STATUS_CLIP_SINGLE_PAGE = 0x02;
    // 裁剪多个图片页面
    private final int STATUS_CLIP_MORE_PAGE = 0x03;
    // 打开相机请求码
    private final int REQUEST_CAMERA = 0x04;

    // 当前页面
    private int currentStatus;

    private ImagePickerView imagePickerView; // 选择图片控件


    private ViewStub vsCropSingle; // 裁剪单张图片时加载
    private ViewStub vsCropMore; // 裁剪多张图片时加载

    /***** 多选图片时的取消、确认(已选张数)等控件 *****/
    private TextView tvCancelSelect, tvConfirmSelect;

    /***** 裁剪单张图片时使用到的控件 *****/
    private TextView tvCancel, tvCrop;
    private ImageCropView imageCropView;
    private LinearLayout cropLayout;

    /***** 裁剪多张图片时使用到的控件 *****/
    private ImageCropMoreLayout cropMoreLayout;

    private ImagePickerParams imagePickerParams;   // 保存图片选择配置信息的对象
    private File cameraSavePath; // 相机照片保存路径
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker_activity);

        loadingDialog = new LoadingDialog(this);

        // 获取配置参数
        imagePickerParams = getIntent().getParcelableExtra("imageParamsConfig");
        // 初始化选择图片界面
        initSelectedImageView();

        vsCropSingle = findViewById(R.id.vs_crop_single);
        vsCropMore = findViewById(R.id.vs_crop_more);

        // 配置数据解析
        configDataParse();
        // 显示图片选择界面
        pageStatusChange(STATUS_IMAGE_SELECT_PAGE);

        // 根据系统版本申请权限并加载SD卡图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        } else {
            startLoadImage();
        }
    }

    /**
     * 初始化选择图片界面
     */
    private void initSelectedImageView() {
        ViewStub vsSelect = findViewById(R.id.vs_image_picker);
        ViewGroup viewGroup = (ViewGroup) vsSelect.inflate();
        this.imagePickerView = getImagePickerView(this);
        viewGroup.addView(this.imagePickerView);

        this.imagePickerView.setImagePickerOperator(this, imagePickerParams);
    }

    @NonNull
    protected ImagePickerView getImagePickerView(AppCompatActivity activity) {
        return new DefaultImagePickerView(activity);
    }

    /**
     * 解析配置数据
     */
    private void configDataParse() {
        if (imagePickerParams == null)
            defaultConfig();

        if (imagePickerParams.isCrop()) {
            if (imagePickerParams.getSelectCount() > 1) {
                initClipMorePage();
                cropMoreLayout.setClipViewParams(imagePickerParams);
            } else {
                initClipSinglePage();
                imageCropView.setCropViewParams(imagePickerParams);
            }
        }
    }

    /**
     * 没有配置信息时，使用默认的配置信息
     */
    private void defaultConfig() {
        imagePickerParams = new ImagePickerParams
                .Builder()
                .build();
    }

    /**
     * 初始化裁剪单张图片页面
     */
    private void initClipSinglePage() {
        vsCropSingle.setLayoutResource(imagePickerParams.getCropSingleLayoutId());

        View clipSingleView = vsCropSingle.inflate();

        /***** 裁剪单张图片时使用到的控件 *****/
        tvCrop = clipSingleView.findViewById(R.id.tv_crop);
        tvCancel = clipSingleView.findViewById(R.id.tv_cancel);
        imageCropView = clipSingleView.findViewById(R.id.image_crop_view);
        cropLayout = clipSingleView.findViewById(R.id.crop_layout);

        if (ImagePickerHelp.getInstance().getOnCropImageChange() != null) {
            // 单张裁剪，总数为 1
            ImagePickerHelp.getInstance().getOnCropImageChange().onDefault(tvCrop, tvCancel, 1, 1);
        }

        tvCancel.setOnClickListener(this);
        tvCrop.setOnClickListener(this);
    }

    /**
     * 初始化裁剪多张图片页面
     */
    private void initClipMorePage() {
        vsCropMore.setLayoutResource(R.layout.image_single_crop_more_layout);
        cropMoreLayout = (ImageCropMoreLayout) vsCropMore.inflate();
        cropMoreLayout.initView(imagePickerParams.getCropMoreLayoutId(), ImagePickerHelp.getInstance().getOnCropImageChange());
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 启动系统相机
        cameraSavePath = ImageFileUtils.getCameraSavePath();
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

            ImageModel imageModel = new ImageModel(cameraSavePath.getAbsolutePath(), cameraSavePath.getName(), cameraSavePath.lastModified());
            imagePickerView.handlerCameraResult(imageModel);
        }
    }

    /**
     * 开始从SD卡中加载图片
     */
    private void startLoadImage() {
        if (ConfigUtils.isShowLogger())
            ConfigUtils.i("开始加载图片");
        loadingDialog.show();
        LoadSDImageUtils.loadImageForSdCard(this, imagePickerParams, new LoadSDImageUtils.LoadImageForSdCardFinishListener() {
            @Override
            public void finish(final List<ImageModel> imageModels, final List<FolderModel> folderModels) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ConfigUtils.isShowLogger())
                            ConfigUtils.i("图片加载完成");
                        imagePickerView.onLoadImageFinish(imageModels, folderModels);
                        loadingDialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 根据参数显示不同页面
     *
     * @param page
     */
    private void pageStatusChange(int page) {
        if (cropLayout == null && cropMoreLayout == null) return;
        if (currentStatus != page) {
            if (STATUS_IMAGE_SELECT_PAGE == page) {
                if (cropLayout != null)
                    cropLayout.setVisibility(View.GONE);
                if (cropMoreLayout != null)
                    cropMoreLayout.setVisibility(View.GONE);
                imagePickerView.setVisibility(View.VISIBLE);
            } else if (STATUS_CLIP_SINGLE_PAGE == page) {
                imagePickerView.setVisibility(View.GONE);
                if (cropMoreLayout != null)
                    cropMoreLayout.setVisibility(View.GONE);
                if (cropLayout != null)
                    cropLayout.setVisibility(View.VISIBLE);
            } else {
                imagePickerView.setVisibility(View.GONE);
                if (cropLayout != null)
                    cropLayout.setVisibility(View.GONE);
                if (cropMoreLayout != null) {
                    cropMoreLayout.setVisibility(View.VISIBLE);
//                    List<ImageModel> checkImages = imagePickerAdapter.getCheckImages();
//                    if (ConfigUtils.isShowLogger())
//                        ConfigUtils.i("多张图片裁剪");
//                    cropMoreLayout.setImageData(checkImages);
//                    cropMoreLayout.setOnImageCropMoreListener(new ImageCropMoreLayout.OnImageCropMoreListener() {
//                        @Override
//                        public void cancel() {
//                            if (ConfigUtils.isShowLogger())
//                                ConfigUtils.i("取消图片裁剪");
//                            ImagePickerActivity.this.finish();
//                        }
//
//                        @Override
//                        public void finish(List<ImageModel> clipResult) {
//                            if (ConfigUtils.isShowLogger())
//                                ConfigUtils.i("多张图片裁剪完成");
//                            if (ImagePickerHelp.getInstance().getOnResultCallBack() != null)
//                                ImagePickerHelp.getInstance().getOnResultCallBack().onResult(clipResult);
//                            ImagePickerActivity.this.finish();
//                        }
//                    });
                }
            }
        }
        currentStatus = page;
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
        if (R.id.tv_cancel == vId || R.id.tv_cancel_select == vId) {
            if (ConfigUtils.isShowLogger())
                ConfigUtils.i("取消");
            finish();
        } else if (R.id.tv_crop == vId) {
            loadingDialog.show();
            imageCropView.cut(new ImageCropView.CutListener() {
                @Override
                public void cutFinish(final ImageModel imageModel) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<ImageModel> selectResults = new ArrayList<>();
                            selectResults.add(imageModel);

                            if (ImagePickerHelp.getInstance().getOnCropImageChange() != null) {
                                // 单张裁剪，总数为 1
                                ImagePickerHelp.getInstance().getOnCropImageChange().onCropChange(tvCrop, tvCancel, imageModel, selectResults, imagePickerParams.isOvalCrop(), 1, 1);
                            }

                            if (ImagePickerHelp.getInstance().getOnResultCallBack() != null) {
                                ImagePickerHelp.getInstance().getOnResultCallBack().onResult(selectResults);
                            }
                            loadingDialog.dismiss();
                            if (ConfigUtils.isShowLogger())
                                ConfigUtils.i("单张裁剪图片完成");
                            finish();
                        }
                    });
                }
            });
        }
    }


    @Override
    public void cancel() {
        finish();
    }

    @Override
    public void confirm(List<ImageModel> imagePickerList) {
        if (ImagePickerHelp.getInstance().getOnResultCallBack() != null)
            ImagePickerHelp.getInstance().getOnResultCallBack().onResult(imagePickerList);
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
