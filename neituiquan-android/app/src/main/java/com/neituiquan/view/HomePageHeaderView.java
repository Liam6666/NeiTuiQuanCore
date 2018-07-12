package com.neituiquan.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.neituiquan.FinalData;
import com.neituiquan.entity.BannerEntity;
import com.neituiquan.gson.BannerModel;
import com.neituiquan.httpEvent.BannerEventModel;
import com.neituiquan.httpEvent.HeaderViewEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.GlideUtils;
import com.neituiquan.work.CitySelectorActivity;
import com.neituiquan.work.MainActivity;
import com.neituiquan.work.R;
import com.neituiquan.work.WebActivity;
import com.neituiquan.work.company.CompanyDetailsActivity;
import com.neituiquan.work.fragment.HomePageFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/7/6.
 * <p>
 * email:nice_ohoh@163.com
 *
 * tv.setSelected(true);   不设置这个属性,字体不会开始滚动
 *
 */

public class HomePageHeaderView extends LinearLayout{


    private Banner view_banner;
    private LinearLayout view_locationLayout;
    private TextView view_locationTv;
    private LinearLayout view_itemLayout1;
    private LinearLayout view_itemLayout2;
    private LinearLayout view_itemLayout3;
    private LinearLayout view_itemLayout4;
    private TextView view_sysNotifyTv;


    public HomePageHeaderView(Context context) {
        super(context);
        initView();
    }

    public HomePageHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        EventBus.getDefault().register(this);
        View.inflate(getContext(), R.layout.view_home_page_header,this);
        bindViews();
        int barHeight = BarUtils.getStatusBarHeight();
        view_locationLayout.setY(barHeight + 15);
        String bannerUrl = FinalData.BASE_URL + "/getAllBanner";
        HttpFactory.getHttpUtils().get(bannerUrl,new BannerEventModel());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAllBannerResult(BannerEventModel eventModel){
        if(eventModel.isSuccess){
            BannerModel model = new Gson().fromJson(eventModel.resultStr,BannerModel.class);

            List<String> imgList = new ArrayList<>();
            for(BannerEntity entity : model.data){
                imgList.add(entity.getImgUrl());
            }
            initBanner(imgList,model.data);
        }
    }

    public void initBanner(List<String> imgList, final List<BannerEntity> bannerEntityList){
        //设置banner样式
        view_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        view_banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        view_banner.setImages(imgList);
        //设置banner动画效果
        view_banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        view_banner.isAutoPlay(true);
        //设置轮播时间
        view_banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        view_banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        try {
            view_banner.start();
        }catch (IllegalArgumentException i){

        }
        view_banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerEntity entity = bannerEntityList.get(position);
                String linkUrl = entity.getLinkUrl();
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("linkUrl",linkUrl);
                getContext().startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void destroyView(HeaderViewEventModel eventModel){
        view_banner.stopAutoPlay();
    }

    private void bindViews() {

        view_banner = (com.youth.banner.Banner) findViewById(R.id.view_banner);
        view_locationLayout = (LinearLayout) findViewById(R.id.view_locationLayout);
        view_locationTv = (TextView) findViewById(R.id.view_locationTv);
        view_itemLayout1 = (LinearLayout) findViewById(R.id.view_itemLayout1);
        view_itemLayout2 = (LinearLayout) findViewById(R.id.view_itemLayout2);
        view_itemLayout3 = (LinearLayout) findViewById(R.id.view_itemLayout3);
        view_itemLayout4 = (LinearLayout) findViewById(R.id.view_itemLayout4);
        view_sysNotifyTv = (TextView) findViewById(R.id.view_sysNotifyTv);
    }


    public TextView getLocationTv() {
        return view_locationTv;
    }

    static class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
