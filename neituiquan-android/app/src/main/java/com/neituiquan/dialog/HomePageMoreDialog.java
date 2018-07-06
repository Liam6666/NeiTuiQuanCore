package com.neituiquan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.neituiquan.base.BaseDialog;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/7/6.
 * <p>
 * email:nice_ohoh@163.com
 */

public class HomePageMoreDialog extends BaseDialog {

    private View contentView;

    private int viewHeight;

    public HomePageMoreDialog(Context context) {
        super(context);
    }

    @Override
    public void init() {
        contentView = View.inflate(getContext(), R.layout.pop_home_page_more,null);
        setContentView(contentView);
        viewHeight = SizeUtils.dp2px(185);
        getLayoutParams().height = viewHeight;
        getLayoutParams().width = (int) (ScreenUtils.getScreenWidth() * 0.6f);
        getLayoutParams().gravity = Gravity.TOP | Gravity.RIGHT;
        setDialogParams(getLayoutParams());
    }

    @Override
    public void initList() {

    }

    public void show(int y) {
        //如果将要超过屏幕高度
        if((y + viewHeight) > (ScreenUtils.getScreenHeight() - viewHeight / 2)){
            getLayoutParams().y = y - viewHeight - 50;
        }else{
            getLayoutParams().y = y + 50;
        }
        setDialogParams(getLayoutParams());
        super.show();
    }
}
