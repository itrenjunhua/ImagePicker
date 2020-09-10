package com.renj.imagepicker.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.renj.imagepicker.R;
import com.renj.imagepicker.listener.IImageCropPage;
import com.renj.imagepicker.model.ImagePickerModel;
import com.renj.imagepicker.ImagePickerParams;
import com.renj.imagepicker.weight.IPFixedSpeedScroller;
import com.renj.imagepicker.weight.IPNoScrollViewPager;
import com.renj.imagepicker.weight.IPImageCropView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-08-21   17:25
 * <p>
 * 描述：默认多张图片裁剪控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class DefaultImageCropMultiLayout extends ImagePickerCropLayout {
    private TextView tvCancel;
    private TextView tvCrop;
    private IPNoScrollViewPager vpCropMore;

    private int currentIndex = 0;
    private List<ImagePickerModel> resultImages = new ArrayList<>();
    private CropMorePagerAdapter cropMorePagerAdapter;

    private IPFixedSpeedScroller mScroller;

    public DefaultImageCropMultiLayout(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.default_image_crop_multi_layout;
    }

    @Override
    protected void initView(View multiCropView) {
        tvCancel = multiCropView.findViewById(R.id.tv_cancel_more);
        tvCrop = multiCropView.findViewById(R.id.tv_crop_more);
        vpCropMore = multiCropView.findViewById(R.id.vp_crop_more);

        try {
            // 通过class文件获取mScroller属性
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new IPFixedSpeedScroller(vpCropMore.getContext(), new AccelerateInterpolator());
            mField.set(vpCropMore, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCropPage.cancel();
            }
        });

        tvCrop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex += 1;
                IPImageCropView focusedChild = cropMorePagerAdapter.getPrimaryItem();
                focusedChild.cut(new IPImageCropView.CutListener() {
                    @Override
                    public void cutFinish(final ImagePickerModel imagePickerModel) {
                        resultImages.add(imagePickerModel);
                        if (currentIndex < imagePickerList.size()) {
                            tvCrop.setText("(" + (currentIndex + 1) + " / " + imagePickerList.size() + ")裁剪");
                        }
                        if (imagePickerList.size() <= resultImages.size()) {
                            imageCropPage.closeLoading();
                            imageCropPage.confirmCropFinish(resultImages);
                        }
                    }
                });

                if (currentIndex >= imagePickerList.size()) {
                    imageCropPage.showLoading();
                    tvCrop.setEnabled(false);
                    return;
                }
                mScroller.setDuration(500);// 切换时间，毫秒值
                vpCropMore.setCurrentItem(currentIndex);
            }
        });
    }

    @Override
    protected void onParamsInitFinish(IImageCropPage iImagePickerPage,
                                      ImagePickerParams imagePickerParams,
                                      List<ImagePickerModel> imagePickerList) {
        cropMorePagerAdapter = new CropMorePagerAdapter();
        vpCropMore.setAdapter(cropMorePagerAdapter);
        tvCrop.setText("(" + (currentIndex + 1) + " / " + imagePickerList.size() + ")裁剪");
    }

    private class CropMorePagerAdapter extends PagerAdapter {
        private IPImageCropView mCurrentView;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (IPImageCropView) object;
        }

        public IPImageCropView getPrimaryItem() {
            return mCurrentView;
        }

        @Override
        public int getCount() {
            return imagePickerList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            IPImageCropView IPImageCropView = new IPImageCropView(getContext());
            IPImageCropView.setCropViewParams(imagePickerParams);
            IPImageCropView.setImage(imagePickerList.get(position).path);
            container.addView(IPImageCropView);
            return IPImageCropView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
