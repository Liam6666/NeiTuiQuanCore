package com.neituiquan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.neituiquan.App;
import com.neituiquan.entity.ChatLoopEntity;
import com.neituiquan.entity.MessageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/7/12.
 * <p>
 * email:nice_ohoh@163.com
 */

public class AppDBUtils extends AppDataBase {

    private SQLiteDatabase db;

    private String account;

    private String userId;

    public AppDBUtils(Context context) {
        super(context);
        db = getWritableDatabase();
        account = App.getAppInstance().getUserInfoUtils().getUserInfo().data.getAccount();
        userId = App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
    }


    public void addChat(ChatDBEntity chatDBEntity){
        String otherSideId;
        if(chatDBEntity.getIsFrom().equals(DBConstants.YES)){
            //发送者
            otherSideId = chatDBEntity.getReceiveId();
        }else{
            //接受者
            otherSideId = chatDBEntity.getFromId();
        }
        if(!hasOtherSideId(chatDBEntity)){
            //未创建聊天组
            ContentValues contentValues = new ContentValues();
            contentValues.put("otherSideId",otherSideId);
            contentValues.put("account",account);
            if(chatDBEntity.getIsFrom().equals(DBConstants.YES)){
                //发送者
                contentValues.put("osHeadImg",chatDBEntity.getReceiveHeadImg());
                contentValues.put("osNickName",chatDBEntity.getReceiveNickName());
            }else{
                //接受者
                contentValues.put("osHeadImg",chatDBEntity.getFromHeadImg());
                contentValues.put("osNickName",chatDBEntity.getFromNickName());
            }
            contentValues.put("notReadCount",1);
            contentValues.put("lastChatId",chatDBEntity.getChatId());
            db.insert("t_chat_group","",contentValues);

            contentValues.clear();
            contentValues.put("chatId",chatDBEntity.getChatId());
            contentValues.put("groupId",otherSideId);
            contentValues.put("fromId",chatDBEntity.getFromId());
            contentValues.put("fromNickName",chatDBEntity.getFromNickName());
            contentValues.put("fromHeadImg",chatDBEntity.getFromHeadImg());
            contentValues.put("receiveId",chatDBEntity.getReceiveId());
            contentValues.put("receiveNickName",chatDBEntity.getReceiveNickName());
            contentValues.put("receiveHeadImg",chatDBEntity.getReceiveHeadImg());
            contentValues.put("msgDetails",chatDBEntity.getMsgDetails());
            contentValues.put("msgType",chatDBEntity.getMsgType());
            contentValues.put("account",chatDBEntity.getAccount());
            contentValues.put("isFrom",chatDBEntity.getIsFrom());
            contentValues.put("isRead",DBConstants.NO);
            contentValues.put("createTime",chatDBEntity.getCreateTime());
            db.insert("t_chat_history","",contentValues);
        }else{
            //更新状态
            ContentValues contentValues = new ContentValues();
            contentValues.put("chatId",chatDBEntity.getChatId());
            contentValues.put("groupId",otherSideId);
            contentValues.put("fromId",chatDBEntity.getFromId());
            contentValues.put("fromNickName",chatDBEntity.getFromNickName());
            contentValues.put("fromHeadImg",chatDBEntity.getFromHeadImg());
            contentValues.put("receiveId",chatDBEntity.getReceiveId());
            contentValues.put("receiveNickName",chatDBEntity.getReceiveNickName());
            contentValues.put("receiveHeadImg",chatDBEntity.getReceiveHeadImg());
            contentValues.put("msgDetails",chatDBEntity.getMsgDetails());
            contentValues.put("msgType",chatDBEntity.getMsgType());
            contentValues.put("account",chatDBEntity.getAccount());
            contentValues.put("isRead",DBConstants.NO);
            contentValues.put("isFrom",chatDBEntity.getIsFrom());
            contentValues.put("createTime",chatDBEntity.getCreateTime());
            db.insert("t_chat_history","",contentValues);

            contentValues.clear();
            if(chatDBEntity.getIsFrom().equals(DBConstants.YES)){
                //发送者
                contentValues.put("osHeadImg",chatDBEntity.getReceiveHeadImg());
                contentValues.put("osNickName",chatDBEntity.getReceiveNickName());
            }else{
                //接受者
                contentValues.put("osHeadImg",chatDBEntity.getFromHeadImg());
                contentValues.put("osNickName",chatDBEntity.getFromNickName());
            }
            contentValues.put("notReadCount",getNoReadCount(otherSideId)+"");
            contentValues.put("lastChatId",chatDBEntity.getChatId());
            db.update("t_chat_group",contentValues,"otherSideId = ?",new String[]{otherSideId});
        }
    }


