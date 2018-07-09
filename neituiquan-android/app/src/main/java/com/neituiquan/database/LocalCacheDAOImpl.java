package com.neituiquan.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class LocalCacheDAOImpl extends AppDataBase implements LocalCacheDAO {

    private SQLiteDatabase db;

    public LocalCacheDAOImpl(Context context) {
        super(context);
        db = getReadableDatabase();
    }

    @Override
    public boolean add(ChatEntity entity) {
        String sql = "insert into t_chat_history values (?,?,?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getFromId(),
                entity.getHeadImg(),entity.getNickName(),
                entity.getCreateTime(),entity.getMsgDetails(),
                entity.getIsRead(),entity.getIsSelf()
        };
        db.execSQL(sql,params);
        return true;
    }

    @Override
    public List<ChatEntity> getChatList() {
        String sql = "select * from t_chat_history";

        Cursor cursor = db.rawQuery(sql,new String[]{});

        List<ChatEntity> list = new ArrayList<>();
        while (cursor.moveToNext()){
            ChatEntity entity = new ChatEntity();
            setValues(entity,cursor);
            list.add(entity);
        }
        return list;
    }

    @Override
    public List<ChatEntity> getChatHistory(String fromId) {
        return null;
    }


    private void setValues(ChatEntity entity,Cursor cursor){
        entity.setId(cursor.getString(cursor.getColumnIndex(DBConstants.ID)));
        entity.setFromId(cursor.getString(cursor.getColumnIndex(DBConstants.FORM_ID)));
        entity.setHeadImg(cursor.getString(cursor.getColumnIndex(DBConstants.HEAD_IMG)));
        entity.setNickName(cursor.getString(cursor.getColumnIndex(DBConstants.NICK_NAME)));
        entity.setCreateTime(cursor.getString(cursor.getColumnIndex(DBConstants.CREATE_TIME)));
        entity.setMsgDetails(cursor.getString(cursor.getColumnIndex(DBConstants.MSG_DETAILS)));
        entity.setIsRead(cursor.getString(cursor.getColumnIndex(DBConstants.IS_READ)));
        entity.setIsSelf(cursor.getString(cursor.getColumnIndex(DBConstants.IS_SELF)));
    }
}
