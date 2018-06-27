package com.zysdk.vulture.clib.sample.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitleArrs;
    private List<Fragment> mFragments;
    private List<String> mTitles;

    public SampleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    public SampleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String>
            titles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    public SampleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[]
            titles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitleArrs = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null && mTitles.size() > 0 && position < mTitles.size()) {
            return mTitles.get(position);
        }

        if (mTitleArrs != null && mTitleArrs.length > 0 && position < mTitleArrs.length) {
            return mTitleArrs[position];
        }
        return super.getPageTitle(position);
    }
}