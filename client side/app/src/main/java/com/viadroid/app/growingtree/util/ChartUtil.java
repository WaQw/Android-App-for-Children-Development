package com.viadroid.app.growingtree.util;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class ChartUtil {

    private static final String TAG = ChartUtil.class.getSimpleName();

    public static final int CHINA = 0;
    public static final int INTERNATIONAL = 1;


    public static List<ILineDataSet> getILineDataSets(Context context, Chart chart, int zone, int gender, int type) {
        ArrayList<ILineDataSet> sets = new ArrayList<>();
//        sets.add(getLineDataSet(GrowthBean.boyHeightPer3, "3%", Color.parseColor("#EF9A9A")));//#FF7449
//        sets.add(getLineDataSet(GrowthBean.boyHeightPer10, "10%", Color.parseColor("#CE93D8")));//#FD5A7B
//        sets.add(getLineDataSet(GrowthBean.boyHeightPer50, "50%", Color.parseColor("#9FA8DA")));//#FFC9C3
//        sets.add(getLineDataSet(GrowthBean.boyHeightPer90, "90%", Color.parseColor("#80DEEA")));//#FF0000
//        sets.add(getLineDataSet(GrowthBean.boyHeightPer97, "97%", Color.parseColor("#C5E1A5")));//#00FF00

        int[] pers = {3, 10, 50, 90, 97};

        if (type == 3 && zone == CHINA) {
            pers[1] = 15;
            pers[3] = 85;
        }

        String dataPath = getDataPath(zone, gender, type);


        sets.add(getLineDataSet(chart, getEntries(context, dataPath + pers[0] + ".txt"), "3%", Color.parseColor("#EF9A9A")));//#FF7449
        sets.add(getLineDataSet(chart, getEntries(context, dataPath + pers[1] + ".txt"), "15%", Color.parseColor("#CE93D8")));//#FD5A7B
        sets.add(getLineDataSet(chart, getEntries(context, dataPath + pers[2] + ".txt"), "50%", Color.parseColor("#9FA8DA")));//#FFC9C3
        sets.add(getLineDataSet(chart, getEntries(context, dataPath + pers[3] + ".txt"), "85%", Color.parseColor("#80DEEA")));//#FF0000
        sets.add(getLineDataSet(chart, getEntries(context, dataPath + pers[4] + ".txt"), "97%", Color.parseColor("#C5E1A5")));//#00FF00
        return sets;
    }

    public static LineDataSet getLineDataSet(Chart chart, List<Entry> entries, String label, int lineColor) {
        LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setColor(lineColor);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(true);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= chart.getXChartMax()) {

                    return label;
                }

                return "";
            }
        });

        return lineDataSet;
    }

    public static List<Entry> getEntries(float[][] data) {
        List<Entry> entries = new ArrayList<>();
        for (float[] it : data) {
            entries.add(new Entry(it[0], it[1]));
        }
        return entries;
    }

    public static List<Entry> getEntries(Context context, String path) {
        return FileUtils.loadEntriesFromAssets(context.getAssets(), path);
    }

    public static String getDataPath(int zone, int gender, int type) {
        String name = "";

        if (zone == INTERNATIONAL) {
            name += "who_";
        }

        if (gender == 1) {
            name += "boy";
        } else {
            name += "girl";
        }

        if (type == 0)
            name += "heightPer";
        else if (type == 1)
            name += "weightPer";
        else if (type == 2)
            name += "headcirPer";
        else if (type == 3)
            name += "bmiPer";

        L.d(TAG, "getDataPath: " + name);

        return name;
    }

}
