package com.neituiquan.work;


import com.neituiquan.work.utils.PageUtils;
import com.neituiquan.work.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

    public static void main(String[] args) {
        System.out.println(StringUtils.getCurrentTimeMillis());
    }

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
