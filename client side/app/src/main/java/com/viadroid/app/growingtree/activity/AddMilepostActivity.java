package com.viadroid.app.growingtree.activity;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.viadroid.app.growingtree.entries.ResultWrapper;
import com.viadroid.app.growingtree.util.Constants;
import com.viadroid.app.growingtree.util.DateUtils;
import com.viadroid.app.growingtree.util.GlideEngine;
import com.viadroid.app.growingtree.util.GsonUtil;
import com.viadroid.app.growingtree.util.L;
import com.viadroid.app.growingtree.util.OkHttp;
import com.viadroid.app.growingtree.util.Ts;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class AddMilepostActivity extends BaseActivity implements OkHttp.NetCallBack {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.profile_image)
    ImageView profile_image;
    @BindView(R.id.iv_add_a_photo)
    ImageView iv_add_a_photo;
    @BindView(R.id.sp_events)
    AppCompatSpinner sp_events;
    @BindView(R.id.et_date)
    EditText et_date;
    @BindView(R.id.et_des)
    EditText et_des;

    private String imgPath;
    private String title;
    private String createdTime;
    private String des;


    @Override
    public int getLayoutResID() {
        return R.layout.activity_add_milepost;
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        et_date.setText(DateUtils.getNowString(DateUtils.PATTEN_YMDSF));
    }

    @Override
    public void setListener() {

    }

    @Override
    public void getData() {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:

                    List<LocalMedia> mediaList = PictureSelector.obtainMultipleResult(data);
                    Ts.showShort(this, "media list size:" + mediaList.size());
                    if (mediaList.size() > 0) {
                        imgPath = mediaList.get(0).getCutPath();
                        Glide.with(this).load(mediaList.get(0).getCutPath()).into(profile_image);
                        iv_add_a_photo.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.et_date)
    void pickDate() {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String format = dateFormat.format(calendar.getTime());
                et_date.setText(format);

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Log.d(TAG, "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);

                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @OnClick(R.id.btn_save)
    void onSave() {
        title = sp_events.getSelectedItem().toString();
        createdTime = et_date.getText().toString();
        des = et_des.getText().toString();


        if (TextUtils.isEmpty(imgPath)) {
            showToast("Please add a picture");
            return;
        }

        if (TextUtils.isEmpty(des)) {
            showToast("Say something...");
            return;
        }


        showProgress("", "Adding milestone...");
        OkHttp.getInstance().asyncUploadImage(Constants.IMAGE_UPLOAD_URL, imgPath, this);
    }

    @Override
    public void onFailure(Call call) {
        showToast("Network Error" + call.request().url());
    }

    @Override
    public void onSuccess(Call call, Response response, String bodyStr) {
        Type type = new TypeToken<ResultWrapper<String>>() {
        }.getType();
        ResultWrapper<String> result = GsonUtil.fromJson(bodyStr, type);

        if (result.getCode() == 200) {
            addMilestone(result.getData());
        } else {
            showToast(result.getMsg());
        }
    }

    void addMilestone(String imgUrl) {
        L.d(TAG, "addRecord: " + App.getApp().getBaby());
        if (App.getApp().getBaby() == null) {
            return;
        }

        String bid = App.getApp().getBaby().getId() + "";

        String assessment = "";

        int type = getTypeFromTitle(title);

        OkHttp.getInstance()
                .setUrl(Constants.ADD_MILESTONE_URL)
                .addBodyParam("bid", bid)
                .addBodyParam("type", String.valueOf(type))
                .addBodyParam("headImgUrl", imgUrl)
                .addBodyParam("title", title)
                .addBodyParam("createdTime", createdTime)
                .addBodyParam("assessment", assessment)
                .addBodyParam("des", des)
                .asyncPostForm(new OkHttp.NetCallBack() {
                    @Override
                    public void onFailure(Call call) {
                        showToast("Network Error" + call.request().url());
                        dismissProgress();
                    }

                    @Override
                    public void onSuccess(Call call, Response response, String bodyStr) {
                        Type type = new TypeToken<ResultWrapper>() {
                        }.getType();
                        ResultWrapper result = GsonUtil.fromJson(bodyStr, type);
                        showToast(result.getMsg());
                        finish();

                        dismissProgress();
                    }
                });
    }

    public Integer getTypeFromTitle(String title) {
        String[] firstEvents = getResources().getStringArray(R.array.first_events);
        HashMap<String, Integer> titleTypeMap = new HashMap<>();
        for (int i = 0; i < firstEvents.length; i++) {
            titleTypeMap.put(firstEvents[i], i);
        }

        return titleTypeMap.get(title);
    }


}
