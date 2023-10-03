package com.viadroid.app.growingtree.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.viadroid.app.growingtree.App;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.base.BaseActivity;
import com.viadroid.app.growingtree.entries.ResultWrapper;
import com.viadroid.app.growingtree.entries.User;
import com.viadroid.app.growingtree.util.Constants;
import com.viadroid.app.growingtree.util.GsonUtil;
import com.viadroid.app.growingtree.util.K;
import com.viadroid.app.growingtree.util.MD5Util;
import com.viadroid.app.growingtree.util.OkHttp;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

public class WelcomeActivity extends BaseActivity {


    private String mPhone;
    private String mPassword;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {

        mPhone = sp.getString(K.USER_NAME, "");
        mPassword = sp.getString(K.PASSWORD, "");

        if (TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mPassword)) {
            toLoginActivity();
        } else {
            login();
        }


    }

    @Override
    public void setListener() {

    }

    @Override
    public void getData() {

    }

    private void login() {

        OkHttp.getInstance()
                .addQueryParameter("username", mPhone)
                .addQueryParameter("password", MD5Util.md5(mPassword))
                .asyncGet(Constants.LOGIN_URL, new OkHttp.NetCallBack() {
                    @Override
                    public void onFailure(Call call) {
                        showToast("Network Error" + call.request().url());
                        dismissProgress();
                    }

                    @Override
                    public void onSuccess(Call call, Response response, String bodyStr) {
                        Type type = new TypeToken<ResultWrapper<User>>() {
                        }.getType();
                        ResultWrapper<User> result = GsonUtil.fromJson(bodyStr, type);

                        if (result.getCode() == 200) {

                            User user = result.getData();
                            App.getApp().setUser(user);

                            if (user.getBaby() != null) {
                                App.getApp().setBaby(user.getBaby());
                                toMainActivity();
                            } else {
                                toAddBabyActivity();
                            }
                        } else {
                            showToast(result.getMsg());
                            toLoginActivity();
                        }
                        dismissProgress();
                    }
                });
    }


    private void toMainActivity() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }, 1000);
    }

    private void toAddBabyActivity() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, AddBabyActivity.class));
            finish();
        }, 1000);
    }

    private void toLoginActivity() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }, 1000);
    }

}
