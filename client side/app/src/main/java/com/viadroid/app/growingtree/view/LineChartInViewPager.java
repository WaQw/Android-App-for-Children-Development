package com.viadroid.app.growingtree.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.viadroid.app.growingtree.util.L;

public class LineChartInViewPager extends LineChart {
    private static final String TAG = "LineChartInViewPager";

    PointF downPoint = new PointF();

    public LineChartInViewPager(Context context) {
        super(context);
    }

    public LineChartInViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartInViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);// 用getParent去请求,
//        // 不拦截
//        return super.dispatchTouchEvent(ev);
//    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint.x = evt.getX();
                downPoint.y = evt.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i("getScrollX ", getScrollX() + "");
                L.d(TAG, getLowestVisibleX() + "," + getHighestVisibleX() + "," + getXChartMin() + "," + getXChartMax());
//                if (getScaleX() > 1 && Math.abs(evt.getX() - downPoint.x) > 5) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }

                L.d(TAG, "x:" + evt.getX() + ":" + downPoint.x);

                boolean isDragLeft = evt.getX() - downPoint.x < 0;

                if (!isDragLeft && getLowestVisibleX() > getXChartMin()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                if (isDragLeft && getHighestVisibleX() < getXChartMax()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
        }
        return super.onTouchEvent(evt);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        L.d(TAG, "l:" + l);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}