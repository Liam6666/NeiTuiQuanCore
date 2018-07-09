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
        String sql = "create table ? (?,?,?,?,?,?,?,?)";
        String[] params = new String[]{
                DBConstants.TABLE_NAME,
                DBConstants.ID,
                DBConstants.FORM_ID,
                DBConstants.HEAD_IMG,
                DBConstants.NICK_NAME,
                DBConstants.CREATE_TIME,
                DBConstants.MSG_DETAILS,
                DBConstants.IS_READ,
                DBConstants.IS_SELF
        };
        db.execSQL(sql,params);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
