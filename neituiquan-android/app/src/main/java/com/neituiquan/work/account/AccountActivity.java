package com.neituiquan.work.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 */

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private View accountUI_statusView;
    private TextView accountUI_cancelTv;
    private TextView accountUI_switcherTv;
    private FrameLayout accountUI_frameLayout;

    private LoginFragment loginFragment;

    private RegisterFragment registerFragment;

    private int showIndex = 2;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        if(savedInstanceState != null){
            removeAllFragment();
        }
        initFragments();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.accountUI_switcherTv:
                switcherFragment(showIndex);
                break;
            case R.id.accountUI_cancelTv:
                finish();
                break;
        }
    }


    private void initFragments(){
        loginFragment = LoginFragment.newInstance();
        registerFragment = RegisterFragment.newInstance();
        createTransaction()
                .add(R.id.accountUI_frameLayout,loginFragment,"loginFragment")
                .add(R.id.accountUI_frameLayout,registerFragment,"registerFragment")
                .show(loginFragment)
                .hide(registerFragment)
                .commit();
    }

    private void switcherFragment(int index){
        if(index == 1){
            createTransaction().show(loginFragment)
                    .hide(registerFragment)
                    .commit();
            showIndex = 2;
            accountUI_switcherTv.setText("注册");
        }else{
            createTransaction().show(registerFragment)
                    .hide(loginFragment)
                    .commit();
            showIndex = 1;
            accountUI_switcherTv.setText("登录");
        }
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
        accountUI_statusView.setLayoutParams(params);
    }


    private void bindViews() {

        accountUI_statusView = (View) findViewById(R.id.accountUI_statusView);
        accountUI_cancelTv = (TextView) findViewById(R.id.accountUI_cancelTv);
        accountUI_switcherTv = (TextView) findViewById(R.id.accountUI_switcherTv);
        accountUI_frameLayout = (FrameLayout) findViewById(R.id.accountUI_frameLayout);

        accountUI_switcherTv.setOnClickListener(this);
        accountUI_cancelTv.setOnClickListener(this);
    }

}
