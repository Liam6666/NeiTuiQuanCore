package com.neituiquan.database;


import com.neituiquan.FinalData;

public class PageUtils {


    public static String limit(String sql,String index){
        int i = Integer.parseInt(index);
        i = i * FinalData.PAGE_SIZE;
        sql += " limit " + i + "," + FinalData.PAGE_SIZE;
        return sql;
    }

    public static String limit(String sql,int index){
        index = index * FinalData.PAGE_SIZE;
        sql += " limit " + index + "," + FinalData.PAGE_SIZE;
        return sql;
    }
}



