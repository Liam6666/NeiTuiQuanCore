package com.neituiquan.work;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.blankj.utilcode.util.PermissionUtils;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
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
                if(App.getAppInstance().getUserInfoUtils().getUserInfo() == null){
                    //未登录
                    startActivity(new Intent(LauncherActivity.this,AccountActivity.class));
                }else{
                    startActivity(new Intent(LauncherActivity.this,MainActivity.class));
                }
                finish();
            }
        },1000);
    }


}
