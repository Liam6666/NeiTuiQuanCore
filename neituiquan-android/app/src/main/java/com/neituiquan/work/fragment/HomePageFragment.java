package com.neituiquan.work.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.neituiquan.FinalData;
import com.neituiquan.adapter.HomePageAdapter;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.entity.BannerEntity;
import com.neituiquan.gson.BannerModel;
import com.neituiquan.eventModel.BannerEventModel;
import com.neituiquan.net.HttpUtils;
import com.neituiquan.work.MainActivity;
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
 * Created by Augustine on 2018/6/15.
 * <p>
 * email:nice_ohoh@163.com
 */

public class HomePageFragment extends BaseFragment {

    public static HomePageFragment newInstance() {

        Bundle args = new Bundle();

        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Banner homeFG_banner;
    private SlidingTabLayout homeFG_tabLayout;
    private ViewPager homeFG_viewPager;
    private View homeFG_statusView;
    private AppBarLayout homeFG_appBarLayout;

    private HttpUtils httpUtils;

    private HomePageAdapter homePageAdapter;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_home_page,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        httpUtils = ((MainActivity)getContext()).getHttpUtils();
        String url = FinalData.UAT + "/getAllBanner";
        httpUtils.get(url,new BannerEventModel());
        initViewPager();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bannerResponse(BannerEventModel eventModel){
        if(eventModel.isSuccess){
            List<String> imgList = new ArrayList<>();
            BannerModel bannerModel = new Gson().fromJson(eventModel.resultStr,BannerModel.class);
            for(BannerEntity entity : bannerModel.data){
                imgList.add(entity.getImgUrl());
            }
            initBanner(imgList);
        }
    }

    private void initBanner(List<String> imgList){
        //设置banner样式
        homeFG_banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        homeFG_banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        homeFG_banner.setImages(imgList);
        //设置banner动画效果
        homeFG_banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        homeFG_banner.isAutoPlay(true);
        //设置轮播时间
        homeFG_banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        homeFG_banner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        homeFG_banner.start();
    }

    private void initViewPager(){
        List<View> viewList = new ArrayList<>();
        viewList.add(View.inflate(getContext(),R.layout.item_home_page,null));
        homePageAdapter = new HomePageAdapter(getContext(),viewList);
        homeFG_viewPager.setAdapter(homePageAdapter);
        homeFG_viewPager.setOffscreenPageLimit(viewList.size());
        homeFG_tabLayout.setViewPager(homeFG_viewPager,new String[]{"item1"});
        RecyclerView recyclerView = viewList.get(0).findViewById(R.id.item_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(View.inflate(getContext(),R.layout.item_tv,null));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
    }

    private void initStatusBar(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int barHeight = BarUtils.getStatusBarHeight();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
                homeFG_statusView.setLayoutParams(params);
                CoordinatorLayout.LayoutParams appBarParams = (CoordinatorLayout.LayoutParams) homeFG_appBarLayout.getLayoutParams();
                appBarParams.height += barHeight;
                homeFG_appBarLayout.setLayoutParams(appBarParams);
            }
        });

    }

    private void bindViews() {
        homeFG_banner = findViewById(R.id.homeFG_banner);
        homeFG_tabLayout = findViewById(R.id.homeFG_tabLayout);
        homeFG_viewPager = findViewById(R.id.homeFG_viewPager);
        homeFG_statusView = findViewById(R.id.homeFG_statusView);
        homeFG_appBarLayout = findViewById(R.id.homeFG_appBarLayout);
    }

    class GlideImageLoader extends ImageLoader{

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
