package com.renj.imageselect.weight;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renj.imageselect.R;
import com.renj.imageselect.listener.OnCropImageChange;
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageParamsConfig;
import com.renj.imageselect.utils.CommonUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-01-02   14:55
 * <p>
 * 描述：多张图片裁剪控件封装
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageCropMoreLayout extends LinearLayout implements View.OnClickListener {
    private TextView tvCancel;
    private TextView tvCrop;
    private NoScrollViewPager vpCropMore;

    private int currentIndex = 0;
    private List<ImageModel> srcImages = new ArrayList<>();
    private List<ImageModel> resultImages = new ArrayList<>();
    private CropMorePagerAdapter cropMorePagerAdapter;
    private ImageParamsConfig imageParamsConfig;

    private FixedSpeedScroller mScroller;
    private LoadingDialog loadingDialog;

    private OnCropImageChange onCropImageChange;

    public ImageCropMoreLayout(Context context) {
        this(context, null);
    }

    public ImageCropMoreLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCropMoreLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageCropMoreLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initView(@LayoutRes int clipMoreLayoutId, OnCropImageChange onCropImageChange) {
        this.onCropImageChange = onCropImageChange;
        View clipMoreLayout = LayoutInflater.from(getContext()).inflate(clipMoreLayoutId, null);
        tvCancel = clipMoreLayout.findViewById(R.id.tv_cancel_more);
        tvCrop = clipMoreLayout.findViewById(R.id.tv_crop_more);
        vpCropMore = clipMoreLayout.findViewById(R.id.vp_crop_more);
        addView(clipMoreLayout);

        setListener();

        cropMorePagerAdapter = new CropMorePagerAdapter();
        vpCropMore.setAdapter(cropMorePagerAdapter);

        try {
            // 通过class文件获取mScroller属性
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(vpCropMore.getContext(), new AccelerateInterpolator());
            mField.set(vpCropMore, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadingDialog = new LoadingDialog(getContext());
    }

    private void setListener() {
        tvCancel.setOnClickListener(this);
        tvCrop.setOnClickListener(this);
    }

    public void setImageData(List<ImageModel> srcImages) {
        this.srcImages = srcImages;
        if (CommonUtils.isShowLogger())
            CommonUtils.i("裁剪：(" + (currentIndex + 1) + " / " + srcImages.size() + ")");
        tvCrop.setText("(" + (currentIndex + 1) + " / " + srcImages.size() + ")裁剪");
        if (onCropImageChange != null) {
            onCropImageChange.onDefault(tvCrop, tvCancel, currentIndex + 1, srcImages.size());
        }
        cropMorePagerAdapter.notifyDataSetChanged();
    }

    private OnImageCropMoreListener onImageCropMoreListener;

    public void setOnImageCropMoreListener(OnImageCropMoreListener onImageCropMoreListener) {
        this.onImageCropMoreListener = onImageCropMoreListener;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (R.id.tv_cancel_more == vId) {
            if (onImageCropMoreListener != null)
                onImageCropMoreListener.cancel();
        } else if (R.id.tv_crop_more == vId) {
            currentIndex += 1;
            ImageCropView focusedChild = cropMorePagerAdapter.getPrimaryItem();
            focusedChild.cut(new ImageCropView.CutListener() {
                @Override
                public void cutFinish(final ImageModel imageModel) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    resultImages.add(imageModel);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (currentIndex < srcImages.size()) {
                                if (CommonUtils.isShowLogger())
                                    CommonUtils.i("裁剪：(" + (currentIndex + 1) + " / " + srcImages.size() + ")");
                                tvCrop.setText("(" + (currentIndex + 1) + " / " + srcImages.size() + ")裁剪");
                                if (onCropImageChange != null) {
                                    onCropImageChange.onClipChange(tvCrop, tvCancel, imageModel, resultImages, imageParamsConfig.isCircleClip(), (currentIndex + 1), srcImages.size());
                                }
                            }
                        }
                    });
                    if (srcImages.size() <= resultImages.size()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismiss();
                                if (onImageCropMoreListener != null)
                                    onImageCropMoreListener.finish(resultImages);
                            }
                        });
                    }
                }
            });

            if (currentIndex >= srcImages.size()) {
                loadingDialog.show();
                tvCrop.setEnabled(false);
                return;
            }
            mScroller.setDuration(500);// 切换时间，毫秒值
            vpCropMore.setCurrentItem(currentIndex);
        }
    }


    /**
     * 设置裁剪控件参数
     *
     * @param imageParamsConfig
     */
    public void setClipViewParams(@NonNull ImageParamsConfig imageParamsConfig) {
        this.imageParamsConfig = imageParamsConfig;
    }

    public interface OnImageCropMoreListener {
        void cancel();

        void finish(List<ImageModel> clipResult);
    }

    class CropMorePagerAdapter extends PagerAdapter {
        private ImageCropView mCurrentView;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (ImageCropView) object;
        }

        public ImageCropView getPrimaryItem() {
            return mCurrentView;
        }

        @Override
        public int getCount() {
            return srcImages.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageCropView imageCropView = new ImageCropView(getContext());
            imageCropView.setCropViewParams(imageParamsConfig);
            imageCropView.setImage(srcImages.get(position).path);
            container.addView(imageCropView);
            return imageCropView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
