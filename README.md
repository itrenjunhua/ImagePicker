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
                    ImageLoaderManager.loadImageForFile(path, imageView);
                }
            });

### 选择裁剪图片
1.选择裁剪单张图片

    ImageSelectConfig imageSelectConfig = new ImageSelectConfig
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
                    .clipConfig(imageSelectConfig)
                    .openImageSelectPage(MainActivity.this)
                    .onResult(new OnResultCallBack() {
                        @Override
                        public void onResult(List<ImageModel> selectResults) {
                            ImageLoaderManager.loadImageForFile(MainActivity.this, selectResults.get(0).path, imageView);
                        }
                    });
            
2.选择裁剪多张图片

    ImageSelectConfig imageSelectConfig1 = new ImageSelectConfig
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
                    .clipConfig(imageSelectConfig1)
                    .openImageSelectPage(MainActivity.this)
                    .onResult(new OnResultCallBack() {
                        @Override
                        public void onResult(List<ImageModel> selectResults) {
                            Toast.makeText(MainActivity.this, "一共选择了" + selectResults.size() + "张图片", Toast.LENGTH_SHORT).show();
                            ImageLoaderManager.loadImageForFile(MainActivity.this, selectResults.get(0).path, imageView);
                        }
                    });

## 附录
相关页面布局文件名称：
* image_select_layout.xml：图片选择布局文件
* image_clip_single_layout.xml：裁剪单张图片布局文件
* image_clip_more_layout.xml：裁剪多张图片布局文件

**注意：修改样式时，不要删除控件或者修改控件的id**
