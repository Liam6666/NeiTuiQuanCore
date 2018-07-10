package com.neituiquan;

import android.app.Application;
import android.content.Intent;

import com.blankj.utilcode.util.Utils;
import com.neituiquan.service.AppService;
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
        APP_INSTANCE = this;
        Utils.init(this);
        userInfoUtils = new UserInfoUtils(this);
        FinalData.FinalDataController.init(this);
        BGASwipeBackHelper.init(this,null);
    }

    public UserInfoUtils getUserInfoUtils() {
        return userInfoUtils;
    }

    public static App getAppInstance() {
        return APP_INSTANCE;
    }



    public static void test(){

    }


}
