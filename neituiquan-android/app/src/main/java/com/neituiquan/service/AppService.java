package com.neituiquan.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/7/4.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 1.监听推送
 * 2.监听消息
 * 3.位置信息
 *
 */

public class AppService extends Service {

    private static final String TAG = "AppService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG,"loop....");
                }
            }
        }).start();


    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy");
        super.onDestroy();
    }
}
