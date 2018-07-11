package com.neituiquan.work.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.adapter.ChatAdapter;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.database.AppDBFactory;
import com.neituiquan.database.ChatEntity;
import com.neituiquan.database.DBConstants;
import com.neituiquan.database.LocalCacheDAOImpl;
import com.neituiquan.entity.ChatLoopEntity;
import com.neituiquan.entity.UserEntity;
import com.neituiquan.httpEvent.ChatEventModel;
import com.neituiquan.httpEvent.ChatSendEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ChatActivity extends BaseActivity {

    private ImageView chatUI_backImg;
    private TextView chatUI_titleTv;
    private ImageView chatUI_moreImg;
    private SmartRefreshLayout chatUI_refreshLayout;
    private RecyclerView chatUI_recyclerView;
    private EditText chatUI_inputEdit;
    private Button chatUI_sendBtn;
    private View chatUI_statusView;


    private ChatAdapter chatAdapter;

    private LocalCacheDAOImpl localCacheDAO;

    private String groupId;

    private LinearLayoutManager linearLayoutManager;

    private UserEntity userEntity;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();

        groupId = getIntent().getStringExtra("groupId");
        userEntity = App.getAppInstance().getUserInfoUtils().getUserInfo().data;
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        chatUI_recyclerView.setLayoutManager(linearLayoutManager);
        localCacheDAO = AppDBFactory.getInstance(this);
        chatAdapter = new ChatAdapter(this,localCacheDAO.getChatHistory(groupId,"0"));

        chatUI_recyclerView.setAdapter(chatAdapter);

        chatUI_sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = chatUI_inputEdit.getText().toString();

                String url = FinalData.BASE_URL + "/sendChat";

                ChatLoopEntity entity = new ChatLoopEntity();
                entity.setFromId(userEntity.getId());
                entity.setMsgDetails(msg);
                entity.setReceiveId(groupId);

                ChatEntity chatEntity = new ChatEntity();
                chatEntity.setId(System.currentTimeMillis()+"");
                chatEntity.setFromHeadImg(userEntity.getHeadImg());
                chatEntity.setFromId(userEntity.getId());
                chatEntity.setMsgDetails(msg);
                chatEntity.setGroupId(groupId);
                chatEntity.setCreateTime(System.currentTimeMillis()+"");
                chatEntity.setMsgDetails(entity.getMsgDetails());
                chatEntity.setIsRead(DBConstants.YES);
                chatAdapter.addData(chatEntity);
                localCacheDAO.add(chatEntity);
                HttpFactory.getHttpUtils().post(new Gson().toJson(entity),url,new ChatSendEventModel());
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatChanged(ChatEventModel eventModel){
        if(eventModel.groupId.equals(groupId)){
            /**
             * 如果获取的最新消息是当前的聊天窗口
             */
            chatAdapter.refresh(localCacheDAO.getChatHistory(groupId,"0"));
        }
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        chatUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {
        chatUI_statusView = findViewById(R.id.chatUI_statusView);
        chatUI_backImg = (ImageView) findViewById(R.id.chatUI_backImg);
        chatUI_titleTv = (TextView) findViewById(R.id.chatUI_titleTv);
        chatUI_moreImg = (ImageView) findViewById(R.id.chatUI_moreImg);
        chatUI_refreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.chatUI_refreshLayout);
        chatUI_recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.chatUI_recyclerView);
        chatUI_inputEdit = (EditText) findViewById(R.id.chatUI_inputEdit);
        chatUI_sendBtn = (Button) findViewById(R.id.chatUI_sendBtn);
    }
}
