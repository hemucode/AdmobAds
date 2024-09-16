package com.codehemu.admobads;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class NativeTemplateView extends FrameLayout {

    public NativeTemplateView(@NonNull Context context) {
        super(context);
    }

    public NativeTemplateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public NativeTemplateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public NativeTemplateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {

        TypedArray attributes =
                context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.NativeTemplateView, 0, 0);

        int templateType;
        try {
            templateType =
                    attributes.getResourceId(
                            R.styleable.NativeTemplateView_codehemu_template_type, R.layout.small_template_view);
        } finally {
            attributes.recycle();
        }
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(templateType, this);
    }


}
