package com.viadroid.app.growingtree.activity;

import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.adapter.FragPagerAdapter;
import com.viadroid.app.growingtree.base.BaseActivity;
import com.viadroid.app.growingtree.base.BaseFragment;
import com.viadroid.app.growingtree.fragment.BabyFragment;
import com.viadroid.app.growingtree.fragment.CurveFragment;
import com.viadroid.app.growingtree.fragment.MilepostFragment;
import com.viadroid.app.growingtree.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.fl_content)
    FrameLayout fl_content;
    @BindView(R.id.view_page)
    ViewPager mViewPager;

    private FragPagerAdapter mFragPagerAdapter;

    private List<BaseFragment> mFragments;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mFragments = new ArrayList<>();

        mFragments.add(BabyFragment.newInstance());
        mFragments.add(CurveFragment.newInstance());
//        mFragments.add(RecordFragment.newInstance());
        mFragments.add(MilepostFragment.newInstance());
        mFragments.add(MineFragment.newInstance());

        mFragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager(), this, null, mFragments);
        mViewPager.setAdapter(mFragPagerAdapter);
        mViewPager.setCurrentItem(0);
        mBottomNavigationView.setSelectedItemId(R.id.nav_baby);
    }

    @Override
    public void setListener() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.setSelectedItemId(R.id.nav_baby);
                        break;
                    case 1:
                        mBottomNavigationView.setSelectedItemId(R.id.nav_curve);
                        break;
                    case 2:
//                        mBottomNavigationView.setSelectedItemId(R.id.nav_record);
                        mBottomNavigationView.setSelectedItemId(R.id.nav_milepost);
                        break;
                    case 3:
//                        mBottomNavigationView.setSelectedItemId(R.id.nav_milepost);
                        mBottomNavigationView.setSelectedItemId(R.id.nav_mine);
                        break;
                    case 4:
//                        mBottomNavigationView.setSelectedItemId(R.id.nav_mine);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_baby:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.nav_curve:
                    mViewPager.setCurrentItem(1);
                    break;
//                case R.id.nav_record:
//                    mViewPager.setCurrentItem(2);
//                    break;
                case R.id.nav_milepost:
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.nav_mine:
                    mViewPager.setCurrentItem(3);
                    break;
            }
            return true;
        });
    }

    @Override
    public void getData() {

    }

}
