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

    public static final String UAT = "http://10.105.21.138:8080";

    public static final String UDT = "http://193.112.17.87:8080";

    public static final String BASE_URL = UAT;

//    public static final String UAT = "http://192.168.0.105:8080";


    public static final String IMG = BASE_URL + "/img?name=";

    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };


}
