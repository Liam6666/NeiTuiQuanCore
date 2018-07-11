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
import com.neituiquan.gson.ReleaseJobListModel;
import com.neituiquan.httpEvent.ReleaseJobListEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.view.AutoLoadRecyclerView;
import com.neituiquan.work.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 发布的职位列表
 */

public class ReleaseJobListActivity extends BaseActivity implements View.OnClickListener {

    private View releaseJobUI_statusView;
    private ImageView releaseJobUI_backImg;
    private ImageView releaseJobUI_addImg;
    private SmartRefreshLayout releaseJobUI_refreshLayout;
    private AutoLoadRecyclerView releaseJobUI_recyclerView;

    private ReleaseJobsAdapter releaseJobsAdapter;

    private LinearLayoutManager linearLayoutManager;

    private static final int INT = 12392;

    private int pageIndex = 0;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release_job_list);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        initValues();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initValues(){
        linearLayoutManager = new LinearLayoutManager(this);
        releaseJobsAdapter = new ReleaseJobsAdapter(this);
        releaseJobUI_recyclerView.setLayoutManager(linearLayoutManager);
        releaseJobUI_recyclerView.setAdapter(releaseJobsAdapter);
        String url = FinalData.BASE_URL +
                "/findJobsById?id"+ App.getAppInstance().getUserInfoUtils().getUserId() +"&index="+pageIndex;
        HttpFactory.getHttpUtils().get(url,new ReleaseJobListEventModel(INT));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getJobsListResult(ReleaseJobListEventModel eventModel){
        if(eventModel.isSuccess){
            ReleaseJobListModel model = new Gson().fromJson(eventModel.resultStr,ReleaseJobListModel.class);
            releaseJobsAdapter.addData(model.data);
        }
    }

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
        releaseJobUI_refreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.releaseJobUI_refreshLayout);
        releaseJobUI_recyclerView = (com.neituiquan.view.AutoLoadRecyclerView) findViewById(R.id.releaseJobUI_recyclerView);
        releaseJobUI_backImg.setOnClickListener(this);
        releaseJobUI_addImg.setOnClickListener(this);
    }



}
