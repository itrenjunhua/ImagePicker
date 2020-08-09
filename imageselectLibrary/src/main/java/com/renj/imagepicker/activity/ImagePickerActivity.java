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
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.adapter.ImagePickerAdapter;
import com.renj.imagepicker.model.FolderModel;
import com.renj.imagepicker.model.ImageModel;
import com.renj.imagepicker.model.ImagePickerParams;
import com.renj.imagepicker.utils.ConfigUtils;
import com.renj.imagepicker.utils.ImageFileUtils;
import com.renj.imagepicker.utils.ImagePickerHelp;
import com.renj.imagepicker.utils.LoadSDImageUtils;
import com.renj.imagepicker.weight.ImageCropMoreLayout;
import com.renj.imagepicker.weight.ImageCropView;
import com.renj.imagepicker.weight.ImageMenuDialog;
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
public class ImagePickerActivity extends AppCompatActivity implements View.OnClickListener {
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

    /***** 选择图片控件 *****/
    private View selectView;
    private GridView gvImages;
    private TextView tvSelectMenu;
    private ViewStub vsClipSingle; // 裁剪单张图片时加载
    private ViewStub vsClipMore; // 裁剪多张图片时加载
    private LinearLayout llSelectView;

    /***** 多选图片时的取消、确认(已选张数)等控件 *****/
    private TextView tvCancelSelect, tvConfirmSelect;

    /***** 裁剪单张图片时使用到的控件 *****/
    private TextView tvCancel, tvClip;
    private ImageCropView imageCropView;
    private LinearLayout clipLayout;

    /***** 裁剪多张图片时使用到的控件 *****/
    private ImageCropMoreLayout clipMoreLayout;

