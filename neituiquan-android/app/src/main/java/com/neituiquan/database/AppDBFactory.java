package com.neituiquan.database;

import android.content.Context;

/**
 * Created by Augustine on 2018/7/12.
 * <p>
 * email:nice_ohoh@163.com
 */

public class AppDBFactory {

    private static AppDBUtils appDBUtils;

    public static AppDBUtils getInstance(Context context){
        if(appDBUtils == null){
            appDBUtils = new AppDBUtils(context);
        }
        return appDBUtils;
    }
}
