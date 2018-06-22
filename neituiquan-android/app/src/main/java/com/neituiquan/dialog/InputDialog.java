package com.neituiquan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.neituiquan.base.BaseDialog;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 */

public class InputDialog extends BaseDialog implements View.OnClickListener {

    private View contentView;

    private EditText dialog_inputEdit;
    private TextView dialog_executeTv;

    private InputDialogCallBack inputDialogCallBack;

    public InputDialog(Context context) {
        super(context);
    }

    public void setInputDialogCallBack(InputDialogCallBack inputDialogCallBack) {
        this.inputDialogCallBack = inputDialogCallBack;
    }

    @Override
    public void init() {
        contentView = View.inflate(getContext(), R.layout.dialog_input,null);
        setContentView(contentView);
        getLayoutParams().height = SizeUtils.dp2px(200);
        setDialogParams(getLayoutParams());
    }

    @Override
    public void initList() {
        bindViews();

    }

    private void bindViews() {

        dialog_inputEdit = (EditText) findViewById(R.id.dialog_inputEdit);
        dialog_executeTv = (TextView) findViewById(R.id.dialog_executeTv);
        dialog_executeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_executeTv:
                if(inputDialogCallBack != null){
                    inputDialogCallBack.executeTvClick(dialog_inputEdit.getText().toString());
                }
                dismiss();
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog_inputEdit.setText("");
    }

    public interface InputDialogCallBack{

        public void executeTvClick(String inputMsg);

    }
}
