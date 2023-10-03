package com.viadroid.app.growingtree.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.viadroid.app.growingtree.base.BaseFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private List<String> mTitles;
    private List<BaseFragment> mFragments;

    public FragPagerAdapter(FragmentManager fm, Context context,
                            List<String> titles, List<BaseFragment> fragments) {
        super(fm);
        mContext = context;
        mTitles = titles;
        mFragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) super.instantiateItem(container, position);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "", e);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position % mTitles.size());
    }

//    public View getCustomTabView(int position) {
//        View view = LayoutInflater.from(mContext).inflate(
//                R.layout.item_custome_tab_layout, null);
//
//        ImageView imageView = (ImageView) view.findViewById(R.id.img);
//        TextView textView = (TextView) view.findViewById(R.id.text);
//
//        imageView.setImageResource(R.drawable.ic_launcher);
//        textView.setText(getPageTitle(position));
//        return view;
//    }

}
