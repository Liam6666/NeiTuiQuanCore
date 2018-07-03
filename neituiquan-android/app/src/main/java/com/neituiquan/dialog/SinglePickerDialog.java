package com.neituiquan.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.neituiquan.base.BaseDialog;
import com.neituiquan.work.R;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.List;

/**
 * Created by Augustine on 2018/7/3.
 * <p>
 * email:nice_ohoh@163.com
 */

public class SinglePickerDialog extends BaseDialog {

    private View contentView;

    private TextView dialog_cancelTv;
    private TextView dialog_executeTv;
    private LoopView dialog_LoopView;

    private SinglePickerDialogCallBack dialogCallBack;

    private List<String> dataList;

    private int currentPosition = 0;

    public SinglePickerDialog(Context context) {
        super(context);
    }

    public void setDialogCallBack(SinglePickerDialogCallBack dialogCallBack) {
        this.dialogCallBack = dialogCallBack;
    }

    @Override
    public void init() {
        contentView = View.inflate(getContext(),R.layout.dialog_single_picker,null);
        setContentView(contentView);
        bindViews();
        dialog_LoopView.setCenterTextColor(ContextCompat.getColor(getContext(),R.color.themeColor));
        dialog_LoopView.setTextSize(16);
        dialog_LoopView.setNotLoop();
        dialog_LoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                currentPosition = index;
            }
        });

        getLayoutParams().width = ScreenUtils.getScreenWidth();
        getLayoutParams().gravity = Gravity.BOTTOM;
        setDialogParams(getLayoutParams());

    }

    @Override
    public void initList() {

    }

    public void setContentData(List<String> dataList){
        this.dataList = dataList;
        dialog_LoopView.setItems(dataList);
    }

    public LoopView getDialog_LoopView() {
        return dialog_LoopView;
    }

    /**
     * 显示并移动到当前选择的值
     * @param currentStr
     */
    public void show(String currentStr){
        if(!StringUtils.isEmpty(currentStr)){
            for(int i = 0 ; i < dataList.size() ; i ++){
                if(dataList.get(i).equals(currentStr)){
                    dialog_LoopView.setCurrentPosition(i);
                }
            }
        }
        show();
    }

    private void bindViews() {

        dialog_cancelTv = contentView.findViewById(R.id.dialog_cancelTv);
        dialog_executeTv = contentView.findViewById(R.id.dialog_executeTv);
        dialog_LoopView = contentView.findViewById(R.id.dialog_LoopView);
        dialog_cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog_executeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogCallBack != null){
                    dialogCallBack.onSelect(dataList.get(currentPosition));
                }
                dismiss();
            }
        });
    }

    public interface SinglePickerDialogCallBack{

        public void onSelect(String info);

    }
}
