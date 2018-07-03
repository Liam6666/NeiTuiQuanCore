package com.neituiquan.work.account;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.work.R;
import com.suke.widget.SwitchButton;

/**
 * Created by Augustine on 2018/7/3.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 求职意向
 */

public class IntentionActivity extends BaseActivity {

    private View intentionUI_statusView;
    private ImageView intentionUI_backImg;
    private TextView intentionUI_addImg;
    private LinearLayout intentionUI_jobTitleLayout;
    private TextView intentionUI_jobTitleTv;
    private LinearLayout intentionUI_currentStateLayout;
    private TextView intentionUI_currentStateTv;
    private LinearLayout intentionUI_cityLayout;
    private TextView intentionUI_cityTv;
    private LinearLayout intentionUI_gotoWorkTimeLayout;
    private View intentionUI_gotoWorkTimeTv;
    private SwitchButton intentionUI_openResumeButton;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_intention);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();

    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        intentionUI_statusView.setLayoutParams(params);
    }


    private void bindViews() {

        intentionUI_statusView = (View) findViewById(R.id.intentionUI_statusView);
        intentionUI_backImg = (ImageView) findViewById(R.id.intentionUI_backImg);
        intentionUI_addImg = (TextView) findViewById(R.id.intentionUI_addImg);
        intentionUI_jobTitleLayout = (LinearLayout) findViewById(R.id.intentionUI_jobTitleLayout);
        intentionUI_jobTitleTv = (TextView) findViewById(R.id.intentionUI_jobTitleTv);
        intentionUI_currentStateLayout = (LinearLayout) findViewById(R.id.intentionUI_currentStateLayout);
        intentionUI_currentStateTv = (TextView) findViewById(R.id.intentionUI_currentStateTv);
        intentionUI_cityLayout = (LinearLayout) findViewById(R.id.intentionUI_cityLayout);
        intentionUI_cityTv = (TextView) findViewById(R.id.intentionUI_cityTv);
        intentionUI_gotoWorkTimeLayout = (LinearLayout) findViewById(R.id.intentionUI_gotoWorkTimeLayout);
        intentionUI_gotoWorkTimeTv = (View) findViewById(R.id.intentionUI_gotoWorkTimeTv);
        intentionUI_openResumeButton = (com.suke.widget.SwitchButton) findViewById(R.id.intentionUI_openResumeButton);
    }
}
