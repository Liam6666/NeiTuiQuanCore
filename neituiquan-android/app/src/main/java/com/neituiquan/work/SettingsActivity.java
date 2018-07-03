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
import com.neituiquan.work.account.AccountActivity;

/**
 * Created by Augustine on 2018/6/21.
 * <p>
 * email:nice_ohoh@163.com
 */

public class SettingsActivity extends BaseActivity {

    private TextView settingsUI_outLogin;

    private View settingsUI_statusView;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        settingsUI_outLogin = findViewById(R.id.settingsUI_outLogin);
        settingsUI_statusView = findViewById(R.id.settingsUI_statusView);
        initStatusBar();
        settingsUI_outLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getAppInstance().getUserInfoUtils().clearUserInfo();
                finish();
                ActivityUtils.finishActivity(MainActivity.class);
                startActivity(new Intent(SettingsActivity.this, AccountActivity.class));
            }
        });
    }


    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        settingsUI_statusView.setLayoutParams(params);
    }
}
