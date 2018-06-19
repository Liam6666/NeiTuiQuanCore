package com.neituiquan.work;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.neituiquan.base.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 个人简历
 *
 *
 */

public class ResumeActivity extends BaseActivity {

    private SmartRefreshLayout resumeUI_refreshLayout;
    private ScrollView resumeUI_scrollView;
    private View resumeUI_headBGView;
    private CircleImageView resumeUI_headImg;
    private TextView resumeUI_nameTv;
    private TextView resumeUI_mottoTv;
    private TextView resumeUI_educationTv;
    private TextView resumeUI_workAgeTv;
    private TextView resumeUI_birthdayTv;
    private TextView resumeUI_targetCity;
    private TextView resumeUI_phoneTv;
    private TextView resumeUI_emailTv;
    private TextView resumeUI_targetWorkTv;
    private TextView resumeUI_targetSalaryTv;
    private LinearLayout resumeUI_ELayout;
    private LinearLayout resumeUI_SLayout;
    private LinearLayout resumeUI_PLayout;
    private LinearLayout resumeUI_ALayout;
    private TextView resumeUI_introductionTv;
    private View resumeUI_statusView;
    private FrameLayout resumeUI_headBGLayout;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_resume);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
    }

    private void initStatusBar() {
        int barHeight = BarUtils.getStatusBarHeight();
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, barHeight);
        resumeUI_statusView.setLayoutParams(params);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) resumeUI_headBGLayout.getLayoutParams();
                params1.height += BarUtils.getStatusBarHeight();
                resumeUI_headBGLayout.setLayoutParams(params1);
            }
        });
    }


    private void bindViews() {
        resumeUI_refreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.resumeUI_refreshLayout);
        resumeUI_scrollView = (ScrollView) findViewById(R.id.resumeUI_scrollView);
        resumeUI_headBGView = findViewById(R.id.resumeUI_headBGView);
        resumeUI_headImg = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.resumeUI_headImg);
        resumeUI_nameTv = (TextView) findViewById(R.id.resumeUI_nameTv);
        resumeUI_mottoTv = (TextView) findViewById(R.id.resumeUI_mottoTv);
        resumeUI_educationTv = (TextView) findViewById(R.id.resumeUI_educationTv);
        resumeUI_workAgeTv = (TextView) findViewById(R.id.resumeUI_workAgeTv);
        resumeUI_birthdayTv = (TextView) findViewById(R.id.resumeUI_birthdayTv);
        resumeUI_targetCity = (TextView) findViewById(R.id.resumeUI_targetCity);
        resumeUI_phoneTv = (TextView) findViewById(R.id.resumeUI_phoneTv);
        resumeUI_emailTv = (TextView) findViewById(R.id.resumeUI_emailTv);
        resumeUI_targetWorkTv = (TextView) findViewById(R.id.resumeUI_targetWorkTv);
        resumeUI_targetSalaryTv = (TextView) findViewById(R.id.resumeUI_targetSalaryTv);
        resumeUI_ELayout = (LinearLayout) findViewById(R.id.resumeUI_ELayout);
        resumeUI_SLayout = (LinearLayout) findViewById(R.id.resumeUI_SLayout);
        resumeUI_PLayout = (LinearLayout) findViewById(R.id.resumeUI_PLayout);
        resumeUI_ALayout = (LinearLayout) findViewById(R.id.resumeUI_ALayout);
        resumeUI_introductionTv = (TextView) findViewById(R.id.resumeUI_introductionTv);
        resumeUI_statusView = (View) findViewById(R.id.resumeUI_statusView);
        resumeUI_headBGLayout = findViewById(R.id.resumeUI_headBGLayout);
    }
}
