package com.neituiquan.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class AppDataBase extends SQLiteOpenHelper {

    public AppDataBase(Context context) {
        super(context, DBConstants.NAME, null, DBConstants.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * t_chat_group 消息列表
         * groupId  消息组的id
         * headImg  消息组的头像
         * groupName  消息组的名字
         * updateTime 最近消息时间
         */
        String sql = "create table t_chat_group " +
                "(groupId primary key,headImg,groupName,updateTime)";
        db.execSQL(sql);
        /**
         * t_chat_msg  消息具体内容
         * groupId  聊天组id
         * fromId  消息发送者id
         * fromHeadImg
         * fromNickName
         * createTime  消息发送时间
         * msgDetails  消息内容
         * isRead  是否已读
         */
        sql = "create table t_chat_msg " +
                "(id primary key,groupId,fromId,fromHeadImg,fromNickName,createTime,msgDetails,isRead)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
