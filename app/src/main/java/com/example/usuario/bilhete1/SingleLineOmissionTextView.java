package com.example.usuario.bilhete1;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import static android.text.TextUtils.TruncateAt.END;

/**
 * Created by Administrator on 2017/12/22.
 */

public class SingleLineOmissionTextView extends AppCompatTextView {
    public SingleLineOmissionTextView(Context context) {
        this(context, null);
    }

    public SingleLineOmissionTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleLineOmissionTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setMaxLines(1);
        setEllipsize(TextUtils.TruncateAt.END);
    }
}






