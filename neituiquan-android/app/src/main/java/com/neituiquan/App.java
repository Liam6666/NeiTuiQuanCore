package com.neituiquan;

import android.app.Application;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.Utils;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by Augustine on 2018/6/15.
 * <p>
 * email:nice_ohoh@163.com
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        BGASwipeBackHelper.init(this,null);
    }
}
