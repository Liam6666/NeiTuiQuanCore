package com.neituiquan;

import android.Manifest;

import com.neituiquan.gson.UserResumeModel;

import java.lang.ref.SoftReference;

/**
 * Created by Augustine on 2018/6/15.
 * <p>
 * email:nice_ohoh@163.com
 */

public class FinalData {

    public static final boolean DEBUG = true;
    /**
     * 本地测试路径
     */
    public static final String UAT = "http://10.105.21.138:80";

    /**
     * 服务器路径
     */
    public static final String UDT = "http://193.112.17.87:80";

    public static final String BASE_URL = UDT;

//    public static final String UAT = "http://192.168.0.105:8080";


    public static final String IMG = BASE_URL + "/img?name=";

    /**
     * 所有页面的单页数量
     */
    public static final int PAGE_SIZE = 15;

    public static final long TIME_OUT = 30 * 1000;

    /**
     * 定位缓存超时时间
     */
    public static final long LOCATION_TIME_OUT = 24 * 60 * 60 * 1000;

    public static final int V_CODE_LENGTH = 6;//验证码位数

    /**
     * 所需的动态权限
     */
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.VIBRATE
    };

    public static final String[] CAMERA_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA
    };


    //----------item type----------

    public static final int ITEM_DEFAULT = 0;

    public static final int ITEM_HEADER = 1;

    public static final int ITEM_FOOTER = 2;

    public static final int ITEM_LOAD = 3;

    public static final int ITEM_EMPTY = 4;

    public static final int ITEM_ERROR = 5;

    public static final int ITEM_BANNER = 6;
}
