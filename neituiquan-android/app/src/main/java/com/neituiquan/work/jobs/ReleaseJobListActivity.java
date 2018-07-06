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
import com.neituiquan.dialog.MenuDialog;
import com.neituiquan.entity.JobsEntity;
import com.neituiquan.gson.ReleaseJobListModel;
import com.neituiquan.httpEvent.ReleaseJobListEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


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

    private static final int INIT = 0;

    private static final int DEL = 1;

    private int page = 1;

    private int offset = 15;

    private MenuDialog menuDialog;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release_job_list);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        menuDialog = new MenuDialog(this);
        menuDialog.setMenuDialogCallBack(menuDialogCallBack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initValues();
    }

    private void initValues(){
        String url = FinalData.BASE_URL + "/findJobsByUserId?userId=" + App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
        HttpFactory.getHttpUtils().get(url,new ReleaseJobListEventModel(INIT));
    }

    private void del(String id){
        String url = FinalData.BASE_URL + "/delJobs?id="+id;
        HttpFactory.getHttpUtils().get(url,new ReleaseJobListEventModel(DEL));

        releaseJobsAdapter.del(id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getJobsListResult(ReleaseJobListEventModel eventModel){
        switch (eventModel.eventId){
            case INIT:
                if(eventModel.isSuccess){
                    ReleaseJobListModel releaseJobListModel = new Gson().fromJson(eventModel.resultStr,ReleaseJobListModel.class);
                    if(releaseJobsAdapter == null){
                        //初始化
                        releaseJobsAdapter = new ReleaseJobsAdapter(this, releaseJobListModel.data);
                        releaseJobUI_listView.setAdapter(releaseJobsAdapter);
                        releaseJobsAdapter.setCallBack(itemClickCallBack);
                    }else{
                        releaseJobsAdapter.refresh(releaseJobListModel.data);
                    }
                }
                break;
            case DEL:

                break;
        }

    }

    ReleaseJobsAdapter.ReleaseJobsAdapterCallBack itemClickCallBack = new ReleaseJobsAdapter.ReleaseJobsAdapterCallBack() {
        @Override
        public void onItemClick(JobsEntity entity, int position) {
            Intent intent = new Intent(ReleaseJobListActivity.this,ReleaseJobsActivity.class);
            intent.putExtra("jobsEntity",entity);
            startActivity(intent);
        }

        @Override
        public void onLongItemClick(JobsEntity entity, int position) {
            menuDialog.show(entity);
        }
    };

    MenuDialog.MenuDialogCallBack menuDialogCallBack = new MenuDialog.MenuDialogCallBack() {
        @Override
        public void onMenuItemSelector(int index,Object selectorObj) {
            JobsEntity entity = (JobsEntity) selectorObj;
            Intent intent;
            switch (index){
                case MenuDialog.VIEW:
                case MenuDialog.EDIT:
                    intent = new Intent(ReleaseJobListActivity.this,ReleaseJobsActivity.class);
                    intent.putExtra("jobsEntity",entity);
                    startActivity(intent);
                    break;
                case MenuDialog.DEL:
                    del(entity.getId());
                    break;
            }
            menuDialog.dismiss();
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
