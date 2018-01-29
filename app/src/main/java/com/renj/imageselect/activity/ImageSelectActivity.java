package com.renj.imageselect.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.renj.imageselect.R;
import com.renj.imageselect.adapter.ImageSelectAdapter;
import com.renj.imageselect.model.FolderModel;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageSelectConfig;
import com.renj.imageselect.utils.LoadSDImageUtils;
import com.renj.imageselect.utils.Logger;
import com.renj.imageselect.utils.OnResultCallBack;
import com.renj.imageselect.utils.Utils;
import com.renj.imageselect.weight.ImageClipMoreLayout;
import com.renj.imageselect.weight.ImageClipView;
import com.renj.imageselect.weight.ImageMenuDialog;

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
public class ImageSelectActivity extends AppCompatActivity implements View.OnClickListener {
    // 图片选择页面
    private final int STATU_IMAGE_SELECT_PAGE = 0x01;
    // 裁剪单个图片页面
    private final int STATU_CLIP_SINGLE_PAGE = 0x02;
    // 裁剪多个图片页面
    private final int STATU_CLIP_MORE_PAGE = 0x03;
    // 打开相机请求码
    private final int REQUEST_CAMERA = 0x04;

    // 当前页面
    private int currentStatu;

    /***** 页面基本控件 *****/
    private GridView gvImages;
    private TextView tvSelectMenu;
    private ViewStub vsClipSingle; // 裁剪单张图片时加载
    private ViewStub vsClipMore; // 裁剪多张图片时加载
    private LinearLayout llSelectView;

    /***** 多选图片时的取消、确认(已选张数)等控件 *****/
    private TextView tvCancelSelect, tvConfirmSelect;

    /***** 裁剪单张图片时使用到的控件 *****/
    private TextView tvCancel, tvClip;
    private ImageClipView imageClipView;
    private LinearLayout clipLayout;

    /***** 裁剪多张图片时使用到的控件 *****/
    private ImageClipMoreLayout clipMoreLayout;

