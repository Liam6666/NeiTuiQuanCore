package com.neituiquan.utils;

import com.neituiquan.FinalData;

import java.util.Random;

/**
 * Created by Augustine on 2018/7/5.
 * <p>
 * email:nice_ohoh@163.com
 */

public class VerificationCodeUtils {

    public static String getCode(){
        int[] code = new int[FinalData.V_CODE_LENGTH];
        Random random = new Random();
        StringBuffer resultCode = new StringBuffer();
        for(int i = 0 ; i < code.length ; i ++){
            code[i] = random.nextInt(9);
            resultCode.append(code[i]);
        }
        return resultCode.toString();
    }
}
