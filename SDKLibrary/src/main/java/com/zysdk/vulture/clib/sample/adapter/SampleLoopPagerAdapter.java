package com.zysdk.vulture.clib.sample.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SampleLoopPagerAdapter extends PagerAdapter {

    private final List<View> mViews;

    public SampleLoopPagerAdapter(List<View> views) {
        this.mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size() == 1 ? 1 : mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        position %= mViews.size();
        if (position < 0) {
            position = mViews.size() + position;
        }
        View view = mViews.get(position);
        if (view.getParent() != null) {
            if (view.getParent() instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view.getParent();
                group.removeView(view);
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // TODO: 2018/6/27  这里不能有任何实现，否则会出现崩溃的情况
    }

}