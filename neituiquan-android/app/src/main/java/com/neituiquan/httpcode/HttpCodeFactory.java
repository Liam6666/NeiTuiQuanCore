package com.neituiquan.httpcode;

/**
 * Created by Augustine on 2018/7/19.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 另一个网络请求工具，使用Handler回调，eventbus使用的太多，代码的可维护性变差
 */

public class HttpCodeFactory {

    private static HttpURLCode httpURLCode;

    public static HttpURLCode getHttpUtils(){
        if(httpURLCode == null){
            httpURLCode = new HttpURLCode();
        }
        return httpURLCode;
    }
}
