package com.neituiquan.httpcode;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.neituiquan.FinalData;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Augustine on 2018/7/19.
 * <p>
 * email:nice_ohoh@163.com
 */

public class HttpURLCode {

    private OkHttpClient okHttpClient;


    private static final String GET = "GET";

    private static final String POST = "POST";

    private static final String TAG = "HttpURLCode";


    public static final int SUCCESS_CODE = 200;

    public static final int ERROR_CODE = 400;

    public static final int COMPLETE_CODE = 900;

    public static final int WAITING_CODE = 100;

    public HttpURLCode(){
        okHttpClient = new OkHttpClient()
                .newBuilder()
                .readTimeout(FinalData.TIME_OUT, TimeUnit.SECONDS)
                .build();
    }


    public void get(String url, final BaseHandler handler, int requestId, final Class model){
        if(FinalData.DEBUG){
            Log.e(TAG,url);
        }
        handler.sendEmptyMessage(WAITING_CODE);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.method(GET,null);
        requestBuilder.url(url);
        final Request request = requestBuilder.build();
        handler.bindId(requestId);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = ERROR_CODE;
                msg.obj = e.getMessage();
                handler.sendMessage(msg);
                handler.sendEmptyMessage(COMPLETE_CODE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = Message.obtain();
                msg.what = SUCCESS_CODE;
                msg.obj = new Gson().fromJson(result,model);
                handler.sendMessage(msg);
                handler.sendEmptyMessage(COMPLETE_CODE);
                if(FinalData.DEBUG){
                    Log.e(TAG,result);
                }
            }
        });
    }


    public void post(String url, String json,final BaseHandler handler, int requestId, final Class model){
        if(FinalData.DEBUG){
            Log.e(TAG,url);
        }
        handler.sendEmptyMessage(WAITING_CODE);
        handler.bindId(requestId);
        RequestBody requestBody = FormBody
                .create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .method(POST,requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = ERROR_CODE;
                msg.obj = e.getMessage();
                handler.sendMessage(msg);
                handler.sendEmptyMessage(COMPLETE_CODE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = Message.obtain();
                msg.what = SUCCESS_CODE;
                msg.obj = new Gson().fromJson(result,model);
                handler.sendMessage(msg);
                handler.sendEmptyMessage(COMPLETE_CODE);
                if(FinalData.DEBUG){
                    Log.e(TAG,result);
                }
            }
        });
    }
}
