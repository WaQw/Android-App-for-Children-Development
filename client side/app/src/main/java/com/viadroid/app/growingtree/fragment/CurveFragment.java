package com.viadroid.app.growingtree.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;
import com.viadroid.app.growingtree.App;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.activity.AddRecordActivity;
import com.viadroid.app.growingtree.base.BaseFragment;
import com.viadroid.app.growingtree.entries.BabyRecord;
import com.viadroid.app.growingtree.entries.BabyRecordList;
import com.viadroid.app.growingtree.entries.ResultWrapper;
import com.viadroid.app.growingtree.util.ChartUtil;
import com.viadroid.app.growingtree.util.Constants;
import com.viadroid.app.growingtree.util.DateUtils;
import com.viadroid.app.growingtree.util.GsonUtil;
import com.viadroid.app.growingtree.util.L;
import com.viadroid.app.growingtree.util.OkHttp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class CurveFragment extends BaseFragment {

    private static final String TAG = "CurveFragment";

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.tv_chart_title)
    TextView tv_chart_title;
    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    @BindView(R.id.line_chart)
    LineChart chart;

    @BindView(R.id.tv_suggestions)
    TextView tv_suggestions;

    private int mZone = ChartUtil.CHINA;
    private int mGender = 1;
    private int mPosition = 0;
    private BabyRecord mBabyRecord;
    private List<ILineDataSet> mDataSets;
    private List<BabyRecord> mBabyRecords;
    private LineDataSet mBabyDataSet;

    public static CurveFragment newInstance() {
        CurveFragment fragment = new CurveFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_curve;
    }

    @Override
    protected void init() {

        mGender = App.getApp().getBaby().getGender();
        configLineChart();
//        setLimitLine(75);
        setChartData();

        setListener();
    }

    private void configLineChart() {
        //隐藏描述
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);

        //设置Y轴方向是否可缩放
        chart.setScaleYEnabled(false);
        chart.setNoDataText("No data available");

        chart.setPinchZoom(false);

        //图例
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);

        /* 右侧Y轴不显示*/
        YAxis axisRight = chart.getAxisRight();
        axisRight.setEnabled(false);

        XAxis x = chart.getXAxis();
        x.setEnabled(true);
        //设置X轴的位置（默认在上方）
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔
        x.setGranularity(1f);
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value < 1) {
                    return "Born";
                }
                if (value < 12) {
                    return (int) value + "month";
                }

                if (value % 12 == 0) {
                    return (int) (value / 12) + "year";
                }

                return (int) (value / 12) + "year" + (int) (value % 12) + "month";
            }
        });


        YAxis y = chart.getAxisLeft();
        y.setAxisMaximum(220f);
        y.setAxisMinimum(0);
        //y坐标之间的最小值
        y.setGranularity(1);
        y.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) {
                    return "";
                }
                return String.valueOf((int) value);
            }
        });

        chart.setOnDragListener((v, event) -> {
            L.d(TAG, event.getX() + "");
            return false;
        });

    }

    private void setYAxisMaximum(float max) {
        YAxis y = chart.getAxisLeft();
        y.setAxisMaximum(max);
    }

    private void setLimitLine(float limit) {
        LimitLine ll = new LimitLine(limit, getString(R.string.curve_limit_line_table));
        ll.setLineColor(Color.parseColor("#FF7449"));
        ll.setLineWidth(1f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setTextColor(Color.parseColor("#FF5A7D"));
        ll.setTextSize(12f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        chart.getXAxis().addLimitLine(ll);
        chart.invalidate();
    }

    private void setChartData() {

        switch (mPosition) {
            case 0:
                setYAxisMaximum(220f);
                break;
            case 1:
                setYAxisMaximum(90f);
                break;
            case 2:
                setYAxisMaximum(70f);
                break;
            case 3:
                setYAxisMaximum(40f);
                break;
        }

        if (mBabyRecord != null) {
            mBabyDataSet = ChartUtil.getLineDataSet(chart, getEntries(mPosition), "baby",
                    Color.parseColor("#0099cc"));
        }

        mDataSets = ChartUtil.getILineDataSets(getContext(), chart, mZone, mGender, mPosition);
        if (mBabyDataSet != null) {
            mDataSets.add(0, mBabyDataSet);
        }
        LineData lineData = new LineData(mDataSets);
        chart.setData(lineData);
        chart.invalidate();
    }


    @Override
    public void onResume() {
        super.onResume();

        getData();
    }


    private void getData() {
        String bid = App.getApp().getBaby().getId() + "";

        OkHttp.getInstance()
                .addQueryParameter("bid", bid)
                .addQueryParameter("headCircumference", "")
                .asyncGet(Constants.GET_RECORD_LIST_URL, new OkHttp.NetCallBack() {
                    @Override
                    public void onFailure(Call call) {
                        showToast("Network Error" + call.request().url());

                    }

                    @Override
                    public void onSuccess(Call call, Response response, String bodyStr) {
                        Type type = new TypeToken<ResultWrapper<BabyRecordList>>() {
                        }.getType();
                        ResultWrapper<BabyRecordList> result = GsonUtil.fromJson(bodyStr, type);

                        if (result.getCode() == 200 && result.getData().getList().size() > 0) {
                            mBabyRecords = result.getData().getList();

                            //排序
                            Collections.sort(mBabyRecords, new Comparator<BabyRecord>() {
                                @Override
                                public int compare(BabyRecord o1, BabyRecord o2) {
                                    return o1.getAddTime().compareTo(o2.getAddTime());
                                }
                            });

                            L.d(TAG, "onSuccess: " + mBabyRecords.get(mBabyRecords.size() - 1));
                            mBabyRecord = mBabyRecords.get(mBabyRecords.size() - 1);

                            setChartData();
                            setNetData();

                        } else {
                            showToast(result.getMsg());
                        }
                    }
                });
    }

    private void setNetData() {
//        int days = DateUtils.daysBetween(DateUtils.string2Date(App.getApp().getBaby().getBirthday()),
//                DateUtils.string2Date(mBabyRecord.getAddTime()));
        float months = getMonthsBetween(mBabyRecord.getAddTime());
        Log.d(TAG, "setNetData: " + App.getApp().getBaby().getBirthday());
        Log.d(TAG, "setNetData: " + mBabyRecord.getAddTime());
//        Log.d(TAG, "setNetData: " + days + "," + months);

        setLimitLine(months);

//        mDataSets.get(0).getValueTypeface()

        // 通过X坐标值获取Y轴Entries
        List<Entry> entries = chart.getLineData().getDataSetByIndex(0).getEntriesForXValue(months);
        for (Entry entry : entries) {
            Log.d(TAG, "setNetData: " + entry.getY());
        }

        Entry per3Entry = chart.getLineData().getDataSetByLabel("3%", true)
                .getEntryForXValue(months, 1);
        Entry per15Entry = chart.getLineData().getDataSetByLabel("15%", true)
                .getEntryForXValue(months, 1);
        Entry per50Entry = chart.getLineData().getDataSetByLabel("50%", true)
                .getEntryForXValue(months, 1);
        Entry per85Entry = chart.getLineData().getDataSetByLabel("85%", true)
                .getEntryForXValue(months, 1);
        Entry per97Entry = chart.getLineData().getDataSetByLabel("97%", true)
                .getEntryForXValue(months, 1);

        Log.d(TAG, "setNetData: entry" + per3Entry.toString());
        Log.d(TAG, "setNetData: entry" + per15Entry.toString());
        Log.d(TAG, "setNetData: entry" + per50Entry.toString());
        Log.d(TAG, "setNetData: entry" + per85Entry.toString());
        Log.d(TAG, "setNetData: entry" + per97Entry.toString());

        String curValueStr = mBabyRecord.getHeight();

        String[] stringArray = getResources().getStringArray(R.array.suggestion_height);
        switch (mPosition) {
            case 0:
                curValueStr = mBabyRecord.getHeight();
                stringArray = getResources().getStringArray(R.array.suggestion_height);
                break;
            case 1:
                curValueStr = mBabyRecord.getWeight();
                stringArray = getResources().getStringArray(R.array.suggestion_weight);
                break;
            case 2:
                curValueStr = mBabyRecord.getHeadCircumference();
                stringArray = getResources().getStringArray(R.array.suggestion_Head_circle);
                break;
            case 3:
                curValueStr = mBabyRecord.getBmi();
                stringArray = getResources().getStringArray(R.array.suggestion_bmi);
                break;
        }

        //* 身高【过低<3% 偏低3%-15% 正常15%-85% 偏高85%-97% 过高>97%】
        //* 体重【过轻<3% 偏轻3%-15% 正常15%-85% 偏重85%-97% 过重>97%】
        //* 头围【过小<3% 偏小3%-15% 正常15%-85% 偏大85%-97% 过大>97%】
        //* BMI【超重>85%】
        try {
            float curVal = Float.parseFloat(curValueStr);
            String suggestion;
            if (curVal < per3Entry.getY()) {// 小于3%
                suggestion = stringArray[0];
            } else if (curVal < per15Entry.getY()) {// 3% ~ 15%
                suggestion = stringArray[1];
            } else if (curVal < per50Entry.getY()) {// 15% ~ 50%
                suggestion = stringArray[2];
            } else if (curVal < per85Entry.getY()) {// 50% ~ 85%
                suggestion = stringArray[2];
            } else if (curVal < per97Entry.getY()) {// 85% ~ 97%
                suggestion = stringArray[3];
            } else {// 大于 97%
                suggestion = stringArray[4];
            }

            tv_suggestions.setText(suggestion);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private List<Entry> getEntries(int position) {
        List<Entry> entries = new ArrayList<>();

        for (BabyRecord record : mBabyRecords) {
            String yStr;
            switch (position) {
                case 0:
                    yStr = record.getHeight();
                    break;
                case 1:
                    yStr = record.getWeight();
                    break;
                case 2:
                    yStr = record.getHeadCircumference();
                    break;
                default:
                    yStr = record.getBmi();
                    break;
            }

            float x = getMonthsBetween(record.getAddTime());
            float y = Float.parseFloat(yStr);
            entries.add(new Entry(x, y));
        }
        return entries;
    }

    private float getMonthsBetween(String date) {
        int days = DateUtils.daysBetween(DateUtils.string2Date(App.getApp().getBaby().getBirthday(),
                DateUtils.PATTEN_YMDSF),
                DateUtils.string2Date(date, DateUtils.PATTEN_YMDSF));
        return days / 30.0f;
    }


    private void setListener() {
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                L.d(TAG, "onTabSelected: " + tab.getPosition());
                mPosition = tab.getPosition();
                String suffix = "(cm)";
                if (mPosition == 3) {
                    suffix = "(%)";
                }
                tv_chart_title.setText(tab.getText() + suffix);

                setChartData();
                setNetData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            L.d(TAG, "onCheckedChanged: " + checkedId);
            switch (checkedId) {
                case R.id.rb_china:
                    L.d(TAG, "onCheckedChanged: rb_china " + checkedId);
                    mZone = ChartUtil.CHINA;
                    break;
                case R.id.rb_who:
                    L.d(TAG, "onCheckedChanged: rb_who " + checkedId);
                    mZone = ChartUtil.INTERNATIONAL;
                    break;
            }
            setChartData();
            setNetData();
        });
    }

    @OnClick(R.id.add)
    void addRecord() {
        startActivity(AddRecordActivity.class);
    }
}
