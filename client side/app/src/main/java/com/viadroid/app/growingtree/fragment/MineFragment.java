package com.viadroid.app.growingtree.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.viadroid.app.growingtree.App;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.activity.AboutUsActivity;
import com.viadroid.app.growingtree.activity.ModifyUsernameActivity;
import com.viadroid.app.growingtree.activity.RetrievePasswordActivity;
import com.viadroid.app.growingtree.base.BaseFragment;
import com.viadroid.app.growingtree.util.K;
import com.viadroid.app.growingtree.util.Sp;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {

    @BindView(R.id.profile_image)
    ImageView profile_image;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init() {
        Glide.with(this).load(App.getApp().getBaby().getHeadImgUrl()).into(profile_image);
    }

    @OnClick(R.id.tv_change_phone)
    void changePhone() {
        startActivity(new Intent(mActivity, ModifyUsernameActivity.class));
    }

    @OnClick(R.id.tv_change_pass)
    void changePassword() {
        startActivity(new Intent(mActivity, RetrievePasswordActivity.class));
    }

    @OnClick(R.id.tv_about_us)
    void aboutUs() {
        startActivity(new Intent(mActivity, AboutUsActivity.class));
    }

    @OnClick(R.id.btn_logout)
    void logout() {
        App.getApp().setUser(null);
        Sp sp = new Sp.Builder(getContext()).setMode(Context.MODE_PRIVATE).build();
        sp.remove(K.USER_NAME);
        sp.remove(K.PASSWORD);

        mActivity.finish();
    }
}
