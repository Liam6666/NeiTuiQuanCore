package com.neituiquan.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/7/11.
 * <p>
 * email:nice_ohoh@163.com
 */

public class GlideUtils {

    public static void load(String img, ImageView imageView){
        Glide.with(App.getAppInstance().getApplicationContext()).load(FinalData.IMG+img).error(R.mipmap.img_error).into(imageView);

    }
}
