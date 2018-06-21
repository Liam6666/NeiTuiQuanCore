package com.neituiquan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.neituiquan.base.BaseDialog;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/6/21.
 * <p>
 * email:nice_ohoh@163.com
 *
 *
 */

public class EditHeadImgMenuDialog extends BaseDialog implements View.OnClickListener {

    private TextView dialog_uploadTv;
    private TextView dialog_photoTv;
    private TextView dialog_cancelTv;

    private DialogCallBack dialogCallBack;

    private View contentView;

    public EditHeadImgMenuDialog(Context context) {
        super(context, R.style.transparentBGDialog);
    }

    @Override
    public void init() {
        contentView = View.inflate(getContext(),R.layout.dialog_edit_head_img,null);
        setContentView(contentView);
        bindViews();
        getLayoutParams().width = -1;
        getLayoutParams().height = SizeUtils.dp2px(150);
        getLayoutParams().gravity = Gravity.BOTTOM;
        setDialogParams(getLayoutParams());
    }

    @Override
    public void initList() {

    }

    public void setDialogCallBack(DialogCallBack dialogCallBack) {
        this.dialogCallBack = dialogCallBack;
    }

    @Override
    public void onClick(View v) {
        if(dialogCallBack != null){
            dialogCallBack.onClick(v.getId());
        }
        switch (v.getId()){
            case R.id.dialog_uploadTv:
                dismiss();
                break;
            case R.id.dialog_photoTv:
                dismiss();
                break;
            case R.id.dialog_cancelTv:
                dismiss();
                break;
        }
    }

    private void bindViews() {

        dialog_uploadTv = contentView.findViewById(R.id.dialog_uploadTv);
        dialog_photoTv = contentView.findViewById(R.id.dialog_photoTv);
        dialog_cancelTv = contentView.findViewById(R.id.dialog_cancelTv);
        dialog_cancelTv.setOnClickListener(this);
        dialog_photoTv.setOnClickListener(this);
        dialog_uploadTv.setOnClickListener(this);
    }


    public interface DialogCallBack{

        public void onClick(int id);

    }

}
