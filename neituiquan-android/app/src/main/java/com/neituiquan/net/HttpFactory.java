package com.neituiquan.net;

/**
 * Created by wangliang on 2018/6/16.
 */

public class HttpFactory {


    private static HttpUtils httpUtils = null;


    public static HttpUtils getHttpUtils(){
        if(httpUtils == null){
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }
}
