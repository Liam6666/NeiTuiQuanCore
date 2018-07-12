package com.neituiquan.work.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.adapter.ReleaseJobsAdapter;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.dialog.MenuDialog;
import com.neituiquan.entity.ReleaseJobsEntity;
import com.neituiquan.gson.ReleaseJobListModel;
import com.neituiquan.gson.StringModel;
import com.neituiquan.httpEvent.ReleaseJobListEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.view.AutoLoadRecyclerView;
import com.neituiquan.work.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 发布的职位列表
 */

public class ReleaseJobListActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, AutoLoadRecyclerView.OnLoadMoreCallBack, ReleaseJobsAdapter.AdapterCallBack, MenuDialog.MenuDialogCallBack {

    private View releaseJobUI_statusView;
    private ImageView releaseJobUI_backImg;
    private ImageView releaseJobUI_addImg;
    private SmartRefreshLayout releaseJobUI_refreshLayout;
    private AutoLoadRecyclerView releaseJobUI_recyclerView;
    private LinearLayout releaseJobUI_emptyLayout;
    private TextView releaseJobUI_emptyTv;

    private ReleaseJobsAdapter releaseJobsAdapter;

    private LinearLayoutManager linearLayoutManager;

    private static final int INIT = 12392;

    private static final int LOAD_MORE = 3242;

    private static final int DEL = 89441;

    private int pageIndex = 0;

    private MenuDialog menuDialog;

    private static final int REQUEST_CODE = 3122;

    private static final int RESULT_CODE_ADD = 3134;

    private static final int RESULT_CODE_UPDATE = 31232;

