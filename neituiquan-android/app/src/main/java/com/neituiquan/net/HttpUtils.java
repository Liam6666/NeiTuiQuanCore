package com.neituiquan.net;


import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.neituiquan.FinalData;
import com.neituiquan.gson.AbsModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liang02.wang on 2018/3/14.
 */

public class HttpUtils {

    private OkHttpClient okHttpClient;

    private static final String GET = "GET";

    private static final String POST = "POST";

    public HttpUtils(){
        okHttpClient = new OkHttpClient()
                .newBuilder()
                .readTimeout(30 * 1000, TimeUnit.SECONDS)
                .build();
    }


    public void get(String url,final RequestEventModel requestEventModel){
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.method(GET,null);
        requestBuilder.url(url);
        final Request request = requestBuilder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestEventModel.isSuccess = false;
                requestEventModel.errorMsg = e.getMessage();
                if(FinalData.DEBUG){
                    ToastUtils.showShort(e.getMessage());
                }
                EventBus.getDefault().post(requestEventModel);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                requestEventModel.resultStr = response.body().string();
                EventBus.getDefault().post(requestEventModel);
                if(response.code() != 200){
                    requestEventModel.isSuccess = false;
                    requestEventModel.errorMsg = response.message();
                    if(FinalData.DEBUG){
                        ToastUtils.showShort(response.message());
                    }
                    EventBus.getDefault().post(requestEventModel);
                }
            }
        });
    }


    public void post(Map<String,String> params, String url,final RequestEventModel requestEventModel){
        FormBody.Builder formBody = new FormBody.Builder();
        if(params != null){
            for(String key : params.keySet()){
                String value = params.get(key);
                formBody.add(key,value);
            }
        }
        RequestBody requestBody = formBody.build();
        Request request = new Request.Builder()
                .url(url)
                .method(POST,requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestEventModel.isSuccess = false;
                requestEventModel.errorMsg = e.getMessage();
                if(FinalData.DEBUG){
                    ToastUtils.showShort(e.getMessage());
                }
                EventBus.getDefault().post(requestEventModel);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                requestEventModel.resultStr = response.body().string();
                EventBus.getDefault().post(requestEventModel);
                if(response.code() != 200){
                    requestEventModel.isSuccess = false;
                    requestEventModel.errorMsg = response.message();
                    if(FinalData.DEBUG){
                        ToastUtils.showShort(response.message());
                    }
                    EventBus.getDefault().post(requestEventModel);
                }
            }
        });
    }


    public void post(String json, String url,final RequestEventModel requestEventModel){
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .method(POST,requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestEventModel.isSuccess = false;
                requestEventModel.errorMsg = e.getMessage();
                if(FinalData.DEBUG){
                    ToastUtils.showShort(e.getMessage());
                }
                EventBus.getDefault().post(requestEventModel);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                requestEventModel.resultStr = response.body().string();
                EventBus.getDefault().post(requestEventModel);
                if(response.code() != 200){
                    requestEventModel.isSuccess = false;
                    requestEventModel.errorMsg = response.message();
                    if(FinalData.DEBUG){
                        ToastUtils.showShort(response.message());
                    }
                    EventBus.getDefault().post(requestEventModel);
                }
            }
        });
    }


    public void uploadMultiFile(File file,String url,final RequestEventModel requestEventModel) {
        String imageType = "multipart/form-data";
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("imagetype", imageType)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .method(POST,requestBody)
                .build();
        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestEventModel.isSuccess = false;
                requestEventModel.errorMsg = e.getMessage();
                if(FinalData.DEBUG){
                    ToastUtils.showShort(e.getMessage());
                }
                EventBus.getDefault().post(requestEventModel);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                requestEventModel.resultStr = response.body().string();
                EventBus.getDefault().post(requestEventModel);
                if(response.code() != 200){
                    requestEventModel.isSuccess = false;
                    requestEventModel.errorMsg = response.message();
                    if(FinalData.DEBUG){
                        ToastUtils.showShort(response.message());
                    }
                    EventBus.getDefault().post(requestEventModel);
                }
            }

        });
    }

}
