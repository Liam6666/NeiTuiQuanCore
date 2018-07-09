package com.neituiquan.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.net.HttpFactory;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


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

public class AppService extends Service implements Callback{

    private static final String TAG = "AppService";

    private AppLoop appLoop;

    private Thread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
        String userId = App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
        appLoop = new AppLoop(this,userId);
        thread = new Thread(appLoop);
        thread.start();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG,e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.i(TAG,response.body().string());
    }


    private static class AppLoop implements Runnable{

        private AppService appService;

        private String userId;

        public AppLoop(AppService appService, String userId) {
            this.appService = appService;
            if(StringUtils.isEmpty(userId)){
                this.userId = "";
            }else{
                this.userId = userId;
            }
        }

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(FinalData.LOOP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG,"loop....");
                HttpFactory.getHttpUtils()
                        .get(FinalData.BASE_URL + "/getMsgTaskList?receiveId="+userId)
                        .enqueue(appService);
            }
        }
    }
}