    private static final int RESULT_CODE_DEL = 4311;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release_job_list);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        initValues();
        menuDialog = new MenuDialog(this);
        menuDialog.setMenuDialogCallBack(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageIndex = 0;
        releaseJobUI_recyclerView.loadComplete();
        initValues();
    }

    @Override
    public void onLoadMore() {
        pageIndex ++;
        loadValues();
    }

    private void initValues(){
        getLoadingDialog().show();
        linearLayoutManager = new LinearLayoutManager(this);
        releaseJobsAdapter = new ReleaseJobsAdapter(this);
        releaseJobsAdapter.setCallBack(this);
        releaseJobUI_recyclerView.setLayoutManager(linearLayoutManager);
        releaseJobUI_recyclerView.setAdapter(releaseJobsAdapter);
        String url = FinalData.BASE_URL +
                "/findJobsByUserId?userId="+ App.getAppInstance().getUserInfoUtils().getUserId() +"&index="+pageIndex;
        HttpFactory.getHttpUtils().get(url,new ReleaseJobListEventModel(INIT));
    }

    private void loadValues(){
        getLoadingDialog().show();
        String url = FinalData.BASE_URL +
                "/findJobsByUserId?userId="+ App.getAppInstance().getUserInfoUtils().getUserId() +"&index="+pageIndex;
        HttpFactory.getHttpUtils().get(url,new ReleaseJobListEventModel(LOAD_MORE));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getJobsListResult(ReleaseJobListEventModel eventModel){
        getLoadingDialog().dismiss();
        if(eventModel.isSuccess){
            switch (eventModel.eventId){
                case INIT:
                    ReleaseJobListModel model = new Gson().fromJson(eventModel.resultStr,ReleaseJobListModel.class);
                    releaseJobsAdapter.addData(model.data);
                    if(model.data.size() > 0){
                        releaseJobUI_emptyLayout.setVisibility(View.GONE);
                    }else{
                        releaseJobUI_emptyLayout.setVisibility(View.VISIBLE);
                    }
                    releaseJobUI_refreshLayout.finishRefresh();
                    break;
                case LOAD_MORE:
                    ReleaseJobListModel model2 = new Gson().fromJson(eventModel.resultStr,ReleaseJobListModel.class);
                    releaseJobsAdapter.addData(model2.data);
                    if(model2.data.size() < FinalData.PAGE_SIZE){
                        //没有更多
                    }else{
                        releaseJobUI_recyclerView.loadComplete();
                    }
                    break;
                case DEL:
                    StringModel stringModel = new Gson().fromJson(eventModel.resultStr,StringModel.class);
                    if(stringModel.code == 0){
                        releaseJobsAdapter.remove(eventModel.jobsId);
                    }else{
                        ToastUtils.showShort("删除失败");
                    }
                    break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.releaseJobUI_backImg:
                finish();
                break;
            case R.id.releaseJobUI_addImg:
            case R.id.releaseJobUI_emptyTv:
                intent = new Intent(this,ReleaseJobsActivity.class);
                break;
        }
        if(intent != null){
            startActivityForResult(intent,REQUEST_CODE);
        }
    }

    @Override
    public void onItemClick(ReleaseJobsEntity entity, int position) {
        Intent intent = new Intent(this,ReleaseJobsActivity.class);
        intent.putExtra("releaseJobsEntity",entity);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void onMenuItemSelector(int index, Object selectorObj) {
        menuDialog.dismiss();
        Intent intent = null;
        ReleaseJobsEntity entity = (ReleaseJobsEntity)selectorObj;
        switch (index){
            case MenuDialog.VIEW:
                intent = new Intent(this,JobDetailsActivity.class);
                intent.putExtra("jobId",entity.getId());
                break;
            case MenuDialog.EDIT:
                intent = new Intent(this,ReleaseJobsActivity.class);
                intent.putExtra("releaseJobsEntity",entity);
                break;
            case MenuDialog.DEL:
                getLoadingDialog().show();
                String url = FinalData.BASE_URL + "/delJobs?id="+entity.getId();
                ReleaseJobListEventModel eventModel = new ReleaseJobListEventModel(DEL);
                eventModel.jobsId = entity.getId();
                HttpFactory.getHttpUtils().get(url,eventModel);
                break;
        }
        if(intent != null){
            startActivityForResult(intent,REQUEST_CODE);
        }
    }

    @Override
    public void onLongItemClick(ReleaseJobsEntity entity, int position) {
        menuDialog.show(entity);
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        releaseJobUI_statusView.setLayoutParams(params);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CODE_ADD){
            ReleaseJobsEntity entity = (ReleaseJobsEntity) data.getSerializableExtra("releaseJobsEntity");
            releaseJobsAdapter.addData(entity);
        }else if(resultCode == RESULT_CODE_UPDATE){
            ReleaseJobsEntity entity = (ReleaseJobsEntity) data.getSerializableExtra("releaseJobsEntity");
            releaseJobsAdapter.update(entity);
        }else if(resultCode ==  RESULT_CODE_DEL){
            String id = data.getStringExtra("id");
            releaseJobsAdapter.remove(id);
        }
    }

    private void bindViews() {

        releaseJobUI_statusView = (View) findViewById(R.id.releaseJobUI_statusView);
        releaseJobUI_backImg = (ImageView) findViewById(R.id.releaseJobUI_backImg);
        releaseJobUI_addImg = (ImageView) findViewById(R.id.releaseJobUI_addImg);
        releaseJobUI_refreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.releaseJobUI_refreshLayout);
        releaseJobUI_recyclerView = (com.neituiquan.view.AutoLoadRecyclerView) findViewById(R.id.releaseJobUI_recyclerView);
        releaseJobUI_emptyLayout = findViewById(R.id.releaseJobUI_emptyLayout);
        releaseJobUI_emptyTv = findViewById(R.id.releaseJobUI_emptyTv);
        releaseJobUI_backImg.setOnClickListener(this);
        releaseJobUI_addImg.setOnClickListener(this);
        releaseJobUI_emptyTv.setOnClickListener(this);
        releaseJobUI_refreshLayout.setOnRefreshListener(this);
        releaseJobUI_recyclerView.setOnLoadMoreCallBack(this);
    }
}
