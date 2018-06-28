package com.neituiquan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.neituiquan.work.R;

import java.util.List;

/**
 * Created by Augustine on 2018/6/15.
 * <p>
 * email:nice_ohoh@163.com
 */

public class BasePageAdapter extends PagerAdapter {

    private Context context;

    private List<View> viewList;

    public BasePageAdapter(Context context, List<View> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }
}
