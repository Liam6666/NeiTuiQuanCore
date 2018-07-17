package com.neituiquan.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.neituiquan.FinalData;
import com.neituiquan.work.account.AccountActivity;
import com.neituiquan.work.widgets.PhotoExtractActivity;

import static java.security.AccessController.getContext;


/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 */

public class TestActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtils.permission(FinalData.PERMISSIONS).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {
                startActivity(new Intent(TestActivity.this, PhotoExtractActivity.class));
            }

            @Override
            public void onDenied() {
                ToastUtils.showShort("打开权限");
            }
        }).request();

    }



    @Override
    protected void onResume() {
        super.onResume();
    }


}
