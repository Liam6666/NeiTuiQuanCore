package com.neituiquan.work;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.neituiquan.base.BaseActivity;

/**
 * Created by Augustine on 2018/7/5.
 * <p>
 * email:nice_ohoh@163.com
 */

public class WebActivity extends BaseActivity {

    private View webUI_statusView;
    private ImageView webUI_backImg;
    private ImageView webUI_moreImg;
    private LinearLayout webUI_contentLayout;

    private AgentWeb agentWeb;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webUI_contentLayout, new LinearLayout.LayoutParams(-1,-1))
                .useDefaultIndicator(ContextCompat.getColor(this,R.color.themeColor), 2)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
                .interceptUnkownUrl()
                .createAgentWeb()
                .ready()
                .go("https://www.baidu.com");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(agentWeb.handleKeyEvent(keyCode,event)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if(!agentWeb.back()){
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        agentWeb.clearWebCache();
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        webUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {

        webUI_statusView = (View) findViewById(R.id.webUI_statusView);
        webUI_backImg = (ImageView) findViewById(R.id.webUI_backImg);
        webUI_moreImg = (ImageView) findViewById(R.id.webUI_moreImg);
        webUI_contentLayout = (LinearLayout) findViewById(R.id.webUI_contentLayout);
    }
}
