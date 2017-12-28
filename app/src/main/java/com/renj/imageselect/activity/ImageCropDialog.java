package com.renj.imageselect.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renj.imageselect.R;
import com.renj.imageselect.weight.CropView;
import com.renj.imageselect.weight.PhotoView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-28   15:19
 * <p>
 * 描述：图片裁剪页面
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ImageCropDialog extends Dialog {
    private TextView tvCancel, tvCrop;
    private PhotoView photoView;
    private CropView cropView;

    public ImageCropDialog(@NonNull Context context) {
        super(context, R.style.crop_dialog);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View viewDialog = inflater.inflate(R.layout.activity_image_crop, null);
        setContentView(viewDialog, layoutParams);

        tvCancel = viewDialog.findViewById(R.id.tv_cancel);
        tvCrop = viewDialog.findViewById(R.id.tv_crop);
        photoView = viewDialog.findViewById(R.id.photo_view);
        cropView = viewDialog.findViewById(R.id.crop_view);

        setImageViewBitmap(bitmap);
        setListener();
    }

    private void setListener() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage();
            }
        });
    }

    private void cropImage() {
        cropView.confirmCrop(new CropView.OnCropRangeListener() {
            @Override
            public void cropRange(CropView.CropShape cropShape, RectF cropRectF) {
                Bitmap bitmap = photoView.cropBitmap(cropShape, cropRectF);
                photoView.setImageBitmap(bitmap);
            }
        });
    }

    Bitmap bitmap;

    public void setImageViewBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (photoView != null)
            photoView.setImageBitmap(bitmap);
    }
}
