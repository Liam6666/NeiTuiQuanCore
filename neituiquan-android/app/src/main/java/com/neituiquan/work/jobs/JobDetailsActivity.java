package com.neituiquan.work.jobs;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.entity.CompanyEntity;
import com.neituiquan.entity.ReleaseJobsEntity;
import com.neituiquan.entity.UserEntity;
import com.neituiquan.gson.CompanyModel;
import com.neituiquan.gson.JobModel;
import com.neituiquan.gson.UserModel;
import com.neituiquan.httpEvent.GetJobInfoEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.Millis2Date;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Augustine on 2018/7/11.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 职位详情
 */

public class JobDetailsActivity extends BaseActivity implements View.OnClickListener {

    private View jobDetailsUI_statusView;
    private ImageView jobDetailsUI_backImg;
    private ImageView jobDetailsUI_moreImg;
    private TextView jobDetailsUI_titleTv;
    private TextView jobDetailsUI_salaryTv;
    private TextView jobDetailsUI_createTimeTv;
    private TextView jobDetailsUI_cityTv;
    private TextView jobDetailsUI_workAgeTv;
    private TextView jobDetailsUI_educationTv;
    private com.neituiquan.view.CompanyIconView jobDetailsUI_companyIconView;
    private TextView jobDetailsUI_companyNameTv;
    private TextView jobDetailsUI_companyAbsTv;
    private TextView jobDetailsUI_descriptionTv;
    private TextView jobDetailsUI_workPositionTv;
    private de.hdodenhof.circleimageview.CircleImageView jobDetailsUI_releaseHeadImg;
    private TextView jobDetailsUI_releaseNickName;
    private TextView jobDetailsUI_toChatTv;
    private LinearLayout jobDetailsUI_moreJobTv;
    private TextView jobDetailsUI_companyLinkTv;
    private LinearLayout jobDetailsUI_likeLayout;
    private LinearLayout jobDetailsUI_shareLayout;
    private TextView jobDetailsUI_pushResumeTv;
    private TextView jobDetailsUI_titleBarTv;
    private TextView jobDetailsUI_scoreCountTv;

    private String jobId;

    private ReleaseJobsEntity releaseJobsEntity;

    private CompanyEntity companyEntity;

    private UserEntity userEntity;

    private static final int JOB = 123;

    private static final int COMPANY = 24132;

