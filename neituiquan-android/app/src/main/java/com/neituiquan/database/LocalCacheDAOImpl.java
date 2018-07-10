package com.neituiquan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.google.gson.Gson;
import com.neituiquan.FinalData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class LocalCacheDAOImpl extends AppDataBase implements LocalCacheDAO {

    private SQLiteDatabase db;

    private static final String TAG = "LocalCacheDAOImpl";

    public LocalCacheDAOImpl(Context context) {
        super(context);
        db = getReadableDatabase();
    }

    @Override
    public boolean add(ChatEntity entity) {
        if(!hasId(entity.getId())){
            ContentValues values = new ContentValues();
            values.put("id",entity.getId());
            values.put("groupId",entity.getGroupId());
            values.put("fromId",entity.getFromId());
            values.put("fromHeadImg",entity.getFromHeadImg());
            values.put("fromNickName",entity.getFromNickName());
            values.put("createTime",entity.getCreateTime());
            values.put("msgDetails",entity.getMsgDetails());
            values.put("isRead",entity.getIsRead());
            db.insert("t_chat_msg","",values);
        }
        if(!hasGroup(entity.getGroupId())){
            ContentValues groupValues = new ContentValues();
            groupValues.put("groupId",entity.getGroupId());
            groupValues.put("headImg",entity.getFromHeadImg());
            groupValues.put("groupName",entity.getFromNickName());
            groupValues.put("updateTime",entity.getCreateTime());
            db.insert("t_chat_group","",groupValues);
        }
        return true;
    }

    @Override
    public List<ChatGroupEntity> getChatGroupList() {
        List<ChatGroupEntity> list = new ArrayList<>();
        String sql = "select * from t_chat_group";
        Cursor cursor = db.rawQuery(sql,new String[]{});
        while (cursor.moveToNext()){
            ChatGroupEntity entity = new ChatGroupEntity();
            setGroupValues(entity,cursor);
            list.add(entity);
        }
//        Log.e(TAG,new Gson().toJson(list));
        cursor.close();
        return list;
    }

    @Override
    public List<ChatEntity> getChatHistory(String groupId,String pageSize) {
        List<ChatEntity> list = new ArrayList<>();
        String sql = "select * from t_chat_msg where groupId = ? order by createTime desc limit ?,?";
        Cursor cursor = db.rawQuery(sql,new String[]{groupId,pageSize,String.valueOf( FinalData.PAGE_SIZE)});
        while (cursor.moveToNext()){
            ChatEntity entity = new ChatEntity();
            setValues(entity,cursor);
            list.add(entity);
        }
        cursor.close();
        return list;
    }

    @Override
    public int getGroupNotReadCount(String groupId) {
        String sql = "select * from t_chat_msg where groupId = ? and isRead = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{groupId,DBConstants.NO});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public ChatEntity getGroupLastChat(String groupId) {
        String sql = "select * from t_chat_msg where groupId = ? order by createTime limit 0,1";
        Cursor cursor = db.rawQuery(sql,new String[]{groupId});
        ChatEntity entity = new ChatEntity();
        while (cursor.moveToNext()){
            setValues(entity,cursor);
        }
        cursor.close();
        return entity;
    }

    @Override
    public boolean hasId(String id) {
        String sql = "select * from t_chat_msg where id = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{id});
        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    @Override
    public boolean hasGroup(String groupId) {
        String sql = "select * from t_chat_group where groupId = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{groupId});
        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private void setGroupValues(ChatGroupEntity entity,Cursor cursor){
        entity.setGroupId(cursor.getString(cursor.getColumnIndex("groupId")));
        entity.setHeadImg(cursor.getString(cursor.getColumnIndex("headImg")));
        entity.setGroupName(cursor.getString(cursor.getColumnIndex("groupName")));
        entity.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
//        entity.setNotReadCount(cursor.getString(cursor.getColumnIndex("notReadCount")));
//        entity.setLastChat(cursor.getString(cursor.getColumnIndex("lastChat")));
//        entity.setLastChatTime(cursor.getString(cursor.getColumnIndex("lastChatTime")));
    }


    private void setValues(ChatEntity entity,Cursor cursor){
        entity.setId(cursor.getString(cursor.getColumnIndex("id")));
        entity.setGroupId(cursor.getString(cursor.getColumnIndex("groupId")));
        entity.setFromId(cursor.getString(cursor.getColumnIndex("fromId")));
        entity.setFromHeadImg(cursor.getString(cursor.getColumnIndex("fromHeadImg")));
        entity.setFromNickName(cursor.getString(cursor.getColumnIndex("fromNickName")));
        entity.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
        entity.setMsgDetails(cursor.getString(cursor.getColumnIndex("msgDetails")));
        entity.setIsRead(cursor.getString(cursor.getColumnIndex("isRead")));

    }

}
