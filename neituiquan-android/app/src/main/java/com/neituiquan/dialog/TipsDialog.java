package com.neituiquan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.neituiquan.base.BaseDialog;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/7/3.
 * <p>
 * email:nice_ohoh@163.com
 */

public class TipsDialog extends BaseDialog implements View.OnClickListener {


    private View contentView;

    private TextView dialog_titleTv;
    private TextView dialog_contentTv;
    private TextView dialog_cancelTv;
    private TextView dialog_executeTv;

    private TipsDialogCallBack tipsDialogCallBack;

    public TipsDialog(Context context) {
        super(context);
    }

    public void setTipsDialogCallBack(TipsDialogCallBack tipsDialogCallBack) {
        this.tipsDialogCallBack = tipsDialogCallBack;
    }

    @Override
    public void init() {
        contentView = View.inflate(getContext(), R.layout.dialog_tips,null);
        setContentView(contentView);
        bindViews();

    }

    @Override
    public void initList() {

    }

    public void show(String title,String content){
        dialog_titleTv.setText(title);
        dialog_contentTv.setText(content);
        show();
    }

    public void show(String content){
        dialog_contentTv.setText(content);
        show();
    }


    private void bindViews() {

        dialog_titleTv = (TextView) findViewById(R.id.dialog_titleTv);
        dialog_contentTv = (TextView) findViewById(R.id.dialog_contentTv);
        dialog_cancelTv = (TextView) findViewById(R.id.dialog_cancelTv);
        dialog_executeTv = (TextView) findViewById(R.id.dialog_executeTv);
        dialog_cancelTv.setOnClickListener(this);
        dialog_executeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == dialog_cancelTv){
            if(tipsDialogCallBack != null){
                tipsDialogCallBack.cancel();
            }
            dismiss();
        }else if(v == dialog_executeTv){
            if(tipsDialogCallBack != null){
                tipsDialogCallBack.execute();
            }
            dismiss();
        }
    }

    public interface TipsDialogCallBack{

        public void execute();

        public void cancel();
    }
}
