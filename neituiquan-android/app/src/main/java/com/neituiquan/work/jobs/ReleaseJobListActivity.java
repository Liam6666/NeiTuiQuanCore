package com.neituiquan.work.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.adapter.ReleaseJobsAdapter;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.entity.JobsEntity;
import com.neituiquan.gson.JobsListModel;
import com.neituiquan.httpEvent.ReleaseJobListEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ReleaseJobListActivity extends BaseActivity implements View.OnClickListener {

    private View releaseJobUI_statusView;
    private ImageView releaseJobUI_backImg;
    private ImageView releaseJobUI_addImg;
    private RecyclerView releaseJobUI_listView;

    private ReleaseJobsAdapter releaseJobsAdapter;

    private int page = 1;

    private int offset = 15;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release_job_list);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initValues();
    }

    private void initValues(){
        String url = FinalData.BASE_URL + "/findJobsByUserId?userId=" + App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
        HttpFactory.getHttpUtils().get(url,new ReleaseJobListEventModel());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getJobsListResult(ReleaseJobListEventModel eventModel){
        if(eventModel.isSuccess){
            JobsListModel jobsListModel = new Gson().fromJson(eventModel.resultStr,JobsListModel.class);
            if(releaseJobsAdapter == null){
                //初始化
                releaseJobsAdapter = new ReleaseJobsAdapter(this,jobsListModel.data);
                releaseJobUI_listView.setAdapter(releaseJobsAdapter);
                releaseJobsAdapter.setCallBack(itemClickCallBack);
            }else{
                releaseJobsAdapter.refresh(jobsListModel.data);
            }
        }
    }

    ReleaseJobsAdapter.ReleaseJobsAdapterCallBack itemClickCallBack = new ReleaseJobsAdapter.ReleaseJobsAdapterCallBack() {
        @Override
        public void onItemClick(JobsEntity entity, int position) {
            Intent intent = new Intent(ReleaseJobListActivity.this,ReleaseJobsActivity.class);
            intent.putExtra("jobsEntity",entity);
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.releaseJobUI_backImg:
                finish();
                break;
            case R.id.releaseJobUI_addImg:
                Intent intent = new Intent(this,ReleaseJobsActivity.class);

                startActivity(intent);
                break;
        }
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        releaseJobUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {

        releaseJobUI_statusView = (View) findViewById(R.id.releaseJobUI_statusView);
        releaseJobUI_backImg = (ImageView) findViewById(R.id.releaseJobUI_backImg);
        releaseJobUI_addImg = (ImageView) findViewById(R.id.releaseJobUI_addImg);
        releaseJobUI_listView = (android.support.v7.widget.RecyclerView) findViewById(R.id.releaseJobUI_listView);
        releaseJobUI_listView.setLayoutManager(new LinearLayoutManager(this));
        releaseJobUI_backImg.setOnClickListener(this);
        releaseJobUI_addImg.setOnClickListener(this);
    }


}
