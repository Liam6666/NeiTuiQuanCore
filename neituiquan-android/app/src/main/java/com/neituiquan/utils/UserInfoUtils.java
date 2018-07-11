package com.neituiquan.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.neituiquan.gson.UserModel;

/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 */

public class UserInfoUtils {

    private SharedPreferences sharedPreferences;


    public static final String KEY_NAME = "USER-INFO";

    public UserInfoUtils(Context context){
        sharedPreferences = context.getSharedPreferences("UserInfoUtils",Context.MODE_PRIVATE);
    }

    public void saveUserInfo(String json){
        sharedPreferences.edit().remove(KEY_NAME).commit();
        sharedPreferences.edit().putString(KEY_NAME,json).commit();
    }

    public UserModel getUserInfo(){
        String json = sharedPreferences.getString(KEY_NAME,"");
        if(json.equals("")){
            return null;
        }
        return new Gson().fromJson(json,UserModel.class);
    }

    public String getUserId(){
        return getUserInfo().data.getId();
    }

    public void clearUserInfo(){
        sharedPreferences.edit().remove(KEY_NAME).commit();
    }
}
