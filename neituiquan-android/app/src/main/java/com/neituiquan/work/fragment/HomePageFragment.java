package com.neituiquan.work.fragment;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.neituiquan.FinalData;
import com.neituiquan.adapter.BasePageAdapter;
import com.neituiquan.adapter.HomePageJobAdapter;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.entity.BannerEntity;
import com.neituiquan.gson.BannerModel;
import com.neituiquan.gson.JobsModel;
import com.neituiquan.httpEvent.BannerEventModel;
import com.neituiquan.httpEvent.GetJobListEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.PositionUtils;
import com.neituiquan.view.AppBarStateChangeListener;
import com.neituiquan.view.AutoLoadRecyclerView;
import com.neituiquan.work.CitySelectorActivity;
import com.neituiquan.work.MainActivity;
import com.neituiquan.work.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/6/15.
 * <p>
 * email:nice_ohoh@163.com
 */

public class HomePageFragment extends BaseFragment implements AutoLoadRecyclerView.OnLoadMoreCallBack, OnRefreshListener, View.OnClickListener {

    public static final String LOCATION_TAG = "GaoDeMap";

    public static HomePageFragment newInstance() {

        Bundle args = new Bundle();

        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SmartRefreshLayout homeFG_refreshLayout;
    private Banner homeFG_banner;
    private SlidingTabLayout homeFG_tabLayout;
    private ViewPager homeFG_viewPager;
    private View homeFG_statusView;
    private AppBarLayout homeFG_appBarLayout;
    private Toolbar homeFG_toolbar;
    private CollapsingToolbarLayout homeFG_collapsingLayout;
    private LinearLayout homeFG_searchLayout;
    private TextView homeFG_locationTv;

    private BasePageAdapter basePageAdapter;

    private int index;

    private String currentCity;

    private String currentTitle;

    private static final int INIT_JOBS = 0;

    private static final int LOAD_MORE = 1;

    private AutoLoadRecyclerView homeFG_recyclerView;

    private HomePageJobAdapter homePageJobAdapter;

    private PositionUtils positionUtils;

    private static final int START_TO_SELECTOR_CITY = 323;

    private static final int SELECTOR_CITY_RESULT_CODE = 333;

    private AMapLocationListener locationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(FinalData.DEBUG){
                Log.e(LOCATION_TAG, aMapLocation.getErrorCode()+"");
                Log.e(LOCATION_TAG, aMapLocation.getErrorInfo());
                Log.e(LOCATION_TAG, aMapLocation.getLatitude()+"");
                Log.e(LOCATION_TAG, aMapLocation.getLongitude()+"");
                Log.e(LOCATION_TAG, aMapLocation.getAddress());
                Log.e(LOCATION_TAG, aMapLocation.getCountry());
                Log.e(LOCATION_TAG, aMapLocation.getProvince());
                Log.e(LOCATION_TAG, aMapLocation.getCity());
                Log.e(LOCATION_TAG, aMapLocation.getDistrict());
                Log.e(LOCATION_TAG, aMapLocation.getStreet());
            }
            //定位成功
            if(aMapLocation.getErrorCode() == 0){
                currentCity = aMapLocation.getCity();
                currentTitle = "";
            }else{
                currentCity = "北京市";
                currentTitle = "";
            }
            homeFG_locationTv.setText(currentCity);
            String url = FinalData.BASE_URL +
                    "/getJobsList?city="+currentCity+"&title="+currentTitle+"&index="+index;
            loadJobsList(url);
        }
    };



    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_home_page, null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        initViewPager();
        changedSearchView();
        loadBanner();
        positionUtils = new PositionUtils();
        positionUtils.initGaoDeLocation(getContext(),locationListener);
        homeFG_refreshLayout.setOnRefreshListener(this);
    }

    private void loadBanner(){
        String url = FinalData.BASE_URL + "/getAllBanner";
        HttpFactory.getHttpUtils().get(url, new BannerEventModel());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bannerResponse(BannerEventModel eventModel) {
        if (eventModel.isSuccess) {
            List<String> imgList = new ArrayList<>();
            BannerModel bannerModel = new Gson().fromJson(eventModel.resultStr, BannerModel.class);
            for (BannerEntity entity : bannerModel.data) {
                imgList.add(entity.getImgUrl());
            }
            initBanner(imgList);
        }
    }

    private void initBanner(List<String> imgList) {
        //设置banner样式
        homeFG_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        homeFG_banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        homeFG_banner.setImages(imgList);
        //设置banner动画效果
        homeFG_banner.setBannerAnimation(Transformer.Accordion);
        //设置自动轮播，默认为true
        homeFG_banner.isAutoPlay(true);
        //设置轮播时间
        homeFG_banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        homeFG_banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        homeFG_banner.start();
    }

    private void initViewPager() {
        List<View> viewList = new ArrayList<>();
        viewList.add(View.inflate(getContext(), R.layout.item_home_page, null));
        String[] tabs = new String[]{
                "职位列表"
        };
        basePageAdapter = new BasePageAdapter(getContext(), viewList);
        homeFG_viewPager.setAdapter(basePageAdapter);
        homeFG_viewPager.setOffscreenPageLimit(viewList.size());
        homeFG_tabLayout.setViewPager(homeFG_viewPager,tabs);
        homeFG_recyclerView = viewList.get(0).findViewById(R.id.item_recyclerView);
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ((MainActivity)getContext()).getLoadingDialog().show();
        index = 0;
        homeFG_recyclerView.loadComplete();
        positionUtils.initGaoDeLocation(getContext(),locationListener);
    }

    @Override
    public void onLoadMore() {
        index ++;
        String url = FinalData.BASE_URL +
                "/getJobsList?city="+currentCity+"&title="+currentTitle+"&index="+index;
        HttpFactory.getHttpUtils().get(url,new GetJobListEventModel(LOAD_MORE));
    }

    private void loadJobsList(String url){
        HttpFactory.getHttpUtils().get(url,new GetJobListEventModel(INIT_JOBS));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getJobsResult(GetJobListEventModel eventModel){
        ((MainActivity)getContext()).getLoadingDialog().dismiss();
        if(!eventModel.isSuccess){
            ToastUtils.showShort(eventModel.errorMsg);
            homeFG_refreshLayout.finishRefresh();
            return;
        }
        JobsModel jobsModel = new Gson().fromJson(eventModel.resultStr,JobsModel.class);
        switch (eventModel.eventId){
            case INIT_JOBS:
                if(homePageJobAdapter == null){
                    homeFG_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    homeFG_recyclerView.setOnLoadMoreCallBack(this);
                    homePageJobAdapter = new HomePageJobAdapter(getContext(),jobsModel.data);
                    homeFG_recyclerView.setAdapter(homePageJobAdapter);
                }else{
                    homePageJobAdapter.refresh(jobsModel.data);
                    homeFG_refreshLayout.finishRefresh();
                }
                break;
            case LOAD_MORE:
                if(jobsModel.dataTotalCount == 0){
                    //加载完毕
                    index --;
                    return;
                }
                homePageJobAdapter.addNewData(jobsModel.data);
                homeFG_recyclerView.loadComplete();
                break;
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.homeFG_locationTv:
                startActivityForResult(new Intent(getContext(), CitySelectorActivity.class),START_TO_SELECTOR_CITY);
                break;
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
                    homeFG_locationTv.setText(city);
                    currentCity = city;
                    ((MainActivity)getContext()).getLoadingDialog().show();
                    index = 0;
                    homeFG_recyclerView.loadComplete();
                    String url = FinalData.BASE_URL +
                            "/getJobsList?city="+currentCity+"&title="+currentTitle+"&index="+index;
                    loadJobsList(url);
                }
            }
        }

    }

    private void initStatusBar() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int barHeight = BarUtils.getStatusBarHeight();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, barHeight);
                homeFG_statusView.setLayoutParams(params);
                CollapsingToolbarLayout.LayoutParams appBarParams = (CollapsingToolbarLayout.LayoutParams) homeFG_toolbar.getLayoutParams();
                appBarParams.height += barHeight;
                homeFG_toolbar.setLayoutParams(appBarParams);
            }
        });

    }

    private void changedSearchView() {
        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        final int PAGE_COLOR_ONE = ContextCompat.getColor(getContext(),R.color.transparent);
        final int PAGE_COLOR_TWO = ContextCompat.getColor(getContext(),R.color.themeColor);
        homeFG_appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                } else if (state == State.COLLAPSED) {
                    //折叠状态

                } else {
                    //中间状态
                }
            }

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                super.onOffsetChanged(appBarLayout, verticalOffset);
//                appBarLayout.getTotalScrollRange()方法获取最大偏移值。
//                Log.e("verticalOffset",""+verticalOffset);
//                Log.e("verticalOffset",""+appBarLayout.getTotalScrollRange());
                float value = (float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange();
//                Log.e("verticalOffset","value:"+value);
                int color = (int) argbEvaluator.evaluate(value,PAGE_COLOR_ONE ,PAGE_COLOR_TWO);
                homeFG_searchLayout.setBackgroundColor(color);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        positionUtils.getLocationClient().stopLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        positionUtils.getLocationClient().onDestroy();
    }

    private void bindViews() {
        homeFG_refreshLayout = findViewById(R.id.homeFG_refreshLayout);
        homeFG_banner = findViewById(R.id.homeFG_banner);
        homeFG_tabLayout = findViewById(R.id.homeFG_tabLayout);
        homeFG_viewPager = findViewById(R.id.homeFG_viewPager);
        homeFG_statusView = findViewById(R.id.homeFG_statusView);
        homeFG_appBarLayout = findViewById(R.id.homeFG_appBarLayout);
        homeFG_toolbar = findViewById(R.id.homeFG_toolbar);
        homeFG_collapsingLayout = findViewById(R.id.homeFG_collapsingLayout);
        homeFG_searchLayout = findViewById(R.id.homeFG_searchLayout);
        homeFG_locationTv = findViewById(R.id.homeFG_locationTv);

        homeFG_locationTv.setOnClickListener(this);
    }


    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

}
