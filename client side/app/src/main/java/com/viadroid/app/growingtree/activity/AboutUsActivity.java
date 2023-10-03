package com.viadroid.app.growingtree.activity;


import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.base.BaseActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void getData() {

    }

}
