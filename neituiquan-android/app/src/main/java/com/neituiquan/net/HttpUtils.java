package com.neituiquan.net;



import android.util.Log;

import com.neituiquan.FinalData;

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

    private static final String TAG = "HttpUtils";


    public HttpUtils(){
        okHttpClient = new OkHttpClient()
                .newBuilder()
                .readTimeout(FinalData.TIME_OUT, TimeUnit.SECONDS)
                .build();
    }


    public void get(String url,final RequestEventModel requestEventModel){
        if(FinalData.DEBUG){
            Log.e(TAG,url);
        }
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
                EventBus.getDefault().post(requestEventModel);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultStr = response.body().string();
                if(response.code() == 200){
                    requestEventModel.resultStr = resultStr;
                }else{
                    requestEventModel.isSuccess = false;
                    requestEventModel.errorMsg = resultStr;
                }
                EventBus.getDefault().post(requestEventModel);
                if(FinalData.DEBUG){
                    Log.e(TAG,resultStr);
                }
            }
        });
    }

    public Call get(String url){
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.method(GET,null);
        requestBuilder.url(url);
        final Request request = requestBuilder.build();
        return okHttpClient.newCall(request);
    }

    public void getNoCall(String url){
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.method(GET,null);
        requestBuilder.url(url);
        final Request request = requestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public void post(Map<String,String> params, String url,final RequestEventModel requestEventModel){
        if(FinalData.DEBUG){
            Log.e(TAG,url);
        }
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
                EventBus.getDefault().post(requestEventModel);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultStr = response.body().string();
                if(response.code() == 200){
                    requestEventModel.resultStr = resultStr;
                }else{
                    requestEventModel.isSuccess = false;
                    requestEventModel.errorMsg = resultStr;
                }
                EventBus.getDefault().post(requestEventModel);
                if(FinalData.DEBUG){
                    Log.e(TAG,resultStr);
                }
            }
        });
    }


    public void post(String json, String url,final RequestEventModel requestEventModel){
        if(FinalData.DEBUG){
            Log.e(TAG,url);
        }
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
                requestEventModel.isSuccess = false;
                requestEventModel.errorMsg = e.getMessage();
                EventBus.getDefault().post(requestEventModel);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultStr = response.body().string();
                if(response.code() == 200){
                    requestEventModel.resultStr = resultStr;
                }else{
                    requestEventModel.isSuccess = false;
                    requestEventModel.errorMsg = resultStr;
                }
                EventBus.getDefault().post(requestEventModel);
                if(FinalData.DEBUG){
                    Log.e(TAG,resultStr);
                }
            }
        });
    }


    public void uploadMultiFile(File file,String url,final RequestEventModel requestEventModel) {
        if(FinalData.DEBUG){
            Log.e(TAG,url);
        }
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
                EventBus.getDefault().post(requestEventModel);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultStr = response.body().string();
                if(response.code() == 200){
                    requestEventModel.resultStr = resultStr;
                }else{
                    requestEventModel.isSuccess = false;
                    requestEventModel.errorMsg = resultStr;
                }
                EventBus.getDefault().post(requestEventModel);
                if(FinalData.DEBUG){
                    Log.e(TAG,resultStr);
                }
            }

        });
    }

}
