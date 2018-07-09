package com.neituiquan.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.database.AppDBFactory;
import com.neituiquan.database.ChatEntity;
import com.neituiquan.database.DBConstants;
import com.neituiquan.database.LocalCacheDAOImpl;
import com.neituiquan.entity.MsgTaskEntity;
import com.neituiquan.gson.MsgTaskModel;
import com.neituiquan.httpEvent.ChatEventModel;
import com.neituiquan.net.HttpFactory;

import org.greenrobot.eventbus.EventBus;

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

public class AppService extends Service{

    private static final String TAG = "AppService";

    private AppLoop appLoop;

    private Thread thread;

    private LocalCacheDAOImpl localCacheDAO;

    private LoopHandler loopHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
        loopHandler = new LoopHandler();
        localCacheDAO = AppDBFactory.getInstance(this);
        String userId = "";
        if(App.getAppInstance().getUserInfoUtils().getUserInfo() != null){
            userId = App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
        }
        appLoop = new AppLoop(this,userId);
        thread = new Thread(appLoop);
        thread.start();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy");
        super.onDestroy();
    }


    private void put2db(String response){
        MsgTaskModel model = new Gson().fromJson(response,MsgTaskModel.class);
        for(MsgTaskEntity entity : model.data){
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setId(entity.getId());
            chatEntity.setGroupId(entity.getFromId());
            chatEntity.setFromId(entity.getFromId());
            chatEntity.setFromNickName(entity.getFromNickName());
            chatEntity.setFromHeadImg(entity.getFromHeadImg());
            chatEntity.setCreateTime(entity.getCreateTime());
            chatEntity.setMsgDetails(entity.getMsgDetails());
            chatEntity.setIsRead(DBConstants.NO);
            localCacheDAO.add(chatEntity);
            /**
             * 通知MessageFragment 接受到新消息
             */
            EventBus.getDefault().post(new ChatEventModel(chatEntity.getGroupId()));
            String url = FinalData.BASE_URL + "/delMsgTaskAdd2History?id="+entity.getId();
                HttpFactory.getHttpUtils().get(url).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                    }
                });
        }
    }


    private static class AppLoop implements Runnable{

        private AppService appService;

        private String userId;

        private long loopFlag = -1;

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
                if(appService != null){
                    HttpFactory.getHttpUtils()
                            .get(FinalData.BASE_URL + "/getMsgTaskList?receiveId="+userId)
                            .enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    appService.put2db(response.body().string());
                                }
                            });
                }
            }
        }
    }

    static class LoopHandler extends Handler{

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }

    }
}
