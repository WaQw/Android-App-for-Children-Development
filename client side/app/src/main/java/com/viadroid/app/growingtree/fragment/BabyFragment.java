package com.viadroid.app.growingtree.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.viadroid.app.growingtree.App;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.base.BaseFragment;
import com.viadroid.app.growingtree.entries.Baby;
import com.viadroid.app.growingtree.entries.BabyRecord;
import com.viadroid.app.growingtree.entries.ResultWrapper;
import com.viadroid.app.growingtree.util.Constants;
import com.viadroid.app.growingtree.util.DateUtils;
import com.viadroid.app.growingtree.util.GsonUtil;
import com.viadroid.app.growingtree.util.OkHttp;
import com.viadroid.app.growingtree.util.L;

import java.lang.reflect.Type;
import java.util.Date;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

public class BabyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.profile_image)
    ImageView profile_image;
    @BindView(R.id.tv_nick_name)
    TextView tv_nick_name;
    @BindView(R.id.tv_age)
    TextView tv_age;
    @BindView(R.id.tv_chinese_zodiac)
    TextView tv_chinese_zodiac;
    @BindView(R.id.tv_zodiac)
    TextView tv_zodiac;
    @BindView(R.id.tv_height)
    TextView tv_height;
    @BindView(R.id.tv_weight)
    TextView tv_weight;
    @BindView(R.id.tv_head_circle)
    TextView tv_head_circle;


    public static BabyFragment newInstance() {
        BabyFragment fragment = new BabyFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baby;
    }

    @Override
    protected void init() {
        mRefreshLayout.setOnRefreshListener(this);
        getBabyData();
    }


    @Override
    public void onRefresh() {
        getBabyData();
    }

    private void getBabyData() {
        OkHttp.getInstance().asyncGet(Constants.GET_BABY_URL, new OkHttp.NetCallBack() {
            @Override
            public void onFailure(Call call) {
                showToast("Network Error" + call.request().url());
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(Call call, Response response, String bodyStr) {
                Type type = new TypeToken<ResultWrapper<Baby>>() {
                }.getType();
                ResultWrapper<Baby> result = GsonUtil.fromJson(bodyStr, type);

                if (result.getCode() == 200) {
                    App.getApp().setBaby(result.getData());
                    setView(result.getData());
                } else {
                    showToast(result.getMsg());
                }
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setView(Baby baby) {
        Log.d(TAG, "headImgUrl:" + baby.getHeadImgUrl());
        Glide.with(this).load(baby.getHeadImgUrl()).into(profile_image);

        tv_nick_name.setText(" " + baby.getNickName());


        int days = DateUtils.daysBetween(DateUtils.string2Date(baby.getBirthday(), DateUtils.PATTEN_YMDSF), new Date());
        //String ageStr = "Age：" + days + "day";
        String ageStr = " ";

        int year = days / 365;
        int month = days % 365 / 30;
        int day = days % 365 % 30;

        L.d(TAG, "setView: " + year + "," + month + "," + day);

        if (year > 0) {
            ageStr += " " + year + "year";
        }
        if (month > 0) {
            ageStr += " " + month + "month";
        }
        ageStr += " " + day + "day";
        tv_age.setText(ageStr);

        String chineseZodiac = "Chinese Zodiac：" + DateUtils.getChineseZodiac(baby.getBirthday(), DateUtils.PATTEN_YMDSF);
        String zodiac = "Constellation：" + DateUtils.getZodiac(baby.getBirthday(), DateUtils.PATTEN_YMDSF);
        tv_chinese_zodiac.setText(chineseZodiac);
        tv_zodiac.setText(zodiac);

        BabyRecord babyRecord = baby.getBabyRecord();
        tv_height.setText(babyRecord.getHeight() + "cm");
        tv_weight.setText(babyRecord.getWeight() + "kg");
        tv_head_circle.setText(babyRecord.getHeadCircumference() + "cm");
    }
}
