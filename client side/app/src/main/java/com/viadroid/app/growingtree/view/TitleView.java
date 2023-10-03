package com.viadroid.app.growingtree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import com.viadroid.app.growingtree.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TitleView extends FrameLayout implements View.OnClickListener {

    private String mTitle;

    private Toolbar mToolbar;

    public TitleView(@NonNull Context context) {
        this(context, null);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_title_view, this);

        mToolbar = findViewById(R.id.toolbar);

    }

    @Override
    public void onClick(View v) {

    }
}
