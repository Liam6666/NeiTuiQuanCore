package com.neituiquan.work.resume;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.gson.UserModel;
import com.neituiquan.gson.UserResumeModel;
import com.neituiquan.work.R;

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

    private UserResumeModel resumeModel;

    private BaseInfoFragment baseInfoFragment;

    public static final int UPDATE_RESUME = 0;

    public static final int IPDATE_RESUME_W = 1;

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
        if(getIntent().getExtras() == null){
            if(FinalData.DEBUG){
                ToastUtils.showShort("getExtras() == null");
            }
            return;
        }
        resumeModel = (UserResumeModel) getIntent().getExtras().getSerializable("resumeModel");
        if(resumeModel.data == null){
            if(FinalData.DEBUG){
                ToastUtils.showShort("resumeModel == null");
            }
            return;
        }
        initFragments();
    }


    private void initFragments(){
        baseInfoFragment = BaseInfoFragment.newInstance();
        createTransaction()
                .add(R.id.editResumeUI_frameLayout,baseInfoFragment,"baseInfoFragment")
                .show(baseInfoFragment)
                .commit();
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
}
