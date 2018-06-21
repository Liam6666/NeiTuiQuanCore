package com.neituiquan.work.resume;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.gson.UserModel;
import com.neituiquan.gson.UserResumeModel;
import com.neituiquan.httpEvent.RefreshResumeEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 填写 修改 简历
 *
 *
 */

public class EditResumeActivity extends BaseActivity{

    private View editResumeUI_statusView;
    private FrameLayout editResumeUI_frameLayout;

    private BaseInfoFragment baseInfoFragment;

    /**
     * 个人获奖情况
     */
    private AWListFragment awListFragment;

    public static final int UPDATE_RESUME = 0;

    public static final int UPDATE_USER_INFO = 1;

    public static final int UPDATE_RESUME_A = 2;

    public static final int SAVED_RESUME_A = 3;

    public static final int DEL_RESUME_A = 4;

    private int editType = 0;

    private List<BaseFragment> fragmentList = new ArrayList<>();

    private UserResumeModel resumeModel;

    private UserModel userModel;

    private BaseFragment currentFragment;

    private static final int REFRESH_ID = 1929;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_resume);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        if(savedInstanceState != null){
            removeAllFragment();
        }
        editType = getIntent().getIntExtra("editType",-1);
        userModel = App.getAppInstance().getUserInfoUtils().getUserInfo();
        resumeModel = (UserResumeModel) getIntent().getSerializableExtra("resumeModel");
        initFragments();
    }

    /**
     * 刷新数据
     *
     */
    public void refresh(){
        String url = FinalData.BASE_URL + "/getUserResume?userId=" + userModel.data.getId();
        HttpFactory.getHttpUtils().get(url,new RefreshResumeEventModel(REFRESH_ID));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshResult(RefreshResumeEventModel eventModel){
        if(eventModel.eventId == REFRESH_ID){
            if(eventModel.isSuccess){
                resumeModel = new Gson().fromJson(eventModel.resultStr,UserResumeModel.class);
                userModel = App.getAppInstance().getUserInfoUtils().getUserInfo();
                currentFragment.refresh();
            }
        }
    }


    private void initFragments(){
        FragmentTransaction transaction = createTransaction();
        switch (editType){
            case 0:
                baseInfoFragment = BaseInfoFragment.newInstance();
                currentFragment = baseInfoFragment;
                if(!baseInfoFragment.isAdded()){
                    transaction.add(R.id.editResumeUI_frameLayout,baseInfoFragment,"baseInfoFragment");
                }
                break;
            case 1:
                awListFragment = AWListFragment.newInstance();
                currentFragment = awListFragment;
                if(!awListFragment.isAdded()){
                    transaction.add(R.id.editResumeUI_frameLayout,awListFragment,"awListFragment");
                }
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
        }
        transaction.show(currentFragment)
                .runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        currentFragment.onLazyInitList();
                    }
                });
        transaction.commit();
    }


    private void bindViews() {
        editResumeUI_statusView = (View) findViewById(R.id.editResumeUI_statusView);
        editResumeUI_frameLayout = (FrameLayout) findViewById(R.id.editResumeUI_frameLayout);
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

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        editResumeUI_statusView.setLayoutParams(params);
    }

    public UserResumeModel getResumeModel() {
        return resumeModel;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
