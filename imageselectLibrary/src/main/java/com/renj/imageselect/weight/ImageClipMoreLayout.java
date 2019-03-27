package com.renj.imageselect.weight;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
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
import com.renj.imageselect.model.ImageModel;
import com.renj.imageselect.model.ImageSelectConfig;

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
public class ImageClipMoreLayout extends LinearLayout implements View.OnClickListener {
    private TextView tvCancel;
    private TextView tvClip;
    private NoScrollViewPager vpClipMore;

    private int currentIndex = 0;
    private List<ImageModel> srcImages = new ArrayList<>();
    private List<ImageModel> resultImages = new ArrayList<>();
    private ClipMorePagerAdapter clipMorePagerAdapter;
    private ImageSelectConfig imageSelectConfig;

    private FixedSpeedScroller mScroller;
    private LoadingDialog loadingDialog;

    public ImageClipMoreLayout(Context context) {
        this(context, null);
    }

    public ImageClipMoreLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageClipMoreLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageClipMoreLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View clipMoreLayout = LayoutInflater.from(context).inflate(R.layout.image_clip_more_layout, null);
        tvCancel = clipMoreLayout.findViewById(R.id.tv_cancel_more);
        tvClip = clipMoreLayout.findViewById(R.id.tv_clip_more);
        vpClipMore = clipMoreLayout.findViewById(R.id.vp_clip_more);
        addView(clipMoreLayout);

        setListener();

        clipMorePagerAdapter = new ClipMorePagerAdapter();
        vpClipMore.setAdapter(clipMorePagerAdapter);

        try {
            // 通过class文件获取mScroller属性
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(vpClipMore.getContext(), new AccelerateInterpolator());
            mField.set(vpClipMore, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadingDialog = new LoadingDialog(context);
    }

    private void setListener() {
        tvCancel.setOnClickListener(this);
        tvClip.setOnClickListener(this);
    }

    public void setImageData(List<ImageModel> srcImages) {
        this.srcImages = srcImages;
        tvClip.setText("(" + (currentIndex + 1) + " / " + srcImages.size() + ")裁剪");
        clipMorePagerAdapter.notifyDataSetChanged();
    }

    private OnImageClipMoreListener onImageClipMoreListener;

    public OnImageClipMoreListener getOnImageClipMoreListener() {
        return onImageClipMoreListener;
    }

    public void setOnImageClipMoreListener(OnImageClipMoreListener onImageClipMoreListener) {
        this.onImageClipMoreListener = onImageClipMoreListener;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (R.id.tv_cancel_more == vId) {
            if (onImageClipMoreListener != null)
                onImageClipMoreListener.cancel();
        } else if (R.id.tv_clip_more == vId) {
            currentIndex += 1;

            ImageClipView focusedChild = clipMorePagerAdapter.getPrimaryItem();
            focusedChild.cut(new ImageClipView.CutListener() {
                @Override
                public void cutFinish(ImageModel imageModel) {
                    resultImages.add(imageModel);
                    if (srcImages.size() <= resultImages.size()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismiss();
                                if (onImageClipMoreListener != null)
                                    onImageClipMoreListener.finish(resultImages);
                            }
                        });
                    }
                }
            });

            if (currentIndex >= srcImages.size()) {
                loadingDialog.show();
                tvClip.setEnabled(false);
//                    if (onImageClipMoreListener != null)
//                        onImageClipMoreListener.finish(resultImages);
                return;
            }
            mScroller.setDuration(500);// 切换时间，毫秒值
            vpClipMore.setCurrentItem(currentIndex);
            tvClip.setText("(" + (currentIndex + 1) + " / " + srcImages.size() + ")裁剪");
        }
    }


    /**
     * 设置裁剪控件参数
     *
     * @param imageSelectConfig
     */
    public void setClipViewParams(@NonNull ImageSelectConfig imageSelectConfig) {
        this.imageSelectConfig = imageSelectConfig;
    }

    public interface OnImageClipMoreListener {
        void cancel();

        void finish(List<ImageModel> clipResult);
    }

    class ClipMorePagerAdapter extends PagerAdapter {
        private ImageClipView mCurrentView;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (ImageClipView) object;
        }

        public ImageClipView getPrimaryItem() {
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
            ImageClipView imageClipView = new ImageClipView(getContext());
            imageClipView.setClipViewParams(imageSelectConfig);
            imageClipView.setImage(srcImages.get(position).path);
            container.addView(imageClipView);
            return imageClipView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
