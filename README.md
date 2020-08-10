# ImageSelect
Android 图片选择、裁剪代码库(**imageselectLibrary**)，主要实现了以下功能：
> 选择图片

* 自定义图片保存目录
* 将设备中的图片进行分目录展示，用户可切换目录
* 可设置选择图片张数
* 是否需要显示拍照功能
* 自定义选择图片页面样式以及图片选中样式
* 设置图片选中改变监听

> 裁剪图片

* 以上选择图片功能都包含
* 可设置裁剪图片张数
* 自定义裁剪图片界面样式
* 设置裁剪图片参数(大小，边框样式，裁剪形状[矩形、圆形]等)
* 移动和改变裁剪区域位置和大小、等比例缩放裁剪区域
* 设置图片裁剪改变监听

# 使用

## 配置加载框架
**注意：在使用选择和裁剪图片之前，必须先配置用于图片加载的框架，否则不能加载图片。**  
原因：在主项目中进行配置图片加载框架，不在选择裁剪框架中导入图片加载框架加载图片，避免和主项目使用不同的图片框架造成不必要的空间占用和代码冗余。

	// 配置全局参数和图片加载框架
	ImagePickerConfig pickerConfig = new ImagePickerConfig.Builder()
                    .loggerTag("MyCustomTag")
                    .fileSavePath(new File(""))
                    .showLogger(true)
                    .center(true)
                    .build();
    ImagePickerUtils.init(pickerConfig, new ImageLoaderModule() {
        @Override
        public void loadImage(String path, ImageView imageView) {
            // 使用图片加载框架加载图片
            ImageLoaderManager.loadImageForFile(path, imageView);
        }
    });

	// 或者使用默认全局参数
    ImagePickerUtils.init(new ImageLoaderModule() {
                @Override
                public void loadImage(String path, ImageView imageView) {
                    // 使用图片加载框架加载图片
                    ImageLoaderManager.loadImageForFile(path, imageView);
                }
            });


## 使用选择图片功能
> 使用默认样式选择图片

    ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .selectCount(imageNum)
                .isShowCamera(true)
                .isContinuityEnlarge(false)
                .isCrop(false)
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        imageShowAdapter.setDatas(resultList);
                    }
                })
                .build();
    ImagePickerUtils.start(this, imagePickerParams);

> 使用自定义样式加载图片，并且增加图片选中改变监听

    ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .selectCount(imageNum)
                .isShowCamera(true)
                .isContinuityEnlarge(false)
                .isCrop(false)
                .selectedLayoutId(R.layout.my_selected_layout)
                .selectItemCameraLayoutId(R.layout.my_image_select_camera_item)
                .selectItemImageLayoutId(R.layout.my_image_select_item)
                .onSelectedImageChange(new OnSelectedImageChange() {
                    @Override
                    public void onDefault(@NonNull TextView confirmView, @NonNull TextView cancelView, int selectedCount, int totalCount) {
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }

                    @Override
                    public void onSelectedChange(@NonNull TextView confirmView, @NonNull TextView cancelView, @NonNull ImageModel imageModel, boolean isSelected,
                                                 @NonNull List<ImageModel> selectedList, int selectedCount, int totalCount) {
                        Log.i("SelectedMyActivity", "imageModel = [" + imageModel + "], isSelected = [" + isSelected + "], selectedList = [" + selectedList + "], selectedCount = [" + selectedCount + "], totalCount = [" + totalCount + "]");
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }
                })
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        imageShowAdapter.setDatas(resultList);
                    }
                })
                .build();
    ImagePickerUtils.start(this, imagePickerParams);


## 使用裁剪图片功能
> 使用默认样式裁剪单张图片

    ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .selectCount(1)
                .isShowCamera(true)
                .isCrop(true)
                .width(200)
                .height(200)
                .isOvalCrop(false)
                .cropBorderWidth(2)
                .maskColor(Color.parseColor("#88000000"))
                .cropBorderColor(Color.parseColor("#ff0000"))
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        ImageLoaderManager.loadImageForFile(resultList.get(0).path, ivClipResult);
                    }
                })
                .build();
    ImagePickerUtils.start(this, imagePickerParams);

> 使用自定义样式裁剪单张图片并增加裁剪改变监听

    ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .selectCount(1)
                .isShowCamera(true)
                .isCrop(true)
                .width(200)
                .height(200)
                .isOvalCrop(false)
                .cropBorderWidth(2)
                .maskColor(Color.parseColor("#88000000"))
                .cropBorderColor(Color.parseColor("#ff0000"))
                // .selectedLayoutId(R.layout.my_selected_layout) // 自定义选择图片布局
                .cropSingleLayoutId(R.layout.my_clip_single_layout) // 自定义单张裁剪部分布局
                .onCropImageChange(new OnCropImageChange() {
                    @Override
                    public void onCropChange(@NonNull TextView clipView, @NonNull TextView cancelView,
                                             @NonNull ImageModel imageModel, @NonNull List<ImageModel> cropResultList,
                                             boolean isOvalCrop, int cropCount, int totalCount) {
                        // clipView.setText(clipCount + "/" + totalCount + "裁剪");
                        Log.i("ClipSingleMyActivity", "imageModel = [" + imageModel + "], clipResultList = [" + cropResultList + "], isCircleClip = [" + isOvalCrop + "], clipCount = [" + cropCount + "], totalCount = [" + totalCount + "]");
                    }
                })
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        ImageLoaderManager.loadImageForFile(resultList.get(0).path, ivClipResult);
                    }
                })
                .build();
    ImagePickerUtils.start(this, imagePickerParams);

