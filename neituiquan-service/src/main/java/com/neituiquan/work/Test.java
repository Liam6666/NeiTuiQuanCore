package com.neituiquan.work;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Test {

    public static void main(String[] args) {


    }


    public static String getCode(){
        int[] code = new int[6];
        Random random = new Random();
        StringBuffer resultCode = new StringBuffer();
        for(int i = 0 ; i < code.length ; i ++){
            code[i] = random.nextInt(9);
            resultCode.append(code[i]);
        }
        return resultCode.toString();
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