    private static final int USER = 53241;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_job_details);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        jobId = getIntent().getStringExtra("jobId");
        if(jobId != null){
            getJobInfo();
        }else{

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.jobDetailsUI_backImg:
                finish();
                break;
        }
    }

    private void getJobInfo(){
        String url = FinalData.BASE_URL + "/findJobsById?id=" + jobId;
        HttpFactory.getHttpUtils().get(url,new GetJobInfoEventModel(JOB));
    }

    private void getCompanyInfo(){
        String url = FinalData.BASE_URL + "/getCompany?id=" + releaseJobsEntity.getCompanyId();
        HttpFactory.getHttpUtils().get(url,new GetJobInfoEventModel(COMPANY));
    }

    private void getUserInfo(){
        String url = FinalData.BASE_URL + "/findUserSimpleById?id=" + releaseJobsEntity.getUserId();
        HttpFactory.getHttpUtils().get(url,new GetJobInfoEventModel(USER));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getJobInfoResult(GetJobInfoEventModel eventModel){
        if(eventModel.isSuccess){
            switch (eventModel.eventId){
                case JOB:
                    JobModel model = new Gson().fromJson(eventModel.resultStr,JobModel.class);
                    releaseJobsEntity = model.data;
                    initValues();
                    getCompanyInfo();
                    getUserInfo();
                    break;
                case COMPANY:
                    CompanyModel companyModel = new Gson().fromJson(eventModel.resultStr,CompanyModel.class);
                    companyEntity = companyModel.data;
                    initCompanyValues();
                    break;
                case USER:
                    UserModel userModel = new Gson().fromJson(eventModel.resultStr,UserModel.class);
                    userEntity = userModel.data;
                    initUserValues();
                    break;
            }

        }
    }

    private void initValues(){
        jobDetailsUI_titleTv.setText(releaseJobsEntity.getTitle());
        jobDetailsUI_salaryTv.setText(releaseJobsEntity.getMinSalary() + "K—" + releaseJobsEntity.getMaxSalary()+"K");
        jobDetailsUI_createTimeTv.setText("发布于："+Millis2Date.simpleMillis2Date(releaseJobsEntity.getCreateTime()));
        jobDetailsUI_cityTv.setText(releaseJobsEntity.getCity());
        jobDetailsUI_workAgeTv.setText(releaseJobsEntity.getWorkAge());
        jobDetailsUI_educationTv.setText(releaseJobsEntity.getEducation());
        jobDetailsUI_descriptionTv.setText(releaseJobsEntity.getDescription());
        jobDetailsUI_titleBarTv.setText(releaseJobsEntity.getTitle());
    }

    private void initCompanyValues(){
        jobDetailsUI_companyNameTv.setText(companyEntity.getCompanyName());
        jobDetailsUI_companyAbsTv.setText(companyEntity.getPeopleNum() + "—"+companyEntity.getAddress());
        jobDetailsUI_companyIconView.setValues(companyEntity.getCompanyName(),Integer.valueOf(releaseJobsEntity.getMaxSalary()));
        jobDetailsUI_workPositionTv.setText(companyEntity.getAddress());
        jobDetailsUI_companyLinkTv.setText(companyEntity.getLinkUrl());
        jobDetailsUI_scoreCountTv.setText("共有"+companyEntity.getScoreCount()+"条评价信息");
    }

    private void initUserValues(){
        Glide.with(this).load(FinalData.IMG + userEntity.getHeadImg()).into(jobDetailsUI_releaseHeadImg);
        jobDetailsUI_releaseNickName.setText(userEntity.getNickName());

    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        jobDetailsUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {
        jobDetailsUI_statusView = (View) findViewById(R.id.jobDetailsUI_statusView);
        jobDetailsUI_backImg = (ImageView) findViewById(R.id.jobDetailsUI_backImg);
        jobDetailsUI_moreImg = (ImageView) findViewById(R.id.jobDetailsUI_moreImg);
        jobDetailsUI_titleTv = (TextView) findViewById(R.id.jobDetailsUI_titleTv);
        jobDetailsUI_salaryTv = (TextView) findViewById(R.id.jobDetailsUI_salaryTv);
        jobDetailsUI_createTimeTv = (TextView) findViewById(R.id.jobDetailsUI_createTimeTv);
        jobDetailsUI_cityTv = (TextView) findViewById(R.id.jobDetailsUI_cityTv);
        jobDetailsUI_workAgeTv = (TextView) findViewById(R.id.jobDetailsUI_workAgeTv);
        jobDetailsUI_educationTv = (TextView) findViewById(R.id.jobDetailsUI_educationTv);
        jobDetailsUI_companyIconView = (com.neituiquan.view.CompanyIconView) findViewById(R.id.jobDetailsUI_companyIconView);
        jobDetailsUI_companyNameTv = (TextView) findViewById(R.id.jobDetailsUI_companyNameTv);
        jobDetailsUI_companyAbsTv = (TextView) findViewById(R.id.jobDetailsUI_companyAbsTv);
        jobDetailsUI_descriptionTv = (TextView) findViewById(R.id.jobDetailsUI_descriptionTv);
        jobDetailsUI_workPositionTv = (TextView) findViewById(R.id.jobDetailsUI_workPositionTv);
        jobDetailsUI_releaseHeadImg = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.jobDetailsUI_releaseHeadImg);
        jobDetailsUI_releaseNickName = (TextView) findViewById(R.id.jobDetailsUI_releaseNickName);
        jobDetailsUI_toChatTv = (TextView) findViewById(R.id.jobDetailsUI_toChatTv);
        jobDetailsUI_moreJobTv = (LinearLayout) findViewById(R.id.jobDetailsUI_moreJobTv);
        jobDetailsUI_companyLinkTv = (TextView) findViewById(R.id.jobDetailsUI_companyLinkTv);
        jobDetailsUI_likeLayout = (LinearLayout) findViewById(R.id.jobDetailsUI_likeLayout);
        jobDetailsUI_shareLayout = (LinearLayout) findViewById(R.id.jobDetailsUI_shareLayout);
        jobDetailsUI_pushResumeTv = (TextView) findViewById(R.id.jobDetailsUI_pushResumeTv);
        jobDetailsUI_titleBarTv = findViewById(R.id.jobDetailsUI_titleBarTv);
        jobDetailsUI_scoreCountTv = findViewById(R.id.jobDetailsUI_scoreCountTv);
        jobDetailsUI_backImg.setOnClickListener(this);
    }

}
