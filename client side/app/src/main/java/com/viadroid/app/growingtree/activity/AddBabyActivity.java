package com.viadroid.app.growingtree.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.viadroid.app.growingtree.App;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.base.BaseActivity;
import com.viadroid.app.growingtree.entries.Baby;
import com.viadroid.app.growingtree.entries.ResultWrapper;
import com.viadroid.app.growingtree.util.Constants;
import com.viadroid.app.growingtree.util.GlideEngine;
import com.viadroid.app.growingtree.util.GsonUtil;
import com.viadroid.app.growingtree.util.OkHttp;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

public class AddBabyActivity extends BaseActivity implements OkHttp.NetCallBack {
    private static final String TAG = "AddBabyActivity";

    @BindView(R.id.profile_image)
    ImageView profile_image;
    @BindView(R.id.iv_add_a_photo)
    ImageView iv_add_a_photo;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    @BindView(R.id.et_birthday)
    EditText et_birthday;
    @BindView(R.id.et_head_circle)
    EditText et_head_circle;
    @BindView(R.id.et_height)
    EditText et_height;
    @BindView(R.id.et_weight)
    EditText et_weight;


    private String imgPath;
    private String name;
    private String birthday;
    private int gender;

    private String headCircle;
    private String height;
    private String weight;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_add_baby;
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

    @OnClick(R.id.et_birthday)
    void onBirthdayClick() {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String format = dateFormat.format(calendar.getTime());
                et_birthday.setText(format);

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                timePickerDialog.show();
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }

    @OnClick(R.id.tv_save)
    void onSave() {
        name = et_name.getText().toString();
        birthday = et_birthday.getText().toString();

        headCircle = et_head_circle.getText().toString();
        height = et_height.getText().toString();
        weight = et_weight.getText().toString();

        if (TextUtils.isEmpty(imgPath)) {
            showToast("Please choose a baby's photo");
            return;
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(birthday)) {
            showToast("Name or birthday cannot be empty");
            return;
        }
        switch (radio_group.getCheckedRadioButtonId()) {
            case R.id.rb_boy:
                Log.d(TAG, "Boy");
                gender = 1;
                break;
            case R.id.rb_girl:
                Log.d(TAG, "Girl");
                gender = 0;
                break;
        }

        if (TextUtils.isEmpty(headCircle) || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight)) {
            showToast("Height, weight and head circumference cannot be empty");
            return;
        }

        showProgress("", "Adding baby...");
        OkHttp.getInstance().asyncUploadImage(Constants.IMAGE_UPLOAD_URL, imgPath, this);
    }

    void toMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.fl_gallery)
    void openGallery() {

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .selectionMode(PictureConfig.SINGLE)
                .enableCrop(true)
                .freeStyleCropEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 动态申请权限的结果回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:

                    List<LocalMedia> mediaList = PictureSelector.obtainMultipleResult(data);
//                    Ts.showShort(this, "media list size:" + mediaList.size());
                    if (mediaList.size() > 0) {
                        imgPath = mediaList.get(0).getCutPath();
                        Glide.with(this).load(imgPath).into(profile_image);
                        iv_add_a_photo.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(Call call) {
        showToast("Network Error" + call.request().url());
    }

    //图片上传成功回调
    @Override
    public void onSuccess(Call call, Response response, String bodyStr) {
        Type type = new TypeToken<ResultWrapper<String>>() {
        }.getType();
        ResultWrapper<String> result = GsonUtil.fromJson(bodyStr, type);

        if (result.getCode() == 200) {
            addBaby(result.getData());
        } else {
            showToast(result.getMsg());
        }
    }

    private void addBaby(String headImgUrl) {
        OkHttp.getInstance()
                .setUrl(Constants.ADD_BABY_URL)
                .addBodyParam("nickName", name)
                .addBodyParam("gender", String.valueOf(gender))
                .addBodyParam("birthday", birthday)
                .addBodyParam("headImgUrl", headImgUrl)
                .addBodyParam("headCircumference", headCircle)
                .addBodyParam("height", height)
                .addBodyParam("weight", weight)
                .asyncPostForm(new OkHttp.NetCallBack() {
                    @Override
                    public void onFailure(Call call) {
                        showToast("Network Error" + call.request().url());
                        dismissProgress();
                    }

                    @Override
                    public void onSuccess(Call call, Response response, String bodyStr) {
                        Type type = new TypeToken<ResultWrapper<Baby>>() {
                        }.getType();
                        ResultWrapper<Baby> result = GsonUtil.fromJson(bodyStr, type);

                        if (result.getCode() == 200) {
                            App.getApp().setBaby(result.getData());
                            toMainActivity();
                            showToast("Added Baby Successfully!");
                        } else {
                            showToast(result.getMsg());
                        }
                        dismissProgress();
                    }
                });
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
}
