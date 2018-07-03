package com.neituiquan.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.neituiquan.base.BaseDialog;
import com.neituiquan.work.R;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Augustine on 2018/7/3.
 * <p>
 * email:nice_ohoh@163.com
 */

public class DatePickerDialog extends BaseDialog {

    private View contentView;

    private LoopView dialog_yearLoopView;

    private LoopView dialog_monthLoopView;

    private LoopView dialog_dayLoopView;

    private TextView dialog_cancelTv;

    private TextView dialog_executeTv;

    private List<String> yearItems;

    private List<String> monthItems;

    private List<String> dayItems;

    private int[] position = new int[3];

    private DatePickerDialogCallBack dialogCallBack;

    public DatePickerDialog(Context context) {
        super(context);
    }

    @Override
    public void init() {
        contentView = View.inflate(getContext(), R.layout.dialog_date_picker,null);
        setContentView(contentView);
        dialog_yearLoopView = contentView.findViewById(R.id.dialog_yearLoopView);
        dialog_monthLoopView = contentView.findViewById(R.id.dialog_monthLoopView);
        dialog_dayLoopView = contentView.findViewById(R.id.dialog_dayLoopView);
        dialog_cancelTv = contentView.findViewById(R.id.dialog_cancelTv);
        dialog_executeTv = contentView.findViewById(R.id.dialog_executeTv);
        dialog_yearLoopView.setCenterTextColor(ContextCompat.getColor(getContext(),R.color.themeColor));
        dialog_yearLoopView.setTextSize(16);
        dialog_yearLoopView.setNotLoop();
        dialog_monthLoopView.setCenterTextColor(ContextCompat.getColor(getContext(),R.color.themeColor));
        dialog_monthLoopView.setTextSize(16);
        dialog_monthLoopView.setNotLoop();
        dialog_dayLoopView.setCenterTextColor(ContextCompat.getColor(getContext(),R.color.themeColor));
        dialog_dayLoopView.setTextSize(16);
        dialog_dayLoopView.setNotLoop();

        getLayoutParams().gravity = Gravity.BOTTOM;
        setDialogParams(getLayoutParams());

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
                    String date = yearItems.get(position[0]) + "年" + monthItems.get(position[1]) + "月" + dayItems.get(position[2]) +"日";
                    dialogCallBack.onSelect(date);
                }
                dismiss();
            }
        });

    }

    @Override
    public void initList() {
        Calendar calendar = Calendar.getInstance();
        yearItems = new ArrayList<>();
        for(int i = calendar.get(Calendar.YEAR) ; i >= 1950 ; i --){
            yearItems.add(i +"");
        }

        monthItems = new ArrayList<>();
        for(int i = 1 ; i <= 12 ; i ++){
            monthItems.add(i +"");
        }
        dialog_yearLoopView.setItems(yearItems);

        dialog_monthLoopView.setItems(monthItems);

        dayItems = new ArrayList<>();
        for(int i = 1 ; i <= 31 ; i ++){
            dayItems.add(i +"");
        }
        dialog_dayLoopView.setItems(dayItems);

        dialog_yearLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                position[0] = index;
            }
        });
        dialog_monthLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                position[1] = index;
            }
        });
        dialog_dayLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                position[2] = index;
            }
        });
    }

    @Override
    public void show() {
        super.show();
        if(position[0] != 0){
            dialog_yearLoopView.setCurrentPosition(position[0]);
        }
        if(position[1] != 0){
            dialog_monthLoopView.setCurrentPosition(position[1]);
        }
        if(position[2] != 0){
            dialog_dayLoopView.setCurrentPosition(position[2]);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void setDialogCallBack(DatePickerDialogCallBack dialogCallBack) {
        this.dialogCallBack = dialogCallBack;
    }

    public interface DatePickerDialogCallBack{

        public void onSelect(String date);

    }
}
