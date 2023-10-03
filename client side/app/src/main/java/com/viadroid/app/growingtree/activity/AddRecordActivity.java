package com.viadroid.app.growingtree.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.gson.reflect.TypeToken;
import com.viadroid.app.growingtree.App;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.base.BaseActivity;
import com.viadroid.app.growingtree.entries.ResultWrapper;
import com.viadroid.app.growingtree.util.Constants;
import com.viadroid.app.growingtree.util.DateUtils;
import com.viadroid.app.growingtree.util.GsonUtil;
import com.viadroid.app.growingtree.util.L;
import com.viadroid.app.growingtree.util.OkHttp;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class AddRecordActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_head_circle)
    EditText et_head_circle;
    @BindView(R.id.et_height)
    EditText et_height;
    @BindView(R.id.et_weight)
    EditText et_weight;
    @BindView(R.id.et_date)
    EditText et_date;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_add_record;
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

    @OnClick(R.id.et_date)
    void onDateClick() {
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
    void addRecord() {
        L.d(TAG, "addRecord: " + App.getApp().getBaby());
        if (App.getApp().getBaby() == null) {
            return;
        }

        String bid = App.getApp().getBaby().getId() + "";
        String height = et_height.getText().toString();
        String weight = et_weight.getText().toString();
        String headCircle = et_head_circle.getText().toString();
        String addTime = et_date.getText().toString();

        if (TextUtils.isEmpty(weight) || TextUtils.isEmpty(height) || TextUtils.isEmpty(headCircle)) {
            showToast("Height/weight/head circumference cannot be empty");
            return;
        }

        showProgress("", "Adding record...");

        OkHttp.getInstance()
                .setUrl(Constants.ADD_RECORD_URL)
                .addBodyParam("bid", bid)
                .addBodyParam("headCircumference", headCircle)
                .addBodyParam("height", height)
                .addBodyParam("weight", weight)
                .addBodyParam("addTime", addTime)
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

}
