package com.neituiquan.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by Augustine on 2018/6/27.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 定位工具类
 */

public class PositionUtils {

    private AMapLocationClient locationClient;

    private AMapLocationClientOption clientOption;

    public void initGaoDeLocation(Context context,AMapLocationListener locationListener){
        locationClient = new AMapLocationClient(context);
        locationClient.setLocationListener(locationListener);
        clientOption = new AMapLocationClientOption();

        clientOption.setLocationCacheEnable(false);
        clientOption.setNeedAddress(true);
        clientOption.setOnceLocation(true);
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        clientOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != locationClient){
            locationClient.setLocationOption(clientOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            locationClient.stopLocation();
            locationClient.startLocation();
        }
    }

    public AMapLocationClient getLocationClient() {
        return locationClient;
    }

    public AMapLocationClientOption getClientOption() {
        return clientOption;
    }
}
