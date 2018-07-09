package com.neituiquan.database;

import android.content.Context;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class AppDBFactory {

    private static LocalCacheDAOImpl appDataBase;

    public static LocalCacheDAOImpl getInstance(Context context){
        if(appDataBase == null){
            appDataBase = new LocalCacheDAOImpl(context);
        }
        return appDataBase;
    }
}
