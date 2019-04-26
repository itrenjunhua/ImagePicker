package com.renj.imageselect.activity;

import android.Manifest;
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
import android.support.annotation.LayoutRes;
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
import com.renj.imageselect.listener.OnClipImageChange;
import com.renj.imageselect.listener.OnSelectedImageChange;
import com.renj.imageselect.model.FolderModel;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.LoadSDImageUtils;
import com.renj.imageselect.listener.OnResultCallBack;
import com.renj.imageselect.utils.Utils;
import com.renj.imageselect.weight.ImageClipMoreLayout;
import com.renj.imageselect.weight.ImageClipView;
import com.renj.imageselect.weight.ImageMenuDialog;
import com.renj.imageselect.weight.LoadingDialog;

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
public class ImageSelectActivity extends AppCompatActivity implements View.OnClickListener {
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
    private ImageClipView imageClipView;
    private LinearLayout clipLayout;

    /***** 裁剪多张图片时使用到的控件 *****/
    private ImageClipMoreLayout clipMoreLayout;

    private ImageSelectAdapter imageSelectAdapter; // 图片展示的适配器
    private ImageParamsConfig imageParamsConfig;   // 保存图片选择配置信息的对象
    private File cameraSavePath; // 相机照片保存路径
    private ImageMenuDialog imageMenuDialog; // 图片目录选择Dialog
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_select_activity);

        loadingDialog = new LoadingDialog(this);

        // 获取配置参数
        imageParamsConfig = getIntent().getParcelableExtra("imageParamsConfig");
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
        if (create().selectedLayoutId > 0) {
            vsSelect.setLayoutResource(create().selectedLayoutId);
        } else {
            vsSelect.setLayoutResource(R.layout.image_select_layout);
        }

        selectView = vsSelect.inflate();
        /***** 页面基本控件 *****/
        gvImages = selectView.findViewById(R.id.gv_images);
        tvSelectMenu = selectView.findViewById(R.id.tv_select_menu);
        llSelectView = selectView.findViewById(R.id.ll_select_view);

        tvSelectMenu.setOnClickListener(this);

        imageMenuDialog = new ImageMenuDialog(this);

        imageSelectAdapter = new ImageSelectAdapter(this);
        gvImages.setAdapter(imageSelectAdapter);
    }

    /**
     * 解析配置数据
     */
    private void configDataParse() {
        if (imageParamsConfig == null)
            defaultConfig();

        isSelectMore(imageParamsConfig.getSelectCount() > 1);
        imageSelectAdapter.isOpenCamera(imageParamsConfig.isShowCamera());

        if (imageParamsConfig.isClip()) {
            if (imageParamsConfig.getSelectCount() > 1) {
                initClipMorePage();
                clipMoreLayout.setClipViewParams(imageParamsConfig);
            } else {
                initClipSinglePage();
                imageClipView.setClipViewParams(imageParamsConfig);
            }
        }
    }

    /**
     * 没有配置信息时，使用默认的配置信息
     */
    private void defaultConfig() {
        imageParamsConfig = new ImageParamsConfig
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
            imageSelectAdapter.setMaxCount(imageParamsConfig.getSelectCount());

            tvConfirmSelect.setText("(" + imageSelectAdapter.getCheckImages().size() + " / " + imageParamsConfig.getSelectCount() + ") 确定");
            if (create().onSelectedImageChange != null) {
                create().onSelectedImageChange.onDefault(tvConfirmSelect, tvCancelSelect, imageSelectAdapter.getCheckImages().size(), imageParamsConfig.getSelectCount());
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
        if (create().clipSingleLayoutId > 0) {
            vsClipSingle.setLayoutResource(create().clipSingleLayoutId);
        } else {
            vsClipSingle.setLayoutResource(R.layout.image_clip_single_layout);
        }
        View clipSingleView = vsClipSingle.inflate();

        /***** 裁剪单张图片时使用到的控件 *****/
        tvClip = clipSingleView.findViewById(R.id.tv_clip);
        tvCancel = clipSingleView.findViewById(R.id.tv_cancel);
        imageClipView = clipSingleView.findViewById(R.id.image_clip_view);
        clipLayout = clipSingleView.findViewById(R.id.clip_layout);

        if (create().onClipImageChange != null) {
            // 单张裁剪，总数为 1
            create().onClipImageChange.onDefault(tvClip, tvCancel, 1, 1);
        }

        tvCancel.setOnClickListener(this);
        tvClip.setOnClickListener(this);
    }

    /**
     * 初始化裁剪多张图片页面
     */
    private void initClipMorePage() {
        vsClipMore.setLayoutResource(R.layout.image_single_clip_more_layout);
        clipMoreLayout = (ImageClipMoreLayout) vsClipMore.inflate();
        if (create().clipMoreLayoutId > 0) {
            clipMoreLayout.initView(create().clipMoreLayoutId, create().onClipImageChange);
        } else {
            clipMoreLayout.initView(R.layout.image_clip_more_layout, create().onClipImageChange);
        }
    }

    /**
     * 设置条目相关监听
     */
    private void setItemListener() {
        // 目录监听
        imageMenuDialog.setMenuClickListener(new ImageMenuDialog.MenuClickListener() {
            @Override
            public void menuClick(FolderModel folderModel) {
                tvSelectMenu.setText(folderModel.name);
                imageSelectAdapter.setImageModels(folderModel.folders);
            }
        });

        // 图片点击监听
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectCount = imageParamsConfig.getSelectCount();
                if (imageParamsConfig.isShowCamera() && position == 0) {
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

            handlerCameraResult(imageModel);
        }
    }

    /**
     * 处理相机照完之后的结果
     *
     * @param imageModel
     */
    private void handlerCameraResult(@NonNull ImageModel imageModel) {
        if (imageParamsConfig.getSelectCount() == 1) {
            // 如果是单张，判断是否需要裁剪或直接返回结果
            selectSingle(imageModel);
        } else {
            // 多张时，也直接判断是否需要裁剪然后返回
            List<ImageModel> checkImages = imageSelectAdapter.getCheckImages();
            checkImages.add(imageModel);
            if (imageParamsConfig.isClip()) {
                pageStatusChange(STATUS_CLIP_MORE_PAGE);
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
        if (imageParamsConfig.isClip()) {
            imageClipView.setImage(imageModel.path);
            pageStatusChange(STATUS_CLIP_SINGLE_PAGE);
        } else {
            if (create().onResultCallBack != null) {
                ArrayList<ImageModel> selectResults = new ArrayList<>();
                selectResults.add(imageModel);
                create().onResultCallBack.onResult(selectResults);
            }
            ImageSelectActivity.this.finish();
        }
    }

    /**
     * 选择多张图片时点击图片操作
     *
     * @param position
     */
    private void selectMore(int position, ImageModel imageModel) {
        boolean isSelected = imageSelectAdapter.addOrClearCheckedPosition(position);
        tvConfirmSelect.setText("(" + imageSelectAdapter.getCheckImages().size() + " / " + imageParamsConfig.getSelectCount() + ") 确定");
        if (create().onSelectedImageChange != null) {
            create().onSelectedImageChange.onSelectedChange(tvConfirmSelect, tvCancelSelect,
                    imageModel, isSelected, imageSelectAdapter.getCheckImages(),
                    imageSelectAdapter.getCheckImages().size(), imageParamsConfig.getSelectCount());
        }
    }

    /**
     * 开始从SD卡中加载图片
     */
    private void startLoadImage() {
        loadingDialog.show();
        LoadSDImageUtils.loadImageForSdCard(this, new LoadSDImageUtils.LoadImageForSdCardFinishListener() {
            @Override
            public void finish(List<ImageModel> imageModels, List<FolderModel> folderModels) {
                imageSelectAdapter.setImageModels(imageModels);
                imageMenuDialog.setMenuData(folderModels);
                loadingDialog.dismiss();
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
            finish();
        } else if (R.id.tv_confirm_select == vId) {
            List<ImageModel> checkImages = imageSelectAdapter.getCheckImages();
            if (checkImages.size() <= 0) {
                Toast.makeText(ImageSelectActivity.this, "没有选择图片", Toast.LENGTH_SHORT).show();
                return;
            }
            if (imageParamsConfig.isClip()) {
                pageStatusChange(STATUS_CLIP_MORE_PAGE);
            } else {
                if (create().onResultCallBack != null)
                    create().onResultCallBack.onResult(checkImages);
                finish();
            }
        } else if (R.id.tv_clip == vId) {
            loadingDialog.show();
            imageClipView.cut(new ImageClipView.CutListener() {
                @Override
                public void cutFinish(final ImageModel imageModel) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<ImageModel> selectResults = new ArrayList<>();
                            selectResults.add(imageModel);

                            if (create().onClipImageChange != null) {
                                // 单张裁剪，总数为 1
                                create().onClipImageChange.onClipChange(tvClip, tvCancel, imageModel, selectResults, imageParamsConfig.isCircleClip(), 1, 1);
                            }

                            if (create().onResultCallBack != null) {
                                create().onResultCallBack.onResult(selectResults);
                            }
                            loadingDialog.dismiss();
                            finish();
                        }
                    });
                }
            });
        }
    }

    private static ImageSelectObservable imageSelectObservable;

    public static ImageSelectObservable create() {
        if (imageSelectObservable == null) {
            synchronized (ImageSelectObservable.class) {
                if (imageSelectObservable == null)
                    imageSelectObservable = new ImageSelectObservable();
            }
        }
        return imageSelectObservable;
    }

    /**
     * 提供动态布局配置、选择图片参数配置、打开图片选择界面的返回结果回调方法
     */
    public static class ImageSelectObservable {
        OnResultCallBack onResultCallBack;
        ImageParamsConfig imageParamsConfig;

        /*********** 选择图片页面动态布局和回调 ***********/
        @LayoutRes
        int selectedLayoutId; // 选择图片页面布局资源id
        OnSelectedImageChange onSelectedImageChange;  // 图片选择页面，图片选择发生变化时回调

        /*********** 裁剪图片页面动态布局和回调 ***********/
        @LayoutRes
        int clipSingleLayoutId; // 裁剪单张图片页面布局资源 id
        @LayoutRes
        int clipMoreLayoutId; // 裁剪单张图片页面布局资源 id
        OnClipImageChange onClipImageChange; // 图片发生裁剪时回调

        ImageSelectObservable() {
        }

        /**
         * 动态设置图片选择页面的布局。<br/>
         * <b>注意：请参照 默认布局文件 image_select_layout.xml ，在默认布局文件中有 id 的控件为必须控件，
         * 在自定义的布局文件中必须存在，并且要保证控件类型和id与默认布局文件中的一致，否则抛出异常。</b>
         *
         * @param selectedLayoutId 布局文件资源id(如果异常，使用默认布局文件 image_select_layout.xml)
         * @return {@link ImageSelectObservable} 对象
         */
        public ImageSelectObservable selectedLayoutId(@LayoutRes int selectedLayoutId) {
            this.selectedLayoutId = selectedLayoutId;
            return this;
        }

        /**
         * 设置图片选择改变监听。<br/>
         * <b>注意：只有在选择多张图片时才会回调，单张图片并不会回调</b>
         *
         * @param onSelectedImageChange 图片选择页面，图片选择发生变化时回调
         * @return {@link ImageSelectObservable} 对象
         */
        public ImageSelectObservable onSelectedImageChange(@Nullable OnSelectedImageChange onSelectedImageChange) {
            this.onSelectedImageChange = onSelectedImageChange;
            return this;
        }

        /**
         * 动态设置单张图片裁剪页面的布局。<br/>
         * <b>注意：请参照 默认布局文件 image_clip_single_layout.xml ，在默认布局文件中有 id 的控件为必须控件，
         * 在自定义的布局文件中必须存在，并且要保证控件类型和id与默认布局文件中的一致，否则抛出异常。</b>
         *
         * @param clipSingleLayoutId 布局文件资源id(如果异常，使用默认布局文件 image_clip_single_layout.xml)
         * @return {@link ImageSelectObservable} 对象
         */
        public ImageSelectObservable clipSingleLayoutId(@LayoutRes int clipSingleLayoutId) {
            this.clipSingleLayoutId = clipSingleLayoutId;
            return this;
        }

        /**
         * 动态设置多张图片裁剪页面的布局。<br/>
         * <b>注意：请参照 默认布局文件 image_clip_more_layout.xml ，在默认布局文件中有 id 的控件为必须控件，
         * 在自定义的布局文件中必须存在，并且要保证控件类型和id与默认布局文件中的一致，否则抛出异常。</b>
         *
         * @param clipMoreLayoutId 布局文件资源id(如果异常，使用默认布局文件 image_clip_more_layout.xml)
         * @return {@link ImageSelectObservable} 对象
         */
        public ImageSelectObservable clipMoreLayoutId(@LayoutRes int clipMoreLayoutId) {
            this.clipMoreLayoutId = clipMoreLayoutId;
            return this;
        }

        /**
         * 设置图片裁剪改变监听。<br/>
         *
         * @param onClipImageChange 图片裁剪时回调
         * @return {@link ImageSelectObservable} 对象
         */
        public ImageSelectObservable onClipImageChange(@Nullable OnClipImageChange onClipImageChange) {
            this.onClipImageChange = onClipImageChange;
            return this;
        }

        /**
         * 设置选择和裁剪配置参数<br/>
         * <b>注意：如果需要配置选择或裁剪参数，一定要先调用 {@link #imageParamsConfig(ImageParamsConfig)} 方法，
         * 在调用 {@link #openImageSelectPage(Context)} 方法。
         * 如果不调用，将使用 {@link com.renj.imageselect.model.DefaultConfigData} 中的数据。
         * 默认选择的张数为1。</b>
         *
         * @param imageParamsConfig {@link ImageParamsConfig} 对象
         * @return {@link ImageSelectObservable} 对象
         */
        public ImageSelectObservable imageParamsConfig(@NonNull ImageParamsConfig imageParamsConfig) {
            this.imageParamsConfig = imageParamsConfig;
            return this;
        }

        /**
         * 打开图片选择界面<br/>
         * <b>注意：如果需要配置选择或裁剪参数，一定要先调用 {@link #imageParamsConfig(ImageParamsConfig)} 方法，
         * 在调用 {@link #openImageSelectPage(Context)} 方法。
         * 如果不调用，将使用 {@link com.renj.imageselect.model.DefaultConfigData} 中的数据。
         * 默认选择的张数为1。</b>
         *
         * @param context 上下文
         * @return {@link ImageSelectObservable} 对象
         */
        public ImageSelectObservable openImageSelectPage(@NonNull Context context) {
            Intent intent = new Intent(context, ImageSelectActivity.class);
            intent.putExtra("imageParamsConfig", imageParamsConfig);
            context.startActivity(intent);
            return this;
        }

        /**
         * 获取返回结果的回调<br/>
         * <b>{@link OnResultCallBack} 注意：当选择一张图片时，集合的大小为1</b>
         *
         * @param onResultCallBack 结果回调
         */
        public void onResult(@NonNull OnResultCallBack onResultCallBack) {
            this.onResultCallBack = onResultCallBack;
        }
    }
}
