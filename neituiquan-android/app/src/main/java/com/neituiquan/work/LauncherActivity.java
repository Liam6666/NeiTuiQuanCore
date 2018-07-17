package com.neituiquan.work;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.neituiquan.App;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.service.AppService;
import com.neituiquan.work.account.AccountActivity;

/**
 * Created by Augustine on 2018/6/28.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 启动页
 */

public class LauncherActivity extends BaseActivity {


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_launcher);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },1000);
        if(App.getAppInstance().getUserInfoUtils().getUserInfo() == null){
            startActivity(new Intent(LauncherActivity.this,AccountActivity.class));
        }else{
            startActivity(new Intent(LauncherActivity.this,MainActivity.class));
            startService(new Intent(LauncherActivity.this,AppService.class));
        }
        finish();
    }


    @Override
    public void onBackPressed() {
    }

}
