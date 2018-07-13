package com.neituiquan.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.database.AppDBFactory;
import com.neituiquan.database.AppDBUtils;
import com.neituiquan.database.ChatDBEntity;
import com.neituiquan.database.DBConstants;
import com.neituiquan.entity.ChatLoopEntity;
import com.neituiquan.gson.MsgLoopModel;
import com.neituiquan.httpEvent.ChatEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.ChatNotifyUtils;
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

    private AppDBUtils dbUtils;

    private AppLoop appLoop;

    private String userAccount;

    private TaskLooper.TaskCompleteCallback completeCallback;

    private AppHandler appHandler;

    private static final int NOTIFY = 2332;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        appHandler = new AppHandler(this);
        if(App.getAppInstance().getUserInfoUtils().getUserInfo() != null){
            dbUtils = AppDBFactory.getInstance(this);
            String userId = App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
            userAccount = App.getAppInstance().getUserInfoUtils().getUserInfo().data.getAccount();
            appLoop = new AppLoop(this,userId);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    TaskLooper.bindTask(appLoop,FinalData.LOOP);
                }
            }, FinalData.LOOP);
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

            ChatDBEntity dbEntity = new ChatDBEntity();
            dbEntity.setChatId(entity.getId());
            dbEntity.setFromId(entity.getFromId());
            dbEntity.setFromNickName(entity.getFromNickName());
            dbEntity.setFromHeadImg(entity.getFromHeadImg());
            dbEntity.setReceiveId(entity.getReceiveId());
            dbEntity.setReceiveNickName(entity.getReceiveNickName());
            dbEntity.setReceiveHeadImg(entity.getReceiveHeadImg());
            dbEntity.setMsgDetails(entity.getMsgDetails());
            dbEntity.setMsgType(entity.getMsgType());
            dbEntity.setAccount(userAccount);
            dbEntity.setCreateTime(entity.getCreateTime());
            dbEntity.setIsFrom(DBConstants.NO);
            /**
             * 保存消息到本地数据库
             */
            dbUtils.addChat(dbEntity);
            /**
             * 通知MessageFragment 接受到新消息
             */
            EventBus.getDefault().post(new ChatEventModel(dbEntity));
            /**
             * 通知服务器已接收到消息
             */
            String url = FinalData.BASE_URL + "/updateChatState?id="+entity.getId();
            HttpFactory.getHttpUtils().getNoCall(url);

            if(FinalData.IS_OPEN_NOTIFY){
                Message msg = Message.obtain();
                msg.what = NOTIFY;
                Bundle bundle = new Bundle();
                bundle.putString("msg_",entity.getMsgDetails());
                bundle.putString("fromNickName",entity.getFromNickName());
                bundle.putString("fromHeadImg",entity.getFromHeadImg());
                msg.setData(bundle);
                appHandler.sendMessage(msg);
            }
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

    private static class AppHandler extends Handler{

        private AppService service;

        public AppHandler(AppService service){
            this.service = service;
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(service == null){
                return;
            }
            switch (msg.what){
                case NOTIFY:
                    Bundle bundle = msg.getData();
                    String msg_ = bundle.getString("msg_");
                    String fromNickName = bundle.getString("fromNickName");
                    String fromHeadImg = bundle.getString("fromHeadImg");
                    ChatNotifyUtils.chatNotify(service,msg_,fromNickName,fromHeadImg);
                    return;
            }
        }
    }

}