    private ImagePickerAdapter imagePickerAdapter; // 图片展示的适配器
    private ImagePickerParams imagePickerParams;   // 保存图片选择配置信息的对象
    private File cameraSavePath; // 相机照片保存路径
    private ImageMenuDialog imageMenuDialog; // 图片目录选择Dialog
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_select_activity);

        loadingDialog = new LoadingDialog(this);

        // 获取配置参数
        imagePickerParams = getIntent().getParcelableExtra("imageParamsConfig");
        // 初始化选择图片界面
        initSelectedImageView();

        vsClipSingle = findViewById(R.id.vs_clip_single);
        vsClipMore = findViewById(R.id.vs_clip_more);

        // 配置数据解析
        configDataParse();
        // 显示图片选择界面
        pageStatusChange(STATUS_IMAGE_SELECT_PAGE);
        // 设置各个条目监听
        setItemListener();

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
        ViewStub vsSelect = findViewById(R.id.vs_select);
        vsSelect.setLayoutResource(imagePickerParams.getSelectedLayoutId());

        selectView = vsSelect.inflate();
        /***** 页面基本控件 *****/
        gvImages = selectView.findViewById(R.id.gv_images);
        tvSelectMenu = selectView.findViewById(R.id.tv_select_menu);
        llSelectView = selectView.findViewById(R.id.ll_select_view);

        tvSelectMenu.setOnClickListener(this);

        imageMenuDialog = new ImageMenuDialog(this);

        gvImages.setNumColumns(imagePickerParams.getGridColNumbers());
        imagePickerAdapter = new ImagePickerAdapter(this, imagePickerParams.getItemCameraLayoutId(), imagePickerParams.getItemImageLayoutId());
        gvImages.setAdapter(imagePickerAdapter);
    }

    /**
     * 解析配置数据
     */
    private void configDataParse() {
        if (imagePickerParams == null)
            defaultConfig();

        isSelectMore(imagePickerParams.getSelectCount() > 1);
        imagePickerAdapter.isOpenCamera(imagePickerParams.isShowCamera());

        if (imagePickerParams.isCrop()) {
            if (imagePickerParams.getSelectCount() > 1) {
                initClipMorePage();
                clipMoreLayout.setClipViewParams(imagePickerParams);
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
     * 是否选择多张图片
     *
     * @param selectMore true：是 false：不是
     */
    private void isSelectMore(boolean selectMore) {
        // 选择图片页面取消按钮
        tvCancelSelect = selectView.findViewById(R.id.tv_cancel_select);
        tvCancelSelect.setOnClickListener(this);

        // 多选图片时的确认控件
        tvConfirmSelect = selectView.findViewById(R.id.tv_confirm_select);

        // 判断是否多选
        if (selectMore) {
            // 告诉Adapter最多选择的图片
            imagePickerAdapter.setMaxCount(imagePickerParams.getSelectCount());

            tvConfirmSelect.setText("(" + imagePickerAdapter.getCheckImages().size() + " / " + imagePickerParams.getSelectCount() + ") 确定");
            if (ImagePickerHelp.getInstance().getOnSelectedImageChange() != null) {
                ImagePickerHelp.getInstance().getOnSelectedImageChange().onDefault(tvConfirmSelect, tvCancelSelect, imagePickerAdapter.getCheckImages().size(), imagePickerParams.getSelectCount());
            }

            tvConfirmSelect.setVisibility(View.VISIBLE);
            tvConfirmSelect.setOnClickListener(this);
        } else {
            tvConfirmSelect.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化裁剪单张图片页面
     */
    private void initClipSinglePage() {
        vsClipSingle.setLayoutResource(imagePickerParams.getCropSingleLayoutId());

        View clipSingleView = vsClipSingle.inflate();

        /***** 裁剪单张图片时使用到的控件 *****/
        tvClip = clipSingleView.findViewById(R.id.tv_clip);
        tvCancel = clipSingleView.findViewById(R.id.tv_cancel);
        imageCropView = clipSingleView.findViewById(R.id.image_clip_view);
        clipLayout = clipSingleView.findViewById(R.id.clip_layout);

        if (ImagePickerHelp.getInstance().getOnCropImageChange() != null) {
            // 单张裁剪，总数为 1
            ImagePickerHelp.getInstance().getOnCropImageChange().onDefault(tvClip, tvCancel, 1, 1);
        }

        tvCancel.setOnClickListener(this);
        tvClip.setOnClickListener(this);
    }

    /**
     * 初始化裁剪多张图片页面
     */
    private void initClipMorePage() {
        vsClipMore.setLayoutResource(R.layout.image_single_crop_more_layout);
        clipMoreLayout = (ImageCropMoreLayout) vsClipMore.inflate();
        clipMoreLayout.initView(imagePickerParams.getCropMoreLayoutId(), ImagePickerHelp.getInstance().getOnCropImageChange());
    }

    /**
     * 设置条目相关监听
     */
    private void setItemListener() {
        // 目录监听
        imageMenuDialog.setMenuClickListener(new ImageMenuDialog.MenuClickListener() {
            @Override
            public void menuClick(FolderModel folderModel) {
                if (ConfigUtils.isShowLogger())
                    ConfigUtils.i("选中图片目录：" + folderModel);
                tvSelectMenu.setText(folderModel.name);
                imagePickerAdapter.setImageModels(folderModel.folders);
            }
        });

        // 图片点击监听
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectCount = imagePickerParams.getSelectCount();
                if (imagePickerParams.isShowCamera() && position == 0) {
                    if (imagePickerAdapter.getCheckImages().size() >= selectCount) {
                        if (ConfigUtils.isShowLogger())
                            ConfigUtils.e("最多选择" + selectCount + "张图片");
                        ConfigUtils.showToast(ImagePickerActivity.this, "最多选择" + selectCount + "张图片");
                    } else {
                        if (ConfigUtils.isShowLogger())
                            ConfigUtils.i("打开相机进行拍照");
                        openCamera();
                    }
                    return;
                }

                Object itemData = parent.getItemAtPosition(position);
                if (itemData instanceof ImageModel) {
                    // 判断是否选择单张还是多张
                    if (selectCount > 1) {
                        selectMore(position, (ImageModel) itemData);
                    } else {
                        ImageModel imageModel = (ImageModel) itemData;
                        selectSingle(imageModel);
                    }
                }
            }
        });
    }

    /**
     * 打开相机
     */
    private void openCamera() {
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

            imagePickerAdapter.notifyDataSetChanged();
            ImageModel imageModel = new ImageModel(cameraSavePath.getAbsolutePath(), cameraSavePath.getName(), cameraSavePath.lastModified());

            handlerCameraResult(imageModel);
        }
    }

    /**
     * 处理相机照完之后的结果
     *
     * @param imageModel
     */
    private void handlerCameraResult(@NonNull ImageModel imageModel) {
        if (ConfigUtils.isShowLogger())
            ConfigUtils.i("拍照成功，处理结果中");
        if (imagePickerParams.getSelectCount() == 1) {
            // 如果是单张，判断是否需要裁剪或直接返回结果
            selectSingle(imageModel);
        } else {
            // 多张时，也直接判断是否需要裁剪然后返回
            List<ImageModel> checkImages = imagePickerAdapter.getCheckImages();
            checkImages.add(imageModel);
            if (imagePickerParams.isCrop()) {
                pageStatusChange(STATUS_CLIP_MORE_PAGE);
            } else {
                if (ImagePickerHelp.getInstance().getOnResultCallBack() != null)
                    ImagePickerHelp.getInstance().getOnResultCallBack().onResult(checkImages);
                if (ConfigUtils.isShowLogger())
                    ConfigUtils.i("拍照成功，直接返回已选择和拍照图片");
                finish();
            }
        }
    }

    /**
     * 选择单张图片完成
     *
     * @param imageModel
     */
    private void selectSingle(@NonNull ImageModel imageModel) {
        if (imagePickerParams.isCrop()) {
            if (ConfigUtils.isShowLogger())
                ConfigUtils.i("单张图片裁剪");
            imageCropView.setImage(imageModel.path);
            pageStatusChange(STATUS_CLIP_SINGLE_PAGE);
        } else {
            if (ImagePickerHelp.getInstance().getOnResultCallBack() != null) {
                ArrayList<ImageModel> selectResults = new ArrayList<>();
                selectResults.add(imageModel);
                ImagePickerHelp.getInstance().getOnResultCallBack().onResult(selectResults);
            }
            if (ConfigUtils.isShowLogger())
                ConfigUtils.i("单张图片选择完成");
            ImagePickerActivity.this.finish();
        }
    }

    /**
     * 选择多张图片时点击图片操作
     *
     * @param position
     */
    private void selectMore(int position, ImageModel imageModel) {
        boolean isSelected = imagePickerAdapter.addOrClearCheckedPosition(position);
        if (ConfigUtils.isShowLogger()) {
            ConfigUtils.i("图片选择：" + imageModel + " 是否选中：" + isSelected);
            ConfigUtils.i("已选择图片数：" + imagePickerAdapter.getCheckImages().size());
        }
        tvConfirmSelect.setText("(" + imagePickerAdapter.getCheckImages().size() + " / " + imagePickerParams.getSelectCount() + ") 确定");
        if (ImagePickerHelp.getInstance().getOnSelectedImageChange() != null) {
            ImagePickerHelp.getInstance().getOnSelectedImageChange().onSelectedChange(tvConfirmSelect, tvCancelSelect,
                    imageModel, isSelected, imagePickerAdapter.getCheckImages(),
                    imagePickerAdapter.getCheckImages().size(), imagePickerParams.getSelectCount());
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
                        tvSelectMenu.setText("全部图片");
                        imagePickerAdapter.setImageModels(imageModels);
                        imageMenuDialog.setMenuData(folderModels);
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
        if (clipLayout == null && clipMoreLayout == null) return;
        if (currentStatus != page) {
            if (STATUS_IMAGE_SELECT_PAGE == page) {
                if (clipLayout != null)
                    clipLayout.setVisibility(View.GONE);
                if (clipMoreLayout != null)
                    clipMoreLayout.setVisibility(View.GONE);
                llSelectView.setVisibility(View.VISIBLE);
            } else if (STATUS_CLIP_SINGLE_PAGE == page) {
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
                    List<ImageModel> checkImages = imagePickerAdapter.getCheckImages();
                    if (ConfigUtils.isShowLogger())
                        ConfigUtils.i("多张图片裁剪");
                    clipMoreLayout.setImageData(checkImages);
                    clipMoreLayout.setOnImageCropMoreListener(new ImageCropMoreLayout.OnImageCropMoreListener() {
                        @Override
                        public void cancel() {
                            if (ConfigUtils.isShowLogger())
                                ConfigUtils.i("取消图片裁剪");
                            ImagePickerActivity.this.finish();
                        }

                        @Override
                        public void finish(List<ImageModel> clipResult) {
                            if (ConfigUtils.isShowLogger())
                                ConfigUtils.i("多张图片裁剪完成");
                            if (ImagePickerHelp.getInstance().getOnResultCallBack() != null)
                                ImagePickerHelp.getInstance().getOnResultCallBack().onResult(clipResult);
                            ImagePickerActivity.this.finish();
                        }
                    });
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
        if (R.id.tv_select_menu == vId) {
            imageMenuDialog.show();
        } else if (R.id.tv_cancel == vId || R.id.tv_cancel_select == vId) {
            if (ConfigUtils.isShowLogger())
                ConfigUtils.i("取消");
            finish();
        } else if (R.id.tv_confirm_select == vId) {
            List<ImageModel> checkImages = imagePickerAdapter.getCheckImages();
            if (checkImages.size() <= 0) {
                if (ConfigUtils.isShowLogger())
                    ConfigUtils.e("没有选择图片");
                ConfigUtils.showToast(ImagePickerActivity.this, "没有选择图片");
                return;
            }
            if (imagePickerParams.isCrop()) {
                pageStatusChange(STATUS_CLIP_MORE_PAGE);
            } else {
                if (ImagePickerHelp.getInstance().getOnResultCallBack() != null)
                    ImagePickerHelp.getInstance().getOnResultCallBack().onResult(checkImages);
                if (ConfigUtils.isShowLogger())
                    ConfigUtils.i("选择图片完成");
                finish();
            }
        } else if (R.id.tv_clip == vId) {
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
                                ImagePickerHelp.getInstance().getOnCropImageChange().onCropChange(tvClip, tvCancel, imageModel, selectResults, imagePickerParams.isOvalCrop(), 1, 1);
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

    public static void open(Context context, ImagePickerParams imagePickerParams) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra("imageParamsConfig", imagePickerParams);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
