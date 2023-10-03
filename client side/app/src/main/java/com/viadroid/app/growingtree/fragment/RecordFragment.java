package com.viadroid.app.growingtree.fragment;

import android.os.Bundle;

import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.base.BaseFragment;

public class RecordFragment extends BaseFragment {

    public static RecordFragment newInstance() {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_record;
    }

    @Override
    protected void init() {

    }
}
