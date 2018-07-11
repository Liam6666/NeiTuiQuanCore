package com.neituiquan.work.company;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.neituiquan.FinalData;
import com.neituiquan.adapter.BasePageAdapter;
import com.neituiquan.adapter.ReleaseJobsAdapter;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.entity.CompanyEntity;
import com.neituiquan.entity.CompanyImgEntity;
import com.neituiquan.gson.CompanyModel;
import com.neituiquan.gson.ReleaseJobListModel;
import com.neituiquan.httpEvent.CompanyDetailsEventModel;
import com.neituiquan.httpEvent.GetJobListEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.GlideUtils;
import com.neituiquan.work.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/6/28.
 * <p>
 * email:nice_ohoh@163.com
 */

public class CompanyDetailsActivity extends BaseActivity {

    private AppBarLayout companyDetailsFG_appBarLayout;
    private Banner companyDetailsFG_banner;
    private LinearLayout companyDetailsFG_toolbarLayout;
    private ImageView companyDetailsFG_backImg;
    private TextView companyDetailsFG_titleTv;
    private SlidingTabLayout companyDetailsFG_tabLayout;
    private ViewPager companyDetailsFG_viewPager;
    private View companyDetailsFG_statusView;

    private List<View> viewList = new ArrayList<>();

    private View itemView1;

    private RecyclerView itemView2ListView;

    private BasePageAdapter basePageAdapter;

    private ReleaseJobsAdapter releaseJobsAdapter;

    private String companyId = null;

    private static final int INIT_JOBS = 11;


    private TextView view_companyNameTv;
    private TextView view_provinceTv;
    private TextView view_cityTv;
    private TextView view_addressTv;
    private TextView view_creationTimeTv;
    private TextView view_peopleNumTv;
    private TextView view_linkUrlTv;
    private TextView view_introduceTv;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_details);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        companyId = getIntent().getStringExtra("companyId");
        if(companyId == null){

        }else{
            String url = FinalData.BASE_URL + "/getCompany?id="+companyId;
            HttpFactory.getHttpUtils().get(url,new CompanyDetailsEventModel());
            initPager();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void companyDetailsResult(CompanyDetailsEventModel eventModel){
        if(eventModel.isSuccess){
            CompanyModel companyModel = new Gson().fromJson(eventModel.resultStr,CompanyModel.class);
            List<String> imgList = new ArrayList<>();
            for(CompanyImgEntity imgEntity : companyModel.data.getImgList()){
                imgList.add(imgEntity.getImgUrl());
            }
            initBanner(imgList);
            initBaseInfo(companyModel.data);
        }
    }

    private void initBaseInfo(CompanyEntity entity){
        view_companyNameTv.setText(entity.getCompanyName());
        view_provinceTv.setText(entity.getProvince());
        view_cityTv.setText(entity.getCity());
        view_addressTv.setText(entity.getAddress());
        view_creationTimeTv.setText(entity.getCreationTime());
        view_peopleNumTv.setText(entity.getPeopleNum());
        view_linkUrlTv.setText(entity.getLinkUrl());
        view_introduceTv.setText(entity.getIntroduce());
    }


    private void initBanner(List<String> imgList){
        //设置banner样式
        companyDetailsFG_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        companyDetailsFG_banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        companyDetailsFG_banner.setImages(imgList);
        //设置banner动画效果
        companyDetailsFG_banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        companyDetailsFG_banner.isAutoPlay(true);
        //设置轮播时间
        companyDetailsFG_banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        companyDetailsFG_banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        companyDetailsFG_banner.start();
    }

    private void initPager(){
        String[] tabs = new String[]{
                "基本信息","招聘职位","公司留言"
        };
        viewList.add(View.inflate(this,R.layout.view_company_base_info,null));
        viewList.add(new RecyclerView(this));
        viewList.add(new View(this));
        itemView1 = viewList.get(0);
        itemView2ListView = (RecyclerView) viewList.get(1);
        basePageAdapter = new BasePageAdapter(this,viewList);
        companyDetailsFG_viewPager.setAdapter(basePageAdapter);
        companyDetailsFG_viewPager.setOffscreenPageLimit(viewList.size());
        companyDetailsFG_tabLayout.setViewPager(companyDetailsFG_viewPager,tabs);
        bindViewsItem1();
        initJobsList();
    }

    private void initJobsList(){
        itemView2ListView.setLayoutManager(new LinearLayoutManager(this));
        String url = FinalData.BASE_URL + "/findJobsByCompanyId?companyId="+companyId;
        HttpFactory.getHttpUtils().get(url,new GetJobListEventModel(INIT_JOBS));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getJobsResult(GetJobListEventModel eventModel){
        if(eventModel.isSuccess){
            switch (eventModel.eventId){
                case INIT_JOBS:
                    ReleaseJobListModel model = new Gson().fromJson(eventModel.resultStr,ReleaseJobListModel.class);
                    if(releaseJobsAdapter == null){
                        releaseJobsAdapter = new ReleaseJobsAdapter(this,model.data);
                        itemView2ListView.setAdapter(releaseJobsAdapter);
                    }else{
                        releaseJobsAdapter.refresh(model.data);
                    }
                    break;
            }
        }
    }


    private void initStatusBar() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int barHeight = BarUtils.getStatusBarHeight();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, barHeight);
                companyDetailsFG_statusView.setLayoutParams(params);
            }
        });

    }

    private void bindViews() {
        companyDetailsFG_statusView = findViewById(R.id.companyDetailsFG_statusView);
        companyDetailsFG_appBarLayout = (android.support.design.widget.AppBarLayout) findViewById(R.id.companyDetailsFG_appBarLayout);
        companyDetailsFG_banner = (com.youth.banner.Banner) findViewById(R.id.companyDetailsFG_banner);
        companyDetailsFG_toolbarLayout = (LinearLayout) findViewById(R.id.companyDetailsFG_toolbarLayout);
        companyDetailsFG_backImg = (ImageView) findViewById(R.id.companyDetailsFG_backImg);
        companyDetailsFG_titleTv = (TextView) findViewById(R.id.companyDetailsFG_titleTv);
        companyDetailsFG_tabLayout = (com.flyco.tablayout.SlidingTabLayout) findViewById(R.id.companyDetailsFG_tabLayout);
        companyDetailsFG_viewPager = (android.support.v4.view.ViewPager) findViewById(R.id.companyDetailsFG_viewPager);
    }

    private void bindViewsItem1() {
        view_companyNameTv = itemView1.findViewById(R.id.view_companyNameTv);
        view_provinceTv = itemView1.findViewById(R.id.view_provinceTv);
        view_cityTv = itemView1.findViewById(R.id.view_cityTv);
        view_addressTv = itemView1.findViewById(R.id.view_addressTv);
        view_creationTimeTv = itemView1.findViewById(R.id.view_creationTimeTv);
        view_peopleNumTv = itemView1.findViewById(R.id.view_peopleNumTv);
        view_linkUrlTv = itemView1.findViewById(R.id.view_linkUrlTv);
        view_introduceTv = itemView1.findViewById(R.id.view_introduceTv);
    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            GlideUtils.load((String)path,imageView);
        }
    }
}
