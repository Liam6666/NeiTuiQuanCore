package com.neituiquan.utils;

import java.util.UUID;

/**
 * Created by Augustine on 2018/7/11.
 * <p>
 * email:nice_ohoh@163.com
 */

public class DataBaseUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String getCurrentTimeMillis(){
        return String.valueOf(System.currentTimeMillis());
    }
}
