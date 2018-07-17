package com.neituiquan.work.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.FinalData;
import com.neituiquan.adapter.HomePageAdapter;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.dialog.HomePageMoreDialog;
import com.neituiquan.entity.JobListEntity;
import com.neituiquan.gson.HomePageJobListModel;
import com.neituiquan.httpEvent.GetJobListEventModel;
import com.neituiquan.httpEvent.HeaderViewEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.PositionUtils;
import com.neituiquan.view.AutoLoadRecyclerView;
import com.neituiquan.work.widgets.CitySelectorActivity;
import com.neituiquan.work.R;
import com.neituiquan.work.jobs.JobDetailsActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by Augustine on 2018/6/15.
 * <p>
 * email:nice_ohoh@163.com
 */

public class HomePageFragment extends BaseFragment implements OnRefreshListener, AutoLoadRecyclerView.OnLoadMoreCallBack, HomePageAdapter.HomePageAdapterCallBack {

    public static HomePageFragment newInstance() {

        Bundle args = new Bundle();

        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SmartRefreshLayout homePageUI_refreshLayout;
    private AutoLoadRecyclerView homePageUI_recyclerView;

    private PositionUtils positionUtils;

    private String city;

    private String title;

    private int pageIndex;

    private static final int INIT_LIST = 8573;

    private static final int REFRESH = 3842;

    private static final int LOAD_MORE = 39846;

    private HomePageAdapter homePageAdapter;

    private LinearLayoutManager linearLayoutManager;


    private static final int START_TO_SELECTOR_CITY = 323;

    private static final int SELECTOR_CITY_RESULT_CODE = 333;

    private TextView locationTv;


    private PositionUtils.PositionCallBack positionCallBack = new PositionUtils.PositionCallBack() {
        @Override
        public void mapLocation(PositionUtils.LocationEntity locationEntity) {
            if(locationEntity.getErrorCode().equals("0")){
                city = locationEntity.getCity();
                title = "";
                pageIndex = 0;
            }else{
                city = "北京市";
                title = "";
                pageIndex = 0;
                ToastUtils.showShort("定位失败，请手动切换位置信息");
            }
            initValues();
        }
    };

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_home_page, null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        positionUtils = new PositionUtils();
        positionUtils.initGaoDeLocation(getContext(),positionCallBack);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageIndex = 0;
        String url = FinalData.BASE_URL + "/getJobsList?city="+city+"&title="+title+"&index="+pageIndex;
        HttpFactory.getHttpUtils().get(url,new GetJobListEventModel(REFRESH));
        homePageUI_recyclerView.loadComplete();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        String url = FinalData.BASE_URL + "/getJobsList?city="+city+"&title="+title+"&index="+pageIndex;
        HttpFactory.getHttpUtils().get(url,new GetJobListEventModel(LOAD_MORE));
    }

    @Override
    public void onEmptyClick() {
        pageIndex = 0;
        String url = FinalData.BASE_URL + "/getJobsList?city="+city+"&title="+title+"&index="+pageIndex;
        HttpFactory.getHttpUtils().get(url,new GetJobListEventModel(REFRESH));
        homePageUI_recyclerView.loadComplete();

    }

    @Override
    public void onItemClick(JobListEntity entity, int position) {
        Intent intent = new Intent(getContext(), JobDetailsActivity.class);
        intent.putExtra("jobId",entity.getJobsId());
        startActivity(intent);
    }

    @Override
    public void onItemMoreClick(JobListEntity entity, int position) {
        HomePageMoreDialog dialog = new HomePageMoreDialog(getContext());
        View itemView = linearLayoutManager.findViewByPosition(position);
        dialog.show((int)itemView.getY());
    }


    @Override
    public void onLocationClick(TextView locationTv) {
        this.locationTv = locationTv;
        startActivityForResult(new Intent(getContext(), CitySelectorActivity.class),START_TO_SELECTOR_CITY);
    }

    private void initValues(){

        String url = FinalData.BASE_URL + "/getJobsList?city="+city+"&title="+title+"&index="+pageIndex;
        HttpFactory.getHttpUtils().get(url,new GetJobListEventModel(INIT_LIST));

        homePageAdapter = new HomePageAdapter(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());
        homePageUI_recyclerView.setLayoutManager(linearLayoutManager);
        homePageUI_recyclerView.setAdapter(homePageAdapter);
        homePageAdapter.setCity(city);
        homePageAdapter.setCallBack(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getJobsResult(GetJobListEventModel eventModel){
        switch (eventModel.eventId){
            case INIT_LIST:
                if(eventModel.isSuccess){
                    HomePageJobListModel model = new Gson().fromJson(eventModel.resultStr,HomePageJobListModel.class);
                    model.data.add(0,new JobListEntity(FinalData.ITEM_BANNER));
                    if(model.data.size() == 1){
                        model.data.add(new JobListEntity(FinalData.ITEM_EMPTY));
                    }
                    homePageAdapter.addList(model.data);
                }
                break;
            case REFRESH:
                if(eventModel.isSuccess){
                    HomePageJobListModel model = new Gson().fromJson(eventModel.resultStr,HomePageJobListModel.class);
                    model.data.add(0,new JobListEntity(FinalData.ITEM_BANNER));
                    if(model.data.size() == 1){
                        model.data.add(new JobListEntity(FinalData.ITEM_EMPTY));
                    }
                    homePageAdapter.refresh(model.data);
                    homePageUI_refreshLayout.finishRefresh(true);
                }else{
                    homePageUI_refreshLayout.finishRefresh(false);
                }
                break;
            case LOAD_MORE:
                if(eventModel.isSuccess){
                    HomePageJobListModel model = new Gson().fromJson(eventModel.resultStr,HomePageJobListModel.class);
                    homePageAdapter.addList(model.data);
                    if(model.data.size() > FinalData.PAGE_SIZE){
                        homePageUI_recyclerView.loadComplete();
                    }else{
                        //加载完毕
                    }
                }
                break;
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id){

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == SELECTOR_CITY_RESULT_CODE){
            if(requestCode == START_TO_SELECTOR_CITY){
                if(data != null){
                    String province = data.getStringExtra("province");
                    String city = data.getStringExtra("city");
                    locationTv.setText(city);
                    HomePageFragment.this.city = city;
                    pageIndex = 0;
                    String url = FinalData.BASE_URL + "/getJobsList?city="+city+"&title="+title+"&index="+pageIndex;
                    HttpFactory.getHttpUtils().get(url,new GetJobListEventModel(REFRESH));
                    homePageUI_recyclerView.loadComplete();
                }
            }
        }

    }

    @Override
    public void onDetach() {
        EventBus.getDefault().post(new HeaderViewEventModel());
        super.onDetach();
    }

    private void bindViews() {

        homePageUI_refreshLayout = findViewById(R.id.homePageUI_refreshLayout);
        homePageUI_recyclerView = findViewById(R.id.homePageUI_recyclerView);

        homePageUI_refreshLayout.setOnRefreshListener(this);
        homePageUI_recyclerView.setOnLoadMoreCallBack(this);
    }

}
