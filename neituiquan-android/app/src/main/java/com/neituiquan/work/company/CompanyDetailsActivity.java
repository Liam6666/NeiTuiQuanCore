package com.neituiquan.work.company;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.neituiquan.base.BaseActivity;
import com.neituiquan.entity.CompanyImgEntity;
import com.neituiquan.gson.CompanyModel;
import com.neituiquan.httpEvent.CompanyDetailsEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;
import com.neituiquan.work.fragment.HomePageFragment;
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

    private BasePageAdapter basePageAdapter;

    private String companyId = null;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_details);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        initPager();
        companyId = getIntent().getStringExtra("companyId");
        if(companyId == null){

        }else{
            String url = FinalData.BASE_URL + "/getCompany?id="+companyId;
            HttpFactory.getHttpUtils().get(url,new CompanyDetailsEventModel());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void companyDetailsResult(CompanyDetailsEventModel eventModel){
        if(eventModel.isSuccess){
            CompanyModel companyModel = new Gson().fromJson(eventModel.resultStr,CompanyModel.class);
            List<String> imgList = new ArrayList<>();
            for(CompanyImgEntity imgEntity : companyModel.data.getImgList()){
                imgList.add(FinalData.IMG + imgEntity.getImgUrl());
            }
            initBanner(imgList);
            for(String s : imgList){
                Log.e("imgList",s);
            }
        }
    }


    private void initBanner(List<String> imgList){
        //设置banner样式
        companyDetailsFG_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        companyDetailsFG_banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        companyDetailsFG_banner.setImages(imgList);
        //设置banner动画效果
        companyDetailsFG_banner.setBannerAnimation(Transformer.Accordion);
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
        viewList.add(new View(this));
        viewList.add(new View(this));
        viewList.add(new View(this));
        basePageAdapter = new BasePageAdapter(this,viewList);
        companyDetailsFG_viewPager.setAdapter(basePageAdapter);
        companyDetailsFG_viewPager.setOffscreenPageLimit(viewList.size());
        companyDetailsFG_tabLayout.setViewPager(companyDetailsFG_viewPager,tabs);
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

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
