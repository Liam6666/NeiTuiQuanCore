package com.neituiquan.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.neituiquan.work.R;


/**
 * Created by liang02.wang on 2018/3/14.
 */

public abstract class BaseDialog extends Dialog {

    private Context context;

    private WindowManager.LayoutParams layoutParams;

    private Window dialogWindow;

    public BaseDialog(Context context) {
        super(context, R.style.transparentDialog);
        this.context = context;
        dialogWindow = getWindow();
        layoutParams = dialogWindow.getAttributes();
        setCanceledOnTouchOutside(true);//点击空白地方可以取消显示dialog
        init();
        initList();
    }

    public BaseDialog(Context context,int themeResId) {
        super(context, themeResId);
        this.context = context;
        dialogWindow = getWindow();
        layoutParams = dialogWindow.getAttributes();
        setCanceledOnTouchOutside(true);//点击空白地方可以取消显示dialog
        init();
        initList();
    }


    public abstract void init();

    public abstract void initList();

    public Window getDialogWindow(){
        return dialogWindow;
    }

    public void setDialogParams(WindowManager.LayoutParams layoutParams){
        getDialogWindow().setAttributes(layoutParams);
    }
    public WindowManager.LayoutParams getLayoutParams(){
        return layoutParams;
    }



}
