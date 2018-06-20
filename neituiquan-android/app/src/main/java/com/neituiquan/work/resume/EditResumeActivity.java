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
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;


import java.io.IOException;
import java.lang.ref.SoftReference;
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
        resumeModel = (UserResumeModel) FinalData.resumeModelSoftReference.get();
        if(resumeModel == null){
            refreshUserResumeModel();
        }else{
            initFragments();
        }
    }

    /**
     * 当内存不足被回收时，重新请求
     *
     */
    private void refreshUserResumeModel(){
        final Handler handler = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                if(msg.what == 111){
                    resumeModel = (UserResumeModel) FinalData.resumeModelSoftReference.get();
                    initFragments();
                }
            }
        };
        String url = FinalData.BASE_URL + "/getUserResume?userId=" + userModel.data.getId();
        HttpFactory.getHttpUtils().get(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                UserResumeModel model = new Gson().fromJson(result,UserResumeModel.class);
                FinalData.resumeModelSoftReference = new SoftReference(model);
                handler.sendEmptyMessage(111);
            }
        });
    }


    private void initFragments(){
        baseInfoFragment = BaseInfoFragment.newInstance();
        awListFragment = AWListFragment.newInstance();
        fragmentList.add(baseInfoFragment);
        fragmentList.add(awListFragment);
        FragmentTransaction transaction = createTransaction();
        if(!baseInfoFragment.isAdded()){
            transaction.add(R.id.editResumeUI_frameLayout,baseInfoFragment,"baseInfoFragment");
        }
        if(!awListFragment.isAdded()){
            transaction.add(R.id.editResumeUI_frameLayout,awListFragment,"awListFragment");
        }
        for(BaseFragment fragment : fragmentList){
            transaction.hide(fragment);
        }
        transaction.show(fragmentList.get(editType))
                .runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        fragmentList.get(editType).onLazyInitList();
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
