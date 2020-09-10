package com.renj.imagepicker.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.renj.imagepicker.R;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-02-09   10:17
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class IPLoadingDialog extends Dialog {

    private String loadingText;
    private TextView tvLoadingText;

    public IPLoadingDialog(@NonNull Context context) {
        super(context, R.style.image_picker_loading_dialog_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker_image_loading_view);

        tvLoadingText = findViewById(R.id.tv_loading_text);
        setLoadingText(loadingText);
        setCanceledOnTouchOutside(false);
    }

    public void setLoadingText(String loadingText) {
        if (loadingText == null) loadingText = "加载中...";

        this.loadingText = loadingText;
        if (tvLoadingText != null) {
            tvLoadingText.setText(loadingText);
        }
    }
}
