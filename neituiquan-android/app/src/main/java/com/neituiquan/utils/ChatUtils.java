package com.neituiquan.utils;

/**
 * Created by Augustine on 2018/7/13.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ChatUtils {

    private static String[] IMG_FORMAT = new String[]{
            ".png",".gif",".jpeg",".jpg",
            ".PNG",".GIF",".JPEG",".JPG",
    };

    public static boolean isImg(String msg){
        if(msg.contains("http")){
            for(String format : IMG_FORMAT){
                if(msg.contains(format)){
                    return true;
                }
            }
        }
        return false;
    }
}
