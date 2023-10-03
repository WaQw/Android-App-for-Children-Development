package com.viadroid.app.growingtree.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.base.BaseActivity;
import com.viadroid.app.growingtree.entries.ResultWrapper;
import com.viadroid.app.growingtree.entries.User;
import com.viadroid.app.growingtree.util.Constants;
import com.viadroid.app.growingtree.util.GsonUtil;
import com.viadroid.app.growingtree.util.K;
import com.viadroid.app.growingtree.util.L;
import com.viadroid.app.growingtree.util.MD5Util;
import com.viadroid.app.growingtree.util.OkHttp;

import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_get_code)
    Button btn_get_code;


    private CountDownTimer mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_get_code.setEnabled(false);
            ColorStateList colorStateList = getColorStateList(R.color.picture_color_grey);
            btn_get_code.setBackgroundTintList(colorStateList);
            btn_get_code.setText(millisUntilFinished / 1000 + "second");
        }

        @Override
        public void onFinish() {
            btn_get_code.setEnabled(true);
            ColorStateList colorStateList = getColorStateList(R.color.colorPrimary);
            btn_get_code.setBackgroundTintList(colorStateList);
            btn_get_code.setText("Get verification code");
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Log.d(TAG, "handleMessage args: " + msg.arg1 + "," + msg.arg2);
            Log.d(TAG, "handleMessage: " + msg.obj.toString());

            int resultCode = msg.arg2;

            int eventCode = msg.arg1;

            if (resultCode == SMSSDK.RESULT_COMPLETE) {
                switch (eventCode) {
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        showToast("Get verification code successfully");
                        break;
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        showToast("Submitted verification code successfully");
                        toRegister();
                        break;
                }
            } else {
                mCountDownTimer.cancel();
                mCountDownTimer.onFinish();
                ((Throwable) msg.obj).printStackTrace();
                showToast("Failure");
            }
        }
    };

    private EventHandler mEventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            mHandler.sendMessage(msg);
        }
    };
    ;
    private String mPhone;
    private String mPassword;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        //回传用户隐私授权结果
        MobSDK.submitPolicyGrantResult(true, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void aVoid) {
                L.d(TAG, "submitPolicyGrantResult onComplete");
            }

            @Override
            public void onFailure(Throwable throwable) {
                L.d(TAG, "submitPolicyGrantResult onFailure");
            }
        });


        //注册一个事件回调监听，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(mEventHandler);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void getData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mEventHandler);
    }

    @OnClick(R.id.btn_get_code)
    void getCode() {
        String phone = et_phone.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            showToast("Mobile phone number is empty");
            return;
        }

        //中国大陆区域 +86
        SMSSDK.getVerificationCode("86", phone);// 获取短信验证码

        mCountDownTimer.start();
    }

    @OnClick(R.id.btn_next)
    void checkCode() {
        mPhone = et_phone.getText().toString().trim();
        mPassword = et_password.getText().toString().trim();
        String code = et_code.getText().toString().trim();

        if (TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mPassword)) {
            showToast("Mobile phone number/password is empty");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showToast("Verification code is empty");
            return;
        }

        SMSSDK.submitVerificationCode("86", mPhone, code);
    }

    private void toRegister() {

        showProgress("Registering...");


        OkHttp.getInstance()
                .setUrl(Constants.REGISTER_URL)
                .addBodyParam("username", mPhone)
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
