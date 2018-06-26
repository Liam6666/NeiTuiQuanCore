package com.neituiquan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.neituiquan.base.BaseDialog;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/6/25.
 * <p>
 * email:nice_ohoh@163.com
 */

public class MenuDialog extends BaseDialog implements View.OnClickListener {

    private View contentView;

    private RadioGroup dialog_radioGroup;
    private TextView dialog_executeTv;

    public static final int VIEW = 0;
    public static final int EDIT = 1;
    public static final int DEL = 2;

    private int currentIndex = -1;

    private MenuDialogCallBack menuDialogCallBack;

    private Object selectorObj = null;

    public MenuDialog(Context context) {
        super(context);
    }

    @Override
    public void init() {
        contentView = View.inflate(getContext(), R.layout.dialog_menu,null);
        setContentView(contentView);
        getLayoutParams().height = SizeUtils.dp2px(250);
        setDialogParams(getLayoutParams());
    }

    @Override
    public void initList() {
        bindViews();
        dialog_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.dialog_radioBtn1:
                        currentIndex = VIEW;
                        break;
                    case R.id.dialog_radioBtn2:
                        currentIndex = EDIT;
                        break;
                    case R.id.dialog_radioBtn3:
                        currentIndex = DEL;
                        break;
                }
            }
        });
    }

    public void setMenuDialogCallBack(MenuDialogCallBack menuDialogCallBack) {
        this.menuDialogCallBack = menuDialogCallBack;
    }

    public void show(Object selectorObj) {
        super.show();
        this.selectorObj = selectorObj;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog_radioGroup.clearCheck();
        selectorObj = null;
    }

    private void bindViews() {

        dialog_radioGroup = (RadioGroup) findViewById(R.id.dialog_radioGroup);
        dialog_executeTv = (TextView) findViewById(R.id.dialog_executeTv);

        dialog_executeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == dialog_executeTv){
            if(menuDialogCallBack != null){
                if(currentIndex != -1){
                    menuDialogCallBack.onMenuItemSelector(currentIndex,selectorObj);
                }
            }
        }
    }


    public interface MenuDialogCallBack{

        public void onMenuItemSelector(int index,Object selectorObj);

    }
}