> 使用默认样式裁剪多张图片

    ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .selectCount(imageNum)
                .isShowCamera(true)
                .isCrop(true)
                .width(240)
                .height(320)
                .isOvalCrop(true)
                .cropBorderWidth(0.5f)
                .maxScale(6f)
                .minScale(0.8f)
                .isContinuityEnlarge(false)
                .maskColor(Color.parseColor("#80000000"))
                .cropBorderColor(Color.parseColor("#ffffff"))
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        imageShowAdapter.setDatas(resultList);
                    }
                })
                .build();
    ImagePickerUtils.start(this, imagePickerParams);

> 使用默认样式裁剪多张图片并增加裁剪改变监听

    ImagePickerParams imagePickerParams = new ImagePickerParams
                .Builder()
                .selectCount(imageNum)
                .isShowCamera(true)
                .isCrop(true)
                .width(240)
                .height(320)
                .isOvalCrop(true)
                .cropBorderWidth(0.5f)
                .maxScale(6f)
                .minScale(0.8f)
                .isContinuityEnlarge(false)
                .maskColor(Color.parseColor("#80000000"))
                .cropBorderColor(Color.parseColor("#ffffff"))
                .selectedLayoutId(R.layout.my_selected_layout)
                .cropMoreLayoutId(R.layout.my_clip_more_layout)
                .onSelectedImageChange(new OnSelectedImageChange() {
                    @Override
                    public void onDefault(@NonNull TextView confirmView, @NonNull TextView cancelView, int selectedCount, int totalCount) {
                        Log.i("ClipMoreMyActivity", "selectedCount = [" + selectedCount + "], totalCount = [" + totalCount + "]");
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }

                    @Override
                    public void onSelectedChange(@NonNull TextView confirmView, @NonNull TextView cancelView, @NonNull ImageModel imageModel, boolean isSelected,
                                                 @NonNull List<ImageModel> selectedList, int selectedCount, int totalCount) {
                        Log.i("ClipMoreMyActivity", "imageModel = [" + imageModel + "], isSelected = [" + isSelected + "], selectedList = [" + selectedList + "], selectedCount = [" + selectedCount + "], totalCount = [" + totalCount + "]");
                        confirmView.setText(selectedCount + "/" + totalCount + "确定");
                    }
                })
                .onCropImageChange(new OnCropImageChange() {
                    @Override
                    public void onCropChange(@NonNull TextView clipView, @NonNull TextView cancelView,
                                             @NonNull ImageModel imageModel, @NonNull List<ImageModel> cropResultList,
                                             boolean isOvalCrop, int cropCount, int totalCount) {
                        Log.i("ClipMoreMyActivity", "imageModel = [" + imageModel + "], clipResultList = [" + cropResultList + "], isCircleClip = [" + isOvalCrop + "], clipCount = [" + cropCount + "], totalCount = [" + totalCount + "]");
                    }
                })
                .onResult(new OnResultCallBack() {
                    @Override
                    public void onResult(List<ImageModel> resultList) {
                        imageShowAdapter.setDatas(resultList);
                    }
                })
                .build();
    ImagePickerUtils.start(this, imagePickerParams);

**注意：以上代码中使用自定义布局修改样式时，请认真阅读对应方法注释。
在对应默认布局文件中有 id 的控件为必须控件，在自定义的布局文件中必须存在，
并且要保证控件类型和id与默认布局文件中的一致，否则抛出异常。**

## 混淆

* imageselectLibrary库

		-keep class com.renj.imagepicker.**{*;}
		-dontwarn com.renj.imagepicker.**
    
* imageloaderlibrary库 [点击查看图片加载框架](https://github.com/itrenjunhua/ImageLoader)

		-keep class com.renj.imageloaderlibrary.**{*;}
		-keep public class * extends com.renj.imageloaderlibrary.config.ImageLoadConfig
		-keep public class * implements com.renj.imageloaderlibrary.loader.IImageLoaderModule
		-keep emum com.renj.imageloaderlibrary.config.ImageLoadLibrary{
          **[] $VALUES;
          public ** valueOf(java.lang.String);
          public *;
        }
		-dontwarn com.renj.imageloaderlibrary.**
    
* glidelibrary库 [点击查看图片加载框架](https://github.com/itrenjunhua/ImageLoader)

		-keep class com.renj.glide.**{*;}
		-dontwarn com.renj.glide.**
		
		# Glide库混淆
		-keep public class * implements com.bumptech.glide.module.GlideModule
        -keep public class * extends com.bumptech.glide.module.AppGlideModule
        -keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
          **[] $VALUES;
          public *;
        }
        # for DexGuard only
        -keepresourcexmlelements manifest/application/meta-data@value=GlideModule



