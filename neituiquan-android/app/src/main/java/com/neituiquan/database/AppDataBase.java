package com.neituiquan.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blankj.utilcode.util.ResourceUtils;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class AppDataBase extends SQLiteOpenHelper {

    private Context context;

    public AppDataBase(Context context) {
        super(context, DBConstants.NAME, null, DBConstants.VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = ResourceUtils.readAssets2String(DBConstants.T_CHAT_HISTORY);
        db.execSQL(sql);
        sql = ResourceUtils.readAssets2String(DBConstants.T_CHAT_GROUP_HISTORY);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Context getContext() {
        return context;
    }
}
