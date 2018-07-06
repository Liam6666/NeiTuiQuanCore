package com.neituiquan.popwindow;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/7/6.
 * <p>
 * email:nice_ohoh@163.com
 */

public class HomePageMorePop extends PopupWindow {

    private View contentView;

    private Context context;

    public HomePageMorePop(Context context){
        super(context);
        this.context = context;
        init();
    }

    private void init(){
        contentView = View.inflate(getContext(), R.layout.pop_home_page_more,null);
        setContentView(contentView);
        setTouchable(true);
    }

    public Context getContext() {
        return context;
    }
}
