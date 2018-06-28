package com.neituiquan.work;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.work.fragment.HomePageFragment;
import com.neituiquan.work.fragment.UserFragment;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private View mainUI_statusView;
    private FrameLayout mainUI_contentFrameLayout;
    private LinearLayout mainUI_homePageLayout;
    private ImageView mainUI_homePageIcon;
    private TextView mainUI_homePageTv;
    private LinearLayout mainUI_foundLayout;
    private ImageView mainUI_foundIcon;
    private TextView mainUI_foundTv;
    private LinearLayout mainUI_messageLayout;
    private ImageView mainUI_messageIcon;
    private TextView mainUI_messageTv;
    private LinearLayout mainUI_meLayout;
    private ImageView mainUI_meIcon;
    private TextView mainUI_meTv;

    private HomePageFragment homePageFragment;
    private UserFragment userFragment;

    private BaseFragment currentFragment;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        if(savedInstanceState != null){
            removeAllFragment();
        }
        initStatusBar();
        mainUI_statusView.setVisibility(View.GONE);
        initFragments();

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.mainUI_homePageLayout:
                showHindFragment(1);
                break;
            case R.id.mainUI_foundLayout:
                showHindFragment(2);
                break;
            case R.id.mainUI_messageLayout:
                showHindFragment(3);
                break;
            case R.id.mainUI_meLayout:
                showHindFragment(4);
                break;
        }
    }

    private void initFragments(){
        homePageFragment = HomePageFragment.newInstance();
        userFragment = UserFragment.newInstance();
        showHindFragment(1);
    }

    private void showHindFragment(int index){
        mainUI_homePageIcon.setImageResource(R.mipmap.home);
        mainUI_homePageTv.setTextColor(ContextCompat.getColor(this,R.color.lowTextColor));
        mainUI_foundIcon.setImageResource(R.mipmap.form_light);
        mainUI_foundTv.setTextColor(ContextCompat.getColor(this,R.color.lowTextColor));
        mainUI_messageIcon.setImageResource(R.mipmap.message_light);
        mainUI_messageTv.setTextColor(ContextCompat.getColor(this,R.color.lowTextColor));
        mainUI_meIcon.setImageResource(R.mipmap.my);
        mainUI_meTv.setTextColor(ContextCompat.getColor(this,R.color.lowTextColor));
        switch (index){
            case 1:
                if(!homePageFragment.isAdded()){
                    createTransaction()
                            .add(R.id.mainUI_contentFrameLayout,homePageFragment)
                            .show(homePageFragment)
                            .commit();
                }else{
                    createTransaction()
                            .show(homePageFragment)
                            .hide(currentFragment)
                            .commit();
                }
                currentFragment = homePageFragment;
                mainUI_homePageIcon.setImageResource(R.mipmap.home_fill);
                mainUI_homePageTv.setTextColor(ContextCompat.getColor(this,R.color.themeColor));
                break;
            case 2:
                mainUI_foundIcon.setImageResource(R.mipmap.form_fill);
                mainUI_foundTv.setTextColor(ContextCompat.getColor(this,R.color.themeColor));
                break;
            case 3:
                mainUI_messageIcon.setImageResource(R.mipmap.message_fill_light);
                mainUI_messageTv.setTextColor(ContextCompat.getColor(this,R.color.themeColor));
                break;
            case 4:
                if(!userFragment.isAdded()){
                    createTransaction()
                            .add(R.id.mainUI_contentFrameLayout,userFragment)
                            .show(userFragment)
                            .commit();
                }else{
                    createTransaction()
                            .show(userFragment)
                            .hide(currentFragment)
                            .commit();
                }
                currentFragment = userFragment;
                mainUI_meIcon.setImageResource(R.mipmap.my_fill);
                mainUI_meTv.setTextColor(ContextCompat.getColor(this,R.color.themeColor));
                break;
        }


    }

    private FragmentTransaction createTransaction(){
        return getSupportFragmentManager().beginTransaction();
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        mainUI_statusView.setLayoutParams(params);
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

    private void bindViews() {

        mainUI_statusView = (View) findViewById(R.id.mainUI_statusView);
        mainUI_contentFrameLayout = (FrameLayout) findViewById(R.id.mainUI_contentFrameLayout);
        mainUI_homePageLayout = (LinearLayout) findViewById(R.id.mainUI_homePageLayout);
        mainUI_homePageIcon = (ImageView) findViewById(R.id.mainUI_homePageIcon);
        mainUI_homePageTv = (TextView) findViewById(R.id.mainUI_homePageTv);
        mainUI_foundLayout = (LinearLayout) findViewById(R.id.mainUI_foundLayout);
        mainUI_foundIcon = (ImageView) findViewById(R.id.mainUI_foundIcon);
        mainUI_foundTv = (TextView) findViewById(R.id.mainUI_foundTv);
        mainUI_messageLayout = (LinearLayout) findViewById(R.id.mainUI_messageLayout);
        mainUI_messageIcon = (ImageView) findViewById(R.id.mainUI_messageIcon);
        mainUI_messageTv = (TextView) findViewById(R.id.mainUI_messageTv);
        mainUI_meLayout = (LinearLayout) findViewById(R.id.mainUI_meLayout);
        mainUI_meIcon = (ImageView) findViewById(R.id.mainUI_meIcon);
        mainUI_meTv = (TextView) findViewById(R.id.mainUI_meTv);
        mainUI_homePageLayout.setOnClickListener(this);
        mainUI_foundLayout.setOnClickListener(this);
        mainUI_messageLayout.setOnClickListener(this);
        mainUI_meLayout.setOnClickListener(this);

    }

}
