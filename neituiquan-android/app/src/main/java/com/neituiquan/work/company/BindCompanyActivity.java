package com.neituiquan.work.company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.dialog.TipsDialog;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 */

public class BindCompanyActivity extends BaseActivity{

    private View bindCompanyUI_statusView;
    private FrameLayout bindCompanyUI_frameLayout;

    private AddCompanyFragment addCompanyFragment;

    public static final int ADD_COMPANY = 0;

    public static final int UPDATE_COMPANY = 1;

    public static final int ADD_COMPANY_IMG = 2;

    public final static int REQUEST_CODE_PICK_IMAGE = 6;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_company);

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
    protected void onResume() {
        super.onResume();
    }

    private void initFragments(){
        if(addCompanyFragment != null){
            return;
        }
        addCompanyFragment = AddCompanyFragment.newInstance(null);
        createTransaction()
                .add(R.id.bindCompanyUI_frameLayout,addCompanyFragment,"addCompanyFragment")
                .show(addCompanyFragment)
                .commit();
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        bindCompanyUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {

        bindCompanyUI_statusView = (View) findViewById(R.id.bindCompanyUI_statusView);
        bindCompanyUI_frameLayout = (FrameLayout) findViewById(R.id.bindCompanyUI_frameLayout);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AddCompanyImgFragment addCompanyImgFragment = (AddCompanyImgFragment) getSupportFragmentManager().findFragmentByTag("addCompanyImgFragment");
        if(addCompanyImgFragment != null){
            addCompanyImgFragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(this);
        KeyboardUtils.fixSoftInputLeaks(this);
        super.onDestroy();
    }
}

