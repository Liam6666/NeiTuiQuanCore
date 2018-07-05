package com.neituiquan;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.neituiquan.utils.UserInfoUtils;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by Augustine on 2018/6/15.
 * <p>
 * email:nice_ohoh@163.com
 */

public class App extends Application {

    private UserInfoUtils userInfoUtils;

    private static App APP_INSTANCE = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        BGASwipeBackHelper.init(this,null);
        APP_INSTANCE = this;
        userInfoUtils = new UserInfoUtils(this);
//        startService(new Intent(getApplicationContext(), AppService.class));

    }

    public UserInfoUtils getUserInfoUtils() {
        return userInfoUtils;
    }

    public static App getAppInstance() {
        return APP_INSTANCE;
    }
}
