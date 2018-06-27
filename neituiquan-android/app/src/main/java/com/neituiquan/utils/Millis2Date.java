package com.neituiquan.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Augustine on 2018/6/27.
 * <p>
 * email:nice_ohoh@163.com
 */

public class Millis2Date {


    public static String millis2Date(String millis){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(millis));
        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String dateStr = dateFormat.format(date);
        if(Integer.valueOf(dateStr.substring(0,4)) == c.get(Calendar.YEAR)){
            dateFormat =  new SimpleDateFormat("MM月dd日");
        }
        return dateFormat.format(date);
    }

}