    private ImageSelectAdapter imageSelectAdapter; // 图片展示的适配器
    private ImageSelectConfig imageSelectConfig;   // 保存图片选择配置信息的对象
    private File cameraSavePath; // 相机照片保存路径
    private ImageMenuDialog imageMenuDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_select_activity);

        // 获取配置参数
        imageSelectConfig = getIntent().getParcelableExtra("imageSelectConfig");

        /***** 页面基本控件 *****/
        gvImages = findViewById(R.id.gv_images);
        tvSelectMenu = findViewById(R.id.tv_select_menu);
        vsClipSingle = findViewById(R.id.vs_clip_single);
        vsClipMore = findViewById(R.id.vs_clip_more);
        llSelectView = findViewById(R.id.ll_select_view);

        tvSelectMenu.setOnClickListener(this);

        imageMenuDialog = new ImageMenuDialog(this);

        imageSelectAdapter = new ImageSelectAdapter(this);
        gvImages.setAdapter(imageSelectAdapter);

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

    /**
     * 解析配置数据
     */
    private void configDataParse() {
        if (imageSelectConfig == null)
            defaultConfig();

        isSelectMore(imageSelectConfig.getSelectCount() > 1);
        imageSelectAdapter.isOpenCamera(imageSelectConfig.isShowCamera());

        if (imageSelectConfig.isClip()) {
            if (imageSelectConfig.getSelectCount() > 1) {
                initClipMorePage();
                clipMoreLayout.setClipViewParams(imageSelectConfig);
            } else {
                initClipSinglePage();
                imageClipView.setClipViewParams(imageSelectConfig);
            }
        }
    }

    /**
     * 没有配置信息时，使用默认的配置信息
     */
    private void defaultConfig() {
        imageSelectConfig = new ImageSelectConfig
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
        tvCancelSelect = findViewById(R.id.tv_cancel_select);
        tvCancelSelect.setOnClickListener(this);

        // 多选图片时的确认控件
        tvConfirmSelect = findViewById(R.id.tv_confirm_select);

        // 判断是否多选
        if (selectMore) {
            // 告诉Adapter最多选择的图片
            imageSelectAdapter.setMaxCount(imageSelectConfig.getSelectCount());

            tvConfirmSelect.setText("(" + imageSelectAdapter.getCheckImages().size() + " / " + imageSelectConfig.getSelectCount() + ") 确定");

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
        vsClipSingle.setLayoutResource(R.layout.image_clip_single_layout);
        View clipSingleView = vsClipSingle.inflate();

        /***** 裁剪单张图片时使用到的控件 *****/
        tvClip = clipSingleView.findViewById(R.id.tv_clip);
        tvCancel = clipSingleView.findViewById(R.id.tv_cancel);
        imageClipView = clipSingleView.findViewById(R.id.image_clip_view);
        clipLayout = clipSingleView.findViewById(R.id.clip_layout);

        tvCancel.setOnClickListener(this);
        tvClip.setOnClickListener(this);
    }

    /**
     * 初始化裁剪多张图片页面
     */
    private void initClipMorePage() {
        vsClipMore.setLayoutResource(R.layout.image_single_clip_more_layout);
        View clipMoreleView = vsClipMore.inflate();

        /***** 裁剪多张图片时使用到的控件 *****/
        clipMoreLayout = clipMoreleView.findViewById(R.id.image_clip_more);
    }

    /**
     * 设置条目相关监听
     */
    private void setItemListener() {
        // 图片点击监听
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectCount = imageSelectConfig.getSelectCount();
                if (imageSelectConfig.isShowCamera() && position == 0) {
                    if (imageSelectAdapter.getCheckImages().size() >= selectCount) {
                        Toast.makeText(ImageSelectActivity.this, "最多选择" + selectCount + "张图片", Toast.LENGTH_SHORT).show();
                    } else {
                        openCamera();
                    }
                    return;
                }

                Object itemData = parent.getItemAtPosition(position);
                if (itemData instanceof ImageModel) {
                    // 判断是否选择单张还是多张
                    if (selectCount > 1) {
                        selectMore(position);
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
        cameraSavePath = Utils.getCameraSavePath();
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

            imageSelectAdapter.notifyDataSetChanged();
            ImageModel imageModel = new ImageModel(cameraSavePath.getAbsolutePath(), cameraSavePath.getName(), cameraSavePath.lastModified());
            Logger.i(imageModel.path);

            handlerCameraResult(imageModel);
        }
    }

    /**
     * 处理相机照完之后的结果
     *
     * @param imageModel
     */
    private void handlerCameraResult(@NonNull ImageModel imageModel) {
        if (imageSelectConfig.getSelectCount() == 1) {
            // 如果是单张，判断是否需要裁剪或直接返回结果
            selectSingle(imageModel);
        } else {
            // 多张时，也直接判断是否需要裁剪然后返回
            List<ImageModel> checkImages = imageSelectAdapter.getCheckImages();
            checkImages.add(imageModel);
            if (imageSelectConfig.isClip()) {
                pageStatuChange(STATU_CLIP_MORE_PAGE);
            } else {
                if (create().onResultCallBack != null)
                    create().onResultCallBack.onResult(checkImages);
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
        if (imageSelectConfig.isClip()) {
            imageClipView.setImage(imageModel.path);
            pageStatuChange(STATU_CLIP_SINGLE_PAGE);
        } else {
            if (create().onResultCallBack != null)
                create().onResultCallBack.onResult(imageModel);
            ImageSelectActivity.this.finish();
        }
    }

    /**
     * 选择多张图片时点击图片操作
     *
     * @param position
     */
    private void selectMore(int position) {
        imageSelectAdapter.addOrClearCheckedPosition(position);
        tvConfirmSelect.setText("(" + imageSelectAdapter.getCheckImages().size() + " / " + imageSelectConfig.getSelectCount() + ") 确定");
    }

    /**
     * 开始从SD卡中加载图片
     */
    private void startLoadImage() {
        LoadSDImageUtils.loadImageForSdCaard(this, new LoadSDImageUtils.LoadImageForSdCardFinishListener() {
            @Override
            public void finish(List<ImageModel> imageModels, List<FolderModel> folderModels) {
                imageSelectAdapter.setImageModels(imageModels);
                imageMenuDialog.setMenuData(folderModels);
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
                    List<ImageModel> checkImages = imageSelectAdapter.getCheckImages();
                    clipMoreLayout.setImageData(checkImages);
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
        switch (vId) {
            case R.id.tv_select_menu:
                imageMenuDialog.show();
                break;
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
                if (imageSelectConfig.isClip()) {
                    pageStatuChange(STATU_CLIP_MORE_PAGE);
                } else {
                    if (create().onResultCallBack != null)
                        create().onResultCallBack.onResult(checkImages);
                    finish();
                }
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

    /**
     * 提供设置配置参数、打开图片选择界面的返回结果回调方法
     */
    public static class ImageSelectObservable {
        OnResultCallBack onResultCallBack;
        ImageSelectConfig imageSelectConfig;

        ImageSelectObservable() {
        }

        /**
         * 设置选择和裁剪配置参数<br/>
         * <b>注意：如果需要配置选择或裁剪参数，一定要先调用 {@link #clipConfig(ImageSelectConfig)} 方法，
         * 在调用 {@link #openImageSelectPage(Context)} 方法。
         * 如果不调用，将使用 {@link com.renj.imageselect.model.DefaultConfigData} 中的数据。
         * 默认选择的张数为1，如果用List集合接收将抛出 {@link ClassCastException} 异常。</b>
         *
         * @param imageSelectConfig {@link ImageSelectConfig} 对象
         * @return {@link ImageSelectObservable} 对象
         */
        public ImageSelectObservable clipConfig(@NonNull ImageSelectConfig imageSelectConfig) {
            this.imageSelectConfig = imageSelectConfig;
            return this;
        }

        /**
         * 打开图片选择界面<br/>
         * <b>注意：如果需要配置选择或裁剪参数，一定要先调用 {@link #clipConfig(ImageSelectConfig)} 方法，
         * 在调用 {@link #openImageSelectPage(Context)} 方法。
         * 如果不调用，将使用 {@link com.renj.imageselect.model.DefaultConfigData} 中的数据。
         * 默认选择的张数为1，如果用List集合接收将抛出 {@link ClassCastException} 异常。</b>
         *
         * @param context 上下文
         * @return {@link ImageSelectObservable} 对象
         */
        public ImageSelectObservable openImageSelectPage(@NonNull Context context) {
            Intent intent = new Intent(context, ImageSelectActivity.class);
            intent.putExtra("imageSelectConfig", imageSelectConfig);
            context.startActivity(intent);
            return this;
        }

        /**
         * 获取返回结果的回调<br/>
         * <b>{@link OnResultCallBack} 注意泛型：</b>
         * <br/>&nbsp;&nbsp;&nbsp;&nbsp;
         * <b>1.当需要选择或裁剪的只是单张图片时，泛型应该为 {@link com.renj.imageselect.model.ImageModel}</b>
         * <br/>&nbsp;&nbsp;&nbsp;&nbsp;
         * <b>2.当需要选择或裁剪的只是多张图片时，泛型应该为 List<{@link com.renj.imageselect.model.ImageModel}></{@link></b>
         *
         * @param onResultCallBack 结果回调
         */
        public void onResult(@NonNull OnResultCallBack onResultCallBack) {
            this.onResultCallBack = onResultCallBack;
        }
    }
}
