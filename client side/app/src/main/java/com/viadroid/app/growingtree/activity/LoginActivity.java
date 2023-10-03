package com.viadroid.app.growingtree.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

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

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_password)
    EditText et_password;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        et_phone.setText(sp.getString(K.USER_NAME, ""));
        et_password.setText(sp.getString(K.PASSWORD, ""));
    }

    @Override
    public void setListener() {

    }

    @Override
    public void getData() {

    }

    @OnClick(R.id.tv_to_register_user)
    void toRegisterUser() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.tv_to_forgot_pass)
    void toForgotPassword() {
        startActivity(new Intent(this, RetrievePasswordActivity.class));

    }

    @OnClick(R.id.btn_login)
    void login() {
        showProgress("Login...");

        String phone = et_phone.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            showToast("Mobile phone number/password is empty");
            return;
        }

        OkHttp.getInstance()
                .addQueryParameter("username", phone)
                .addQueryParameter("password", MD5Util.md5(password))
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
                            sp.putString(K.USER_NAME, phone);
                            sp.putString(K.PASSWORD, password);

                            User user = result.getData();
                            App.getApp().setUser(user);

                            if (user.getBaby() != null) {
                                App.getApp().setBaby(user.getBaby());
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            } else {
                                startActivity(new Intent(getBaseContext(), AddBabyActivity.class));
                            }
                            finish();
                        } else {
                            showToast(result.getMsg());
                        }
                        dismissProgress();
                    }
                });
    }

}
