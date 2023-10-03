package com.viadroid.app.growingtree.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.viadroid.app.growingtree.util.L;
import com.viadroid.app.growingtree.util.Sp;
import com.viadroid.app.growingtree.util.Ts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String BASE_TAG = BaseActivity.class.getSimpleName();

    protected String TAG = this.getClass().getSimpleName();

    protected Sp sp;

    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(BASE_TAG, this.getClass().getName());

        sp = new Sp.Builder(getBaseContext()).setMode(Context.MODE_PRIVATE).build();

        setContentView(getLayoutResID());

        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);

        initView();
        setListener();

        getData();
    }

    public abstract int getLayoutResID();

    public abstract void initView();

    public abstract void setListener();

    public abstract void getData();

    public void onClick(View v) {

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showToast(String msg) {
        Ts.showShort(this, msg);
    }

    public void showProgress(String msg) {
        showProgress("", msg);
    }

    public void showProgress(String title, String msg) {
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void dismissProgress() {
        mProgressDialog.dismiss();
    }
}
