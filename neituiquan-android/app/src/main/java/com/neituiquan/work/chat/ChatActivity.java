package com.neituiquan.work.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.UUID;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 聊天界面
 *
 * 必须接受三个参数 对方id 对方昵称 对方头像
 *
 * otherSideId
 * osNickName
 * osHeadImg
 *
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, ChatEmotionFragment.ChatEmotionFragmentCallBack {

    private LinearLayout chatUI_toolBarLayout;
    private View chatUI_statusView;
    private ImageView chatUI_backImg;
    private TextView chatUI_titleTv;
    private ImageView chatUI_moreImg;
    private SmartRefreshLayout chatUI_refreshLayout;
    private RecyclerView chatUI_recyclerView;
    private EditText chatUI_inputTv;
    private View chatUI_inputBottomView;
    private TextView chatUI_sendTv;
    private ImageView chatUI_photoImg;
    private ImageView chatUI_cameraImg;
    private ImageView chatUI_emjoyImg;
    private LinearLayout chatUI_contentLayout;
    private FrameLayout chatUI_frameLayout;

    private LinearLayoutManager linearLayoutManager;

    private AppDBUtils dbUtils;

    private String otherSideId;//对方ID

    private String osNickName;

    private String osHeadImg;

    private ChatAdapter chatAdapter;

    private UserEntity userEntity;

    private int pageIndex = 0;

    private static final String TAG = "fuck chat";

    private long lastSendTime = System.currentTimeMillis();

    //最小发送间隔
    private static final long MIN_SEND_SPACE = 500;

    private ChatEmotionFragment chatEmotionFragment;

    private boolean emotionIsShow = false;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        if(savedInstanceState != null){
            removeAllFragment();
        }
        otherSideId = getIntent().getStringExtra("otherSideId");
        osNickName = getIntent().getStringExtra("osNickName");
        osHeadImg = getIntent().getStringExtra("osHeadImg");
        dbUtils = AppDBFactory.getInstance(this);
        initValues();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changedSoft();
            }
        });
    }

    private void initValues(){
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        chatUI_recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(this);
        chatUI_recyclerView.setAdapter(chatAdapter);
        userEntity = App.getAppInstance().getUserInfoUtils().getUserInfo().data;
        List<ChatDBEntity> list = dbUtils.getChatList(otherSideId,pageIndex);
        chatAdapter.refresh(list);
        dbUtils.updateChatState(otherSideId);
        chatUI_titleTv.setText(osNickName);
    }

    /**
     * 接受到这个聊天组的新消息
     * @param eventModel
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newChatNotify(ChatEventModel eventModel){
        if(eventModel.chatDBEntity.getFromId().equals(otherSideId)){
            chatAdapter.addData(eventModel.chatDBEntity);
            dbUtils.addChat(eventModel.chatDBEntity);
            chatUI_recyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * 下拉刷新方法
     * 这里不要当做刷新方法，而是当做下拉加载更多
     * @param refreshLayout
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageIndex ++;
        List<ChatDBEntity> entityList = dbUtils.getChatList(otherSideId,pageIndex);
        chatAdapter.addData(entityList);
        refreshLayout.finishRefresh(100);
        if(entityList.size() < FinalData.PAGE_SIZE){
            //没有更多了
            refreshLayout.setEnableRefresh(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.chatUI_sendTv:
                sendMessage(chatUI_inputTv.getText().toString());
                break;
            case R.id.chatUI_emjoyImg:
                if(emotionIsShow){
                    hindEmotion();
                }else{
                    showEmotion();
                }
                break;
        }
    }

    private void showEmotion(){
        hindKeyboard();
        if(chatEmotionFragment == null){
            chatEmotionFragment = ChatEmotionFragment.newInstance();
        }
        if(!chatEmotionFragment.isAdded()){
            createTransaction()
                    .add(R.id.chatUI_frameLayout,chatEmotionFragment,"chatEmotionFragment")
                    .show(chatEmotionFragment)
                    .commit();
        }else{
            createTransaction()
                    .show(chatEmotionFragment)
                    .commit();
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chatUI_frameLayout.getLayoutParams();
        params.height = SizeUtils.dp2px(200);
        chatUI_frameLayout.setLayoutParams(params);
        emotionIsShow = true;
        chatEmotionFragment.setChatEmotionFragmentCallBack(this);
    }

    private void hindEmotion(){
        if(chatEmotionFragment == null){
            return;
        }
        if(!chatEmotionFragment.isAdded()){
            return;
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chatUI_frameLayout.getLayoutParams();
        params.height = 0;
        chatUI_frameLayout.setLayoutParams(params);
        createTransaction()
                .hide(chatEmotionFragment)
                .commit();
        emotionIsShow = false;
    }

    @Override
    public void onItemClick(String emotionUrl) {
        sendMessage(emotionUrl);
    }

    /**
     * 发送消息
     */
    private void sendMessage(String msg){
        if(NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_NO){
            ToastUtils.showShort("当前网络不可用");
            return;
        }
        long nowTime = System.currentTimeMillis();
        if((nowTime - lastSendTime) < MIN_SEND_SPACE){
            ToastUtils.showShort("发送太快");
            return;
        }
        if(otherSideId.equals(userEntity.getId())){
            ToastUtils.showShort("不能给自己发送消息");
            return;
        }
        if(StringUtils.isEmpty(msg)){
            ToastUtils.showShort("不能发送空消息");
            return;
        }
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
        entity.setReceiveNickName(osNickName);
        entity.setReceiveHeadImg(osHeadImg);
        entity.setMsgDetails(msg);
        entity.setMsgType(DBConstants.MSG_TYPE_CHAT);
        entity.setAccount(userEntity.getAccount());
        entity.setIsFrom(DBConstants.YES);
        entity.setCreateTime(System.currentTimeMillis()+"");
        String json = new Gson().toJson(entity);
        String url = FinalData.BASE_URL + "/sendChat";
        //开始发送
        HttpFactory.getHttpUtils().post(json,url,new SendChatEventModel());
        if(FinalData.DEBUG){
            Log.e(TAG,new Gson().toJson(entity));
        }
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
        chatUI_recyclerView.smoothScrollToPosition(0);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void senMsgResult(SendChatEventModel eventModel){
        if(eventModel.isSuccess){
            lastSendTime = System.currentTimeMillis();
            ToastUtils.showShort("发送成功");
            chatUI_inputTv.setText("");
        }else{
            ToastUtils.showShort("发送失败");
        }
    }

    private void changedSoft(){
        final int toolBarHeight = SizeUtils.getMeasuredHeight(chatUI_toolBarLayout);
        final int getStartY = (int) chatUI_contentLayout.getY() + toolBarHeight;
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if(height > 0){
                    chatUI_contentLayout.setY(getStartY - height);
                    hindEmotion();
                }else{
                    chatUI_contentLayout.setY(toolBarHeight);
                }
            }
        });
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        chatUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {

        chatUI_toolBarLayout = findViewById(R.id.chatUI_toolBarLayout);
        chatUI_statusView = (View) findViewById(R.id.chatUI_statusView);
        chatUI_backImg = (ImageView) findViewById(R.id.chatUI_backImg);
        chatUI_titleTv = (TextView) findViewById(R.id.chatUI_titleTv);
        chatUI_moreImg = (ImageView) findViewById(R.id.chatUI_moreImg);
        chatUI_refreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.chatUI_refreshLayout);
        chatUI_recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.chatUI_recyclerView);
        chatUI_inputTv = (EditText) findViewById(R.id.chatUI_inputTv);
        chatUI_inputBottomView = (View) findViewById(R.id.chatUI_inputBottomView);
        chatUI_sendTv = (TextView) findViewById(R.id.chatUI_sendTv);
        chatUI_photoImg = (ImageView) findViewById(R.id.chatUI_photoImg);
        chatUI_cameraImg = (ImageView) findViewById(R.id.chatUI_cameraImg);
        chatUI_emjoyImg = (ImageView) findViewById(R.id.chatUI_emjoyImg);
        chatUI_contentLayout = findViewById(R.id.chatUI_contentLayout);
        chatUI_frameLayout = findViewById(R.id.chatUI_frameLayout);
        chatUI_sendTv.setOnClickListener(this);
        chatUI_emjoyImg.setOnClickListener(this);
        chatUI_refreshLayout.setOnRefreshListener(this);
        chatUI_inputTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    chatUI_inputBottomView.setBackgroundColor(ContextCompat.getColor(ChatActivity.this,R.color.themeColor));
                }else{
                    chatUI_inputBottomView.setBackgroundColor(ContextCompat.getColor(ChatActivity.this,R.color.lineColor));
                }
            }
        });
        chatUI_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    hindKeyboard();
                    if(emotionIsShow){
                        hindEmotion();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void hindKeyboard(){
        if(KeyboardUtils.isSoftInputVisible(this)){
            KeyboardUtils.hideSoftInput(this);
            if(KeyboardUtils.isSoftInputVisible(this)){
                KeyboardUtils.hideSoftInput(chatUI_inputTv);
            }
        }
    }

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(this);
        super.onDestroy();
    }


    private FragmentTransaction createTransaction(){
        return getSupportFragmentManager().beginTransaction();
    }

    private void removeAllFragment(){
        FragmentTransaction transaction = createTransaction();
        for(Fragment fragment : getSupportFragmentManager().getFragments()){
            transaction.remove(fragment);
        }
        if(getSupportFragmentManager().getBackStackEntryCount() != 0){
            for(int i = 0 ; i < getSupportFragmentManager().getBackStackEntryCount() ; i ++){
                getSupportFragmentManager().popBackStack();
            }
        }
        transaction.commit();
    }

}
