package com.neituiquan.work.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.neituiquan.adapter.MessageAdapter;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.database.AppDBFactory;
import com.neituiquan.database.LocalCacheDAOImpl;
import com.neituiquan.httpEvent.ChatEventModel;
import com.neituiquan.view.AutoLoadRecyclerView;
import com.neituiquan.work.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class MessageFragment extends BaseFragment {

    public static MessageFragment newInstance() {

        Bundle args = new Bundle();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private View editHeadUI_statusView;
    private ImageView messageFG_addImg;
    private SmartRefreshLayout messageFG_refreshLayout;
    private AutoLoadRecyclerView messageFG_recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private MessageAdapter messageAdapter;

    private LocalCacheDAOImpl localCacheDAO;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(),R.layout.fragment_message,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        localCacheDAO = AppDBFactory.getInstance(getContext());

        linearLayoutManager = new LinearLayoutManager(getContext());
        messageFG_recyclerView.setLayoutManager(linearLayoutManager);

        messageAdapter = new MessageAdapter(getContext(),localCacheDAO.getChatGroupList());
        messageFG_recyclerView.setAdapter(messageAdapter);
    }

    @Override
    public void onLazyInitList() {
        super.onLazyInitList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newChatNotify(ChatEventModel eventModel){
        ToastUtils.showShort("接受到新消息");
        messageAdapter.refresh(localCacheDAO.getChatGroupList());
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        editHeadUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {

        editHeadUI_statusView = (View) findViewById(R.id.editHeadUI_statusView);
        messageFG_addImg = (ImageView) findViewById(R.id.messageFG_addImg);
        messageFG_refreshLayout = findViewById(R.id.messageFG_refreshLayout);
        messageFG_recyclerView = findViewById(R.id.messageFG_recyclerView);
    }
}
