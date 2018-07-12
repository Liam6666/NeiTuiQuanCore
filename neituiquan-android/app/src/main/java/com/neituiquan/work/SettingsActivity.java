package com.neituiquan.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.neituiquan.App;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.database.AppDBFactory;
import com.neituiquan.database.AppDBUtils;
import com.neituiquan.service.AppService;
import com.neituiquan.utils.PositionUtils;
import com.neituiquan.work.account.AccountActivity;

/**
 * Created by Augustine on 2018/6/21.
 * <p>
 * email:nice_ohoh@163.com
 */

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private TextView settingsUI_outLogin;

    private TextView settingsUI_clearLocalDB;

    private View settingsUI_statusView;

    private AppDBUtils dbUtils;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();

    }


    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        settingsUI_statusView.setLayoutParams(params);
    }

    private void bindViews(){
        settingsUI_outLogin = findViewById(R.id.settingsUI_outLogin);
        settingsUI_statusView = findViewById(R.id.settingsUI_statusView);
        settingsUI_clearLocalDB = findViewById(R.id.settingsUI_clearLocalDB);
        settingsUI_outLogin.setOnClickListener(this);
        settingsUI_clearLocalDB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.settingsUI_outLogin:
                App.getAppInstance().getUserInfoUtils().clearUserInfo();
                PositionUtils.clearLocationInfo(this);
                finish();
                ActivityUtils.finishActivity(MainActivity.class);
                startActivity(new Intent(SettingsActivity.this, AccountActivity.class));
                break;
            case R.id.settingsUI_clearLocalDB:
                dbUtils = AppDBFactory.getInstance(this);
                dbUtils.removeAll();
                break;
        }
    }
}
