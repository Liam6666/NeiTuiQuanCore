package com.neituiquan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.entity.UserEntity;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.net.RequestEventModel;

import java.io.Serializable;

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

    public static final String LOCATION_TAG = "PositionUtils";

    private static final String FILE_NAME = "location-info";

    private static final String KEY_NAME = "location";

    private SharedPreferences sharedPreferences;

    private long nextUpdateTime = -1;//下一次更新时间

    private long updateTime = FinalData.LOCATION_TIME_OUT;//超时时间 24小时更新一次

    public void initGaoDeLocation(Context context, final PositionCallBack callBack){
        sharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        LocationEntity cacheLocation = getCachePosition();
        if(cacheLocation == null){//没有缓存信息
            startLocation(context,callBack);
            return;
        }
        boolean timeOut = System.currentTimeMillis() > Long.parseLong(cacheLocation.getNextUpdateTime());
        boolean locationErr = !cacheLocation.getErrorCode().equals("0");
        if(timeOut || locationErr){//定位失败或者缓存超时
            startLocation(context,callBack);
            return;
        }
        callBack.mapLocation(cacheLocation);
        upLoadLocationInfo(cacheLocation);
    }

    public AMapLocationListener setCachePosition(final PositionCallBack callBack){
        return new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                LocationEntity locationEntity = new LocationEntity();
                locationEntity.setTime(System.currentTimeMillis()+"");
                locationEntity.setErrorCode(aMapLocation.getErrorCode()+"");
                locationEntity.setErrorInfo(aMapLocation.getErrorInfo());
                locationEntity.setLatitude(aMapLocation.getLatitude()+"");
                locationEntity.setLongitude(aMapLocation.getLongitude()+"");
                locationEntity.setAddress(aMapLocation.getAddress());
                locationEntity.setCountry(aMapLocation.getCountry());
                locationEntity.setProvince(aMapLocation.getProvince());
                locationEntity.setCity(aMapLocation.getCity());
                locationEntity.setDistrict(aMapLocation.getDistrict());
                locationEntity.setStreet(aMapLocation.getStreet());

                nextUpdateTime = Long.parseLong(locationEntity.getTime()) + updateTime;
                locationEntity.setNextUpdateTime(nextUpdateTime+"");

                sharedPreferences.edit()
                        .putString(KEY_NAME,new Gson().toJson(locationEntity))
                        .commit();

                callBack.mapLocation(locationEntity);

                upLoadLocationInfo(locationEntity);

                if(FinalData.DEBUG){
                    Log.e(LOCATION_TAG, locationEntity.getErrorCode()+"");
                    Log.e(LOCATION_TAG, locationEntity.getErrorInfo());
                    Log.e(LOCATION_TAG, locationEntity.getLatitude()+"");
                    Log.e(LOCATION_TAG, locationEntity.getLongitude()+"");
                    Log.e(LOCATION_TAG, locationEntity.getAddress());
                    Log.e(LOCATION_TAG, locationEntity.getCountry());
                    Log.e(LOCATION_TAG, locationEntity.getProvince());
                    Log.e(LOCATION_TAG, locationEntity.getCity());
                    Log.e(LOCATION_TAG, locationEntity.getDistrict());
                    Log.e(LOCATION_TAG, locationEntity.getStreet());
                    Log.e(LOCATION_TAG, "定位时间："+Millis2Date.millis2Date(locationEntity.getTime()));
                    Log.e(LOCATION_TAG, "过期时间："+Millis2Date.millis2Date(locationEntity.getNextUpdateTime()));
                }
            }
        };
    }

    public LocationEntity getCachePosition(){
        String json = sharedPreferences.getString(KEY_NAME,"");
        if(StringUtils.isEmpty(json)){
            return null;
        }
        return new Gson().fromJson(json,LocationEntity.class);
    }

    private void startLocation(Context context,PositionCallBack callBack){
        locationClient = new AMapLocationClient(context);
        locationClient.setLocationListener(setCachePosition(callBack));
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

    private void upLoadLocationInfo(LocationEntity locationEntity){
        if(App.getAppInstance().getUserInfoUtils().getUserInfo() == null){
            return;
        }
        UserEntity userEntity = App.getAppInstance().getUserInfoUtils().getUserInfo().data;
        userEntity.setLatitude(locationEntity.getLatitude());
        userEntity.setLongitude(locationEntity.getLongitude());
        userEntity.setAccuracy(locationEntity.getAddress());
        userEntity.setProvince(locationEntity.getProvince());
        userEntity.setCity(locationEntity.getCity());
        userEntity.setDistrict(locationEntity.getDistrict());
        HttpFactory.getHttpUtils().post(new Gson().toJson(userEntity),FinalData.BASE_URL +"/updateLocation",new RequestEventModel());
    }

    public void clearCache(){
        sharedPreferences.edit().remove(KEY_NAME).commit();
    }

    public AMapLocationClient getLocationClient() {
        return locationClient;
    }

    public AMapLocationClientOption getClientOption() {
        return clientOption;
    }

    public static void clearLocationInfo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(KEY_NAME).commit();
    }

    public interface PositionCallBack {

        public void mapLocation(LocationEntity locationEntity);

    }

    public static class LocationEntity implements Serializable {

        private String time;
        private String nextUpdateTime;
        private String errorCode;
        private String errorInfo;
        private String latitude;
        private String longitude;
        private String address;
        private String country;
        private String province;
        private String city;
        private String district;
        private String street;

        public String getNextUpdateTime() {
            return nextUpdateTime;
        }

        public void setNextUpdateTime(String nextUpdateTime) {
            this.nextUpdateTime = nextUpdateTime;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(String errorInfo) {
            this.errorInfo = errorInfo;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }
}
