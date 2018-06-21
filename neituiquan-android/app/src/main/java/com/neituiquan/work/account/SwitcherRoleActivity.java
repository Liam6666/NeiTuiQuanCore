package com.neituiquan.work.account;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.entity.UserEntity;
import com.neituiquan.gson.UserModel;
import com.neituiquan.httpEvent.UpdateRoleEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Augustine on 2018/6/21.
 * <p>
 * email:nice_ohoh@163.com
 */

public class SwitcherRoleActivity extends BaseActivity implements View.OnClickListener {


    private View switcherRoleUI_statusView;
    private ImageView switcherRoleUI_backImg;
    private ImageView switcherRoleUI_role1Img;
    private ImageView switcherRoleUI_role2Img;
    private ImageView switcherRoleUI_role3Img;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_swither_role);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        switcherRoleUI_statusView.setLayoutParams(params);
    }


    private void bindViews() {

        switcherRoleUI_statusView = (View) findViewById(R.id.switcherRoleUI_statusView);
        switcherRoleUI_backImg = (ImageView) findViewById(R.id.switcherRoleUI_backImg);
        switcherRoleUI_role1Img = (ImageView) findViewById(R.id.switcherRoleUI_role1Img);
        switcherRoleUI_role2Img = (ImageView) findViewById(R.id.switcherRoleUI_role2Img);
        switcherRoleUI_role3Img = (ImageView) findViewById(R.id.switcherRoleUI_role3Img);

        switcherRoleUI_role1Img.setOnClickListener(this);
        switcherRoleUI_role2Img.setOnClickListener(this);
        switcherRoleUI_role3Img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.switcherRoleUI_role1Img:
                switchRole(1);
                break;
            case R.id.switcherRoleUI_role2Img:
                switchRole(2);
                break;
            case R.id.switcherRoleUI_role3Img:
                switchRole(3);
                break;
        }
    }

    private void switchRole(int index){
        String url = FinalData.BASE_URL + "/updateRole";
        UserEntity userEntity = App.getAppInstance().getUserInfoUtils().getUserInfo().data;
        switch (index){
            case 1:
                userEntity.setRoleName("找人");
                break;
            case 2:
                userEntity.setRoleName("HR");
                break;
            case 3:
                userEntity.setRoleName("找工作");
                break;
        }
        String json = new Gson().toJson(userEntity);
        HttpFactory.getHttpUtils().post(json,url,new UpdateRoleEventModel());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateResult(UpdateRoleEventModel eventModel){
        if(eventModel.isSuccess){
            UserModel userModel = new Gson().fromJson(eventModel.resultStr,UserModel.class);
            if(userModel.code == 0){

                App.getAppInstance().getUserInfoUtils().saveUserInfo(eventModel.resultStr);

                //发送给UserFragment
                EventBus.getDefault().post(userModel);
                finish();
            }else{
                ToastUtils.showShort(userModel.msg);
            }
        }
    }
}
