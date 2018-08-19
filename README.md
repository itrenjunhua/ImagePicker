# ImageSelect
Android 图片选择、裁剪代码库

## 使用
### 配置加载框架
注意：在使用选择和裁剪图片之前，必须先配置用于图片加载的框架，否则不能加载图片。  
目的：在选择裁剪框架中不另外导入图片加载的框架，在主项目中进行配置，这样就可以使用和主项目中相同的图片加载框架，避免使用的不同的框架造成不必要的空间占用

    ImageSelectUtils.newInstance().configImageLoaderModule(new ImageLoaderUtils.ImageLoaderModule() {
        @Override
        public void loadImage(String path, ImageView imageView) {
            // 使用图片加载框架加载图片
            ImageInfoConfig imageInfoConfig = new ImageInfoConfig.Builder()
                    .filePath(path)
                    .target(imageView)
                    .build();
            ImageLoaderManager.getImageLoader().loadImage(imageInfoConfig);
        }
    });

### 选择裁剪图片
1.选择裁剪单张图片

    ImageSelectConfig imageSelectConfig = new ImageSelectConfig
            .Builder()
            .width(200)  // 裁剪宽度
            .height(300) // 裁剪高度
            .clipBorderWidth(3) // 裁剪边框宽度
            .selectCount(1) // 选择和裁剪的图片数量
            .isShowCamera(true) // 是否能调用相机拍照
            .maskColor(Color.parseColor("#88000000")) // 裁剪遮罩层颜色
            .clipBorderColor(Color.parseColor("#ff0000")) // 裁剪框颜色
            .isClip(true) // 是否裁剪
            .isCircleClip(false) // 是否圆形裁剪
            .build();
    ImageSelectUtils.newInstance().create()
            .clipConfig(imageSelectConfig) // 配置信息，必须
            .openImageSelectPage(MainActivity.this)
            .onResult(new OnResultCallBack() {  // 结果回调，单张图片集合大小为1
                @Override
                public void onResult(List<ImageModel> selectResults) {
                    Glide.with(MainActivity.this).load(selectResults.get(0).path).into(imageView);
                }
            });
            
2.选择裁剪多张图片

    ImageSelectConfig imageSelectConfig1 = new ImageSelectConfig
            .Builder()
            .width(600)  // 裁剪宽度
            .height(800) // 裁剪高度
            .clipBorderWidth(0.5f) // 裁剪边框宽度
            .selectCount(5) // 选择和裁剪的图片数量
            .isShowCamera(true) // 是否能调用相机拍照
            .maskColor(Color.parseColor("#80000000")) // 裁剪遮罩层颜色
            .clipBorderColor(Color.parseColor("#ffffff")) // 裁剪框颜色
            .isClip(true)) // 是否裁剪
            .isCircleClip(true) // 是否圆形裁剪
            .maxScale(6f) // 图片最大缩放倍数
            .minScale(0.8f) // 图片最小缩放倍数
            .isContinuityEnlarge(false) // 是否双击连续放大
            .build();
    ImageSelectUtils.newInstance().create()
            .clipConfig(imageSelectConfig1)  // 配置信息，必须
            .openImageSelectPage(MainActivity.this)
            .onResult(new OnResultCallBack() {  // 结果回调
                @Override
                public void onResult(List<ImageModel> selectResults) {
                    Toast.makeText(MainActivity.this, "一共选择了" + selectResults.size() + "张图片", Toast.LENGTH_SHORT).show();
                    Glide.with(MainActivity.this).load(selectResults.get(0).path).into(imageView);
                }
            });

## 附录
相关页面布局文件名称：
* image_select_layout.xml：图片选择布局文件
* image_clip_single_layout.xml：裁剪单张图片布局文件
* image_clip_more_layout.xml：裁剪多张图片布局文件

**注意：修改样式时，不要删除控件或者修改控件的id**
