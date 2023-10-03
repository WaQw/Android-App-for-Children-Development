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

public class ModifyUsernameActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText et_phone;
    //    @BindView(R.id.et_code)
//    EditText et_code;
    @BindView(R.id.et_password)
    EditText et_password;
    //    @BindView(R.id.btn_get_code)
//    Button btn_get_code;
    @BindView(R.id.et_new_phone)
    EditText et_new_phone;


    private String mPhone;
    private String mNewPhone;
    private String mPassword;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_modify_username;
    }

    @Override
    public void initView() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void getData() {

    }

    @OnClick(R.id.btn_next)
    void changeUsername() {

        mPhone = et_phone.getText().toString().trim();
        mNewPhone = et_new_phone.getText().toString().trim();
        mPassword = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mNewPhone)) {
            showToast("Mobile phone number/password is empty");
            return;
        }


        showProgress("Changing mobile phone number...");

        OkHttp.getInstance()
                .setUrl(Constants.MODIFY_USERNAME_URL)
                .addBodyParam("username", mPhone)
                .addBodyParam("newUsername", mNewPhone)
                .addBodyParam("password", MD5Util.md5(mPassword))
                .asyncPostForm(new OkHttp.NetCallBack() {
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
                            sp.putString(K.USER_NAME, mPhone);
                            sp.putString(K.PASSWORD, mPassword);

                            App.getApp().setUser(null);
                            startActivity(new Intent(getBaseContext(), LoginActivity.class));
                            finish();
                        } else {
                            showToast(result.getMsg());
                        }
                        dismissProgress();
                    }
                });
    }
}
