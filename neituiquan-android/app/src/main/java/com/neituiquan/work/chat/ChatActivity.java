package com.neituiquan.work.chat;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.adapter.ChatAdapter;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.database.AppDBFactory;
import com.neituiquan.database.AppDBUtils;
import com.neituiquan.database.ChatDBEntity;
import com.neituiquan.database.DBConstants;
import com.neituiquan.entity.ChatLoopEntity;
import com.neituiquan.entity.UserEntity;
import com.neituiquan.httpEvent.ChatEventModel;
import com.neituiquan.httpEvent.SendChatEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.UUID;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ChatActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener {

    private View chatUI_statusView;
    private ImageView chatUI_backImg;
    private TextView chatUI_titleTv;
    private ImageView chatUI_moreImg;
    private SmartRefreshLayout chatUI_refreshLayout;
    private RecyclerView chatUI_recyclerView;
    private EditText chatUI_inputTv;
    private View chatUI_inputBottomView;
    private TextView chatUI_sendTv;

    private LinearLayoutManager linearLayoutManager;

    private AppDBUtils dbUtils;

    private String otherSideId;//对方ID

    private String osNickName;

    private String osHeadImg;

    private ChatAdapter chatAdapter;

    private UserEntity userEntity;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        otherSideId = getIntent().getStringExtra("otherSideId");
        osNickName = getIntent().getStringExtra("osNickName");
        osHeadImg = getIntent().getStringExtra("osHeadImg");
        dbUtils = AppDBFactory.getInstance(this);
        linearLayoutManager = new LinearLayoutManager(this);
        chatUI_recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(this);
        chatUI_recyclerView.setAdapter(chatAdapter);
        userEntity = App.getAppInstance().getUserInfoUtils().getUserInfo().data;
        List<ChatDBEntity> list = dbUtils.getChatList(otherSideId);
        chatAdapter.refresh(list);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newChatNotify(ChatEventModel eventModel){
        if(eventModel.groupId.equals(otherSideId)){
            List<ChatDBEntity> list = dbUtils.getChatList(otherSideId);
            chatAdapter.refresh(list);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.chatUI_sendTv:
                sendMessage();
                break;
        }
    }

    private void sendMessage(){
        String msg = chatUI_inputTv.getText().toString();
        /**
         * 发送给服务器的消息
         */
        ChatLoopEntity entity = new ChatLoopEntity();
        entity.setId(UUID.randomUUID().toString().replace("-",""));
        entity.setGroupId(otherSideId);
        entity.setFromId(userEntity.getId());
        entity.setFromHeadImg(userEntity.getHeadImg());
        entity.setFromNickName(userEntity.getNickName());
        entity.setReceiveId(otherSideId);
        entity.setReceiveHeadImg(osNickName);
        entity.setReceiveNickName(osHeadImg);
        entity.setMsgDetails(msg);
        entity.setMsgType(DBConstants.MSG_TYPE_CHAT);
        entity.setAccount(userEntity.getAccount());
        entity.setIsFrom(DBConstants.YES);
        entity.setCreateTime(System.currentTimeMillis()+"");
        String json = new Gson().toJson(entity);
        String url = FinalData.BASE_URL + "/sendChat";
        //开始发送
        HttpFactory.getHttpUtils().post(json,url,new SendChatEventModel());

        /**
         * 保存到本地表的信息
         */

        ChatDBEntity chatDBEntity = new ChatDBEntity();
        chatDBEntity.setChatId(entity.getId());
        chatDBEntity.setGroupId(otherSideId);
        chatDBEntity.setFromId(entity.getFromId());
        chatDBEntity.setFromNickName(entity.getFromNickName());
        chatDBEntity.setFromHeadImg(entity.getFromHeadImg());
        chatDBEntity.setReceiveId(otherSideId);
        chatDBEntity.setReceiveNickName(osNickName);
        chatDBEntity.setReceiveHeadImg(osHeadImg);
        chatDBEntity.setMsgDetails(msg);
        chatDBEntity.setMsgType(entity.getMsgType());
        chatDBEntity.setAccount(userEntity.getAccount());
        chatDBEntity.setIsFrom(entity.getIsFrom());
        chatDBEntity.setIsRead(DBConstants.YES);
        chatDBEntity.setCreateTime(entity.getCreateTime());
        chatAdapter.addData(chatDBEntity);
        dbUtils.addChat(chatDBEntity);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void senMsgResult(SendChatEventModel eventModel){
        if(eventModel.isSuccess){
            ToastUtils.showShort("发送成功");
            chatUI_inputTv.setText("");
        }else{
            ToastUtils.showShort("发送失败");
        }
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        chatUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {

        chatUI_statusView = (View) findViewById(R.id.chatUI_statusView);
        chatUI_backImg = (ImageView) findViewById(R.id.chatUI_backImg);
        chatUI_titleTv = (TextView) findViewById(R.id.chatUI_titleTv);
        chatUI_moreImg = (ImageView) findViewById(R.id.chatUI_moreImg);
        chatUI_refreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.chatUI_refreshLayout);
        chatUI_recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.chatUI_recyclerView);
        chatUI_inputTv = (EditText) findViewById(R.id.chatUI_inputTv);
        chatUI_inputBottomView = (View) findViewById(R.id.chatUI_inputBottomView);
        chatUI_sendTv = (TextView) findViewById(R.id.chatUI_sendTv);
        chatUI_inputTv.setOnFocusChangeListener(this);
        chatUI_sendTv.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v == chatUI_inputTv){
            if(hasFocus){
                chatUI_inputBottomView.setBackgroundColor(ContextCompat.getColor(this,R.color.themeColor));
            }else{
                chatUI_inputBottomView.setBackgroundColor(ContextCompat.getColor(this,R.color.lineColor));
                KeyboardUtils.hideSoftInput(this);
            }
        }
    }

}