    /**
     * 创建一个聊天组前先判断是否有这个聊天组
     *
     * 1、判断对方是发送者还是接受者
     */
    public boolean hasOtherSideId(ChatDBEntity chatDBEntity){
        Cursor cursor;
        if(chatDBEntity.getIsFrom().equals(DBConstants.YES)){
            //发送者
            String sql = "select * from t_chat_group where account = ? and otherSideId = ?";
            cursor = db.rawQuery(sql,new String[]{account,chatDBEntity.getReceiveId()});
        }else{
            //接受者
            String sql = "select * from t_chat_group where account = ? and otherSideId = ?";
            cursor = db.rawQuery(sql,new String[]{account,chatDBEntity.getFromId()});
        }
        int count = cursor.getCount();
        if(count == 0){
            return false;
        }
        return true;
    }


    public ChatDBEntity getLastChat(String groupId){
        ChatDBEntity entity = new ChatDBEntity();
        String sql = "select * from t_chat_history where " +
                "account = ? and groupId = ? order by createTime limit 0,1";
        Cursor cursor = db.rawQuery(sql,new String[]{account,groupId});
        while (cursor.moveToNext()){
            setChatValues(entity,cursor);
        }
        return entity;
    }


    public List<ChatGroupDBEntity> getGroupList(){
        List<ChatGroupDBEntity> list = new ArrayList<>();
        String sql = "select * from t_chat_group where account = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{account});
        while (cursor.moveToNext()){
            ChatGroupDBEntity entity = new ChatGroupDBEntity();
            setGroupValues(entity,cursor);
            ChatDBEntity chatDBEntity = findChatById(entity.getLastChatId());
            entity.setLastChatEntity(chatDBEntity);
            list.add(entity);
        }
        cursor.close();
        return list;
    }

    public ChatDBEntity findChatById(String chatId){
        ChatDBEntity entity = new ChatDBEntity();
        String sql = "select * from t_chat_history where " +
                "chatId = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{chatId});
        while (cursor.moveToNext()){
            setChatValues(entity,cursor);
        }
        cursor.close();
        return entity;
    }

    public int getNoReadCount(String groupId){
        String sql = "select * from t_chat_history where " +
                "account = ? and groupId = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{account,groupId});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public List<ChatDBEntity> getChatList(String groupId){
        List<ChatDBEntity> list = new ArrayList<>();
        String sql = "select * from t_chat_history where " +
                "account = ? and groupId = ? order by createTime desc";
        Cursor cursor = db.rawQuery(sql,new String[]{groupId});
        while (cursor.moveToNext()){
            ChatDBEntity entity = new ChatDBEntity();
            setChatValues(entity,cursor);
            list.add(entity);
        }
        cursor.close();
        return list;
    }

    public void removeAll(){
        String sql = "delete from t_chat_history";
        db.execSQL(sql);
        sql = "delete from t_chat_group";
        db.execSQL(sql);
    }

    private void setChatValues(ChatDBEntity entity,Cursor cursor){
        entity.setChatId(cursor.getString(cursor.getColumnIndex("chatId")));
        entity.setGroupId(cursor.getString(cursor.getColumnIndex("groupId")));
        entity.setFromId(cursor.getString(cursor.getColumnIndex("fromId")));
        entity.setFromNickName(cursor.getString(cursor.getColumnIndex("fromNickName")));
        entity.setFromHeadImg(cursor.getString(cursor.getColumnIndex("fromHeadImg")));
        entity.setReceiveId(cursor.getString(cursor.getColumnIndex("receiveId")));
        entity.setReceiveNickName(cursor.getString(cursor.getColumnIndex("receiveNickName")));
        entity.setReceiveHeadImg(cursor.getString(cursor.getColumnIndex("receiveHeadImg")));
        entity.setMsgDetails(cursor.getString(cursor.getColumnIndex("msgDetails")));
        entity.setMsgType(cursor.getString(cursor.getColumnIndex("msgType")));
        entity.setAccount(cursor.getString(cursor.getColumnIndex("account")));
        entity.setIsFrom(cursor.getString(cursor.getColumnIndex("isFrom")));
        entity.setIsRead(cursor.getString(cursor.getColumnIndex("isRead")));
        entity.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
    }


    private void setGroupValues(ChatGroupDBEntity entity,Cursor cursor){
        entity.setOtherSideId(cursor.getString(cursor.getColumnIndex("otherSideId")));
        entity.setAccount(cursor.getString(cursor.getColumnIndex("account")));
        entity.setOsHeadImg(cursor.getString(cursor.getColumnIndex("osHeadImg")));
        entity.setOsNickName(cursor.getString(cursor.getColumnIndex("osNickName")));
        entity.setNotReadCount(cursor.getString(cursor.getColumnIndex("notReadCount")));
        entity.setLastChatId(cursor.getString(cursor.getColumnIndex("lastChatId")));
    }
}
