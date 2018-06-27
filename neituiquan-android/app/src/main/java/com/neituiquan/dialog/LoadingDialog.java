package com.neituiquan.dialog;

import android.content.Context;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.neituiquan.base.BaseDialog;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/6/27.
 * <p>
 * email:nice_ohoh@163.com
 */

public class LoadingDialog extends BaseDialog {

    private View contentView;

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public void init() {
        contentView = View.inflate(getContext(), R.layout.dialog_loading,null);
        setContentView(contentView);

        getLayoutParams().height = SizeUtils.dp2px(100);
        getLayoutParams().width = SizeUtils.dp2px(300);
        setDialogParams(getLayoutParams());
    }

    @Override
    public void initList() {

    }
}
