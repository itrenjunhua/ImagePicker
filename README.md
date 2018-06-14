# ImageSelect
Android 图片选择、裁剪代码库

## 使用
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
    ImageSelectUtil.create()
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
    ImageSelectUtil.create()
            .clipConfig(imageSelectConfig1)  // 配置信息，必须
            .openImageSelectPage(MainActivity.this)
            .onResult(new OnResultCallBack() {  // 结果回调
                @Override
                public void onResult(List<ImageModel> selectResults) {
                    Toast.makeText(MainActivity.this, "一共选择了" + selectResults.size() + "张图片", Toast.LENGTH_SHORT).show();
                    Glide.with(MainActivity.this).load(selectResults.get(0).path).into(imageView);
                }
            });
