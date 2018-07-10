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
import com.neituiquan.entity.ChatLoopEntity;
import com.neituiquan.gson.MsgLoopModel;
import com.neituiquan.httpEvent.ChatEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.TaskLooper;

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

    private LocalCacheDAOImpl localCacheDAO;

    private AppLoop appLoop;

    private TaskLooper.TaskCompleteCallback completeCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        localCacheDAO = AppDBFactory.getInstance(this);
        if(App.getAppInstance().getUserInfoUtils().getUserInfo() != null){
            String userId = App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
            appLoop = new AppLoop(this,userId);
            TaskLooper.bindTask(appLoop,FinalData.LOOP);
        }
    }

    @Override
    public void onDestroy() {
        TaskLooper.unBind(appLoop);
        super.onDestroy();
    }


    public void setCompleteCallback(TaskLooper.TaskCompleteCallback completeCallback) {
        this.completeCallback = completeCallback;
    }

    /**
     * 将获取到的消息保存在本地数据库
     * 并通知刷新UI
     * this not a UI thread
     *
     */
    private void put2db(String response){
        MsgLoopModel model = new Gson().fromJson(response,MsgLoopModel.class);
        for(ChatLoopEntity entity : model.data){
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setId(entity.getId());
            chatEntity.setGroupId(entity.getFromId());
            chatEntity.setFromId(entity.getFromId());
            chatEntity.setCreateTime(entity.getCreateTime());
            chatEntity.setMsgDetails(entity.getMsgDetails());
            chatEntity.setIsRead(DBConstants.NO);
            /**
             * 保存消息到本地数据库
             */
            localCacheDAO.add(chatEntity);
            /**
             * 通知MessageFragment 接受到新消息
             */
            EventBus.getDefault().post(new ChatEventModel(chatEntity.getGroupId()));
            /**
             * 通知服务器已接收到消息
             */
            String url = FinalData.BASE_URL + "/updateChatState?id="+entity.getId();
            HttpFactory.getHttpUtils().getNoCall(url);
        }
        /**
         * 完成
         */
        completeCallback.onComplete();
    }


    private static class AppLoop implements TaskLooper.Task{

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
        public void run(final TaskLooper.TaskCompleteCallback callback) {
            if(appService != null){
                this.appService.setCompleteCallback(callback);
                loopChat();
                if (FinalData.DEBUG){
                    Log.i(TAG,"loop....");
                }
            }
        }

        private void loopChat(){
            HttpFactory.getHttpUtils()
                    .get(FinalData.BASE_URL + "/getChatList?receiveId="+userId)
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
